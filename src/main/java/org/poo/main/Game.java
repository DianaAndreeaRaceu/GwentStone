package org.poo.main;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.Objects;


public final class Game {
    private Player player1;
    private Player player2;
    private int currentPlayer;
    private Board board;
    private Statistics gameStatistics;

    public Game(final Player player1, final Player player2,
                final Board board, final Statistics stat) {
        this.player1 = player1;
        this.player2 = player2;
        this.board = board;
        this.gameStatistics = stat;
    }


    public void setCurrentPlayer(final int currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public Player getCurrentPlayer() {
        if (currentPlayer == 1) {
            return player1;
        }
        return player2;
    }

    public void unfreezeCardsPlayer1() {
        for (int x = 2; x < board.getRows(); x++) {
            for (int y = 0; y < board.getColumns(); y++) {
                if (board.getCard(x, y) != null && board.getCard(x, y).getIsFrozen() == 1) {
                    board.getCard(x, y).unfreeze();
                }
            }
        }
    }

    public void unfreezeCardsPlayer2() {
        for (int x = 0; x < board.getRows() / 2; x++) {
            for (int y = 0; y < board.getColumns(); y++) {
                if (board.getCard(x, y) != null && board.getCard(x, y).getIsFrozen() == 1) {
                    board.getCard(x, y).unfreeze();
                }
            }
        }
    }

    private void switchPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    public void startNextRound() {
        player1.resetTurn();
        player2.resetTurn();

        player1.increaseMana();
        player2.increaseMana();

        player1.addCard();
        player2.addCard();

        board.resetHasAttackedForAllMinions();
        //board.resetAbilityUsageForAllMinions();

        player1.getHero().setAbilityUsed(0);
        player2.getHero().setAbilityUsed(0);

    }

    public void endTurn() {
        Player current = getCurrentPlayer();
        current.endTurn();

        if (current == player1) {
            unfreezeCardsPlayer1();
        } else {
            unfreezeCardsPlayer2();
        }

        if (player1.getTurnEnded() == 1 && player2.getTurnEnded() == 1) {
            startNextRound();
        }
        switchPlayer();
    }

    public int detRowForCard(final Player player, final Card card) {
        Minion minion = (Minion) card;
        if (player == player1) {
            if (minion.isBackRow() == 1) {
                return (board.getRows() / 2) + 1; //3
            } else {
                return board.getRows() / 2; //2
            }
        } else {
            if (minion.isBackRow() == 1) {
                return 0; //0
            } else {
                return 1; //1
            }
        }
    }

    public ObjectNode placeCard(final int indexHand) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = null;  // Inițializăm ca `null` pentru cazurile în care nu dorim output

        Player current = getCurrentPlayer();

        // Verificare index invalid
        if (indexHand < 0 || indexHand >= current.getHand().size()) {
            // Returnăm `null` pentru a ignora complet acest caz
            return null;
        }

        Card card = current.getHand().get(indexHand);

        // Verificare mană insuficientă - adăugăm `command` și eroarea specificată
        if (card.getMana() > current.getMana()) {
            result = objectMapper.createObjectNode();
            result.put("command", "placeCard");
            result.put("handIdx", indexHand);
            result.put("error", "Not enough mana to place card on table.");
            System.out.println("Not enough mana to place card on table.");
            return result;
        }

        // Determinare rând pentru carte și verificare
        int row = detRowForCard(current, card);
        if (row == -1) {
            // Returnăm `null` pentru a ignora complet acest caz
            return null;
        }

        // Verificare dacă rândul este plin - adăugăm `command` și eroarea specificată
        if (board.firstPositionFree(row) == -1) {
            result = objectMapper.createObjectNode();
            result.put("command", "placeCard");
            result.put("handIdx", indexHand);
            result.put("error", "Cannot place card on table since row is full.");
            System.out.println("Cannot place card on table since row is full.");
            return result;
        }

        // Actualizare mană și plasare carte pe masă dacă toate condițiile sunt îndeplinite
        current.setMana(current.getMana() - card.getMana());
        board.addMinion((Minion) card, row);
        current.getHand().remove(indexHand);

        board.printBoard();
        // Returnăm `null` pentru a nu include în output-ul final cazurile normale de succes
        return null;
    }


    public int tankInEnemy(final int targetX) {
        if (targetX < board.getRows() / 2) {
            for (int x = 0; x < board.getRows() / 2; x++) {
                for (int y = 0; y < board.getColumns(); y++) {
                    if (board.getCard(x, y) != null && board.getCard(x, y).isTank() == 1) {
                        return 1;
                    }
                }
            }
        } else {
            for (int x = 2; x < board.getRows(); x++) {
                for (int y = 0; y < board.getColumns(); y++) {
                    if (board.getCard(x, y) != null && board.getCard(x, y).isTank() == 1) {
                        return 1;
                    }
                }
            }
        }
        return 0;
    }

    //atac inte carti
    public ObjectNode cardAttack(final int attackX, final int attackY,
                                 final int targetX, final int targetY) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        Minion attack = board.getCard(attackX, attackY);
        Minion target = board.getCard(targetX, targetY);

        // Adăugăm informațiile despre atacator și țintă în răspuns
        ObjectNode cardAttacker = mapper.createObjectNode();
        cardAttacker.put("x", attackX);
        cardAttacker.put("y", attackY);
        result.set("cardAttacker", cardAttacker);

        ObjectNode cardAttacked = mapper.createObjectNode();
        cardAttacked.put("x", targetX);
        cardAttacked.put("y", targetY);
        result.set("cardAttacked", cardAttacked);

        // Adăugăm comanda în răspuns
        result.put("command", "cardUsesAttack");

        if (attack == null) {
            result.put("error", "No card found at the specified position to attack.");
            return result;
        }

        if (target == null) {
            result.put("error", "No target card found at the specified position.");
            return result;
        }

        if ((currentPlayer == 2 && targetX < board.getRows() / 2)
                || (currentPlayer == 1 && targetX >= board.getRows() / 2)) {
            result.put("error", "Attacked card does not belong to the enemy.");
        } else if (attack.getHasAttacked() == 1) {
            result.put("error", "Attacker card has already attacked this turn.");
        } else if (attack.getIsFrozen() == 1) {
            result.put("error", "Attacker card is frozen.");
        } else if (tankInEnemy(targetX) == 1 && target.isTank() == 0) {
            result.put("error", "Attacked card is not of type 'Tank'.");
        } else {
            attack.attack(target);
            if (target.getHealth() <= 0) {
                board.removeMinion(targetX, targetY);
            }
            return null; // Successfully attacked without error
        }
        return result;
    }


    public ObjectNode useCardAbility(final int attackX, final int attackY,
                                     final int targetX, final int targetY) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        Minion attack = board.getCard(attackX, attackY);
        Minion target = board.getCard(targetX, targetY);

        if (attack.getIsFrozen() == 1) {
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            ObjectNode cardAttacked = mapper.createObjectNode();
            cardAttacked.put("x", targetX);
            cardAttacked.put("y", targetY);
            result.set("cardAttacked", cardAttacked);

            // Adăugăm comanda în răspuns
            result.put("command", "cardUsesAbility");

            result.put("error", "Attacker card is frozen.");
            return result;
        } else if (attack.getHasAttacked() == 1) {
            // Adăugăm informațiile despre atacator și țintă în răspuns
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            ObjectNode cardAttacked = mapper.createObjectNode();
            cardAttacked.put("x", targetX);
            cardAttacked.put("y", targetY);
            result.set("cardAttacked", cardAttacked);

            // Adăugăm comanda în răspuns
            result.put("command", "cardUsesAbility");

            result.put("error", "Attacker card has already attacked this turn.");
            return result;
        } else if (Objects.equals(attack.getName(), "Disciple")) {

            if ((attackX >= board.getRows() / 2 && targetX < board.getRows() / 2)
                    || (attackX < board.getRows() / 2 && targetX >= board.getRows() / 2)) {
                // Adăugăm informațiile despre atacator și țintă în răspuns
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                ObjectNode cardAttacked = mapper.createObjectNode();
                cardAttacked.put("x", targetX);
                cardAttacked.put("y", targetY);
                result.set("cardAttacked", cardAttacked);

                // Adăugăm comanda în răspuns
                result.put("command", "cardUsesAbility");

                result.put("error", "Attacked card does not belong to the current player.");
                return result;
            }
        } else if (Objects.equals(attack.getName(), "The Ripper")
                || Objects.equals(attack.getName(), "Miraj")
                || Objects.equals(attack.getName(), "The Cursed One")) {
            if ((attackX >= board.getRows() / 2 && targetX >= board.getRows() / 2)
                    || (attackX < board.getRows() / 2 && targetX < board.getRows() / 2)) {
                // Adăugăm informațiile despre atacator și țintă în răspuns
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                ObjectNode cardAttacked = mapper.createObjectNode();
                cardAttacked.put("x", targetX);
                cardAttacked.put("y", targetY);
                result.set("cardAttacked", cardAttacked);

                // Adăugăm comanda în răspuns
                result.put("command", "cardUsesAbility");

                result.put("error", "Attacked card does not belong to the enemy.");
                return result;
            } else if (tankInEnemy(targetX) == 1 && target.isTank() == 0) {
                // Adăugăm informațiile despre atacator și țintă în răspuns
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                ObjectNode cardAttacked = mapper.createObjectNode();
                cardAttacked.put("x", targetX);
                cardAttacked.put("y", targetY);
                result.set("cardAttacked", cardAttacked);

                // Adăugăm comanda în răspuns
                result.put("command", "cardUsesAbility");

                result.put("error", "Attacked card is not of type 'Tank'.");
                return result;
            }
        }
        Minion minionTwo = attack.createMinion(attack.getName(), attack.getMana(), attack.getDescription(),
                attack.getColors(), attack.getHealth(), attack.getAttackDamage());

        minionTwo.useAbility(attack, target);
        attack.setHasAttacked(1);
        if (target.getHealth() == 0) {
            board.removeMinion(targetX, targetY);
        }
        return result;
    }

    public ObjectNode attackHero(final int attackX, final int attackY) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        Minion attack = board.getCard(attackX, attackY);

        Player target;
        if (currentPlayer == 1) {
            target = player2;
        } else {
            target = player1;
        }
        Hero hero = target.getHero();

        if (attack.getIsFrozen() == 1) {
            // Adăugăm informațiile despre atacator și țintă în răspuns
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            // Adăugăm comanda în răspuns
            result.put("command", "useAttackHero");
            result.put("error", "Attacker card is frozen.");
        } else if (attack.getHasAttacked() == 1) {
            // Adăugăm informațiile despre atacator și țintă în răspuns
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            // Adăugăm comanda în răspuns
            result.put("command", "useAttackHero");
            result.put("error", "Attacker card has already attacked this turn.");
        } else {
            int targetRow;
            if (target == player1) {
                targetRow = 2;
            } else {
                targetRow = 1;
            }
            if (tankInEnemy(targetRow) == 1) {
                // Adăugăm informațiile despre atacator și țintă în răspuns
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                // Adăugăm comanda în răspuns
                result.put("command", "useAttackHero");
                result.put("error", "Attacked card is not of type 'Tank'.");
            } else {
                hero.setHealth(hero.getHealth() - attack.getAttackDamage());
                attack.setHasAttacked(1);
                if (hero.getHealth() <= 0) {
                    if (currentPlayer == 2) {
                        result.put("gameEnded", "Player two killed the enemy hero.");
                        gameOver(player2);
                    } else {
                        result.put("gameEnded", "Player one killed the enemy hero.");
                        gameOver(player1);
                    }
                }
            }
        }
        return result;
    }

    public void gameOver(final Player winner) {
        gameStatistics.increaseGames();
        if (winner == player1) {
            gameStatistics.increaseWins(1);
        } else {
            gameStatistics.increaseWins(2);
        }
    }

    public int enemyRow(final Player player, final int row) {
        if (player == player1) {
            if (row == 0 || row == 1) {
                return 1;
            }
            return 0;
        } else {
            if (row == board.getRows() / 2 || row == (board.getRows() / 2) + 1) {
                return 1;
            }
            return 0;
        }
    }

    public ObjectNode useHeroAbility(final int abilityX) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        Player player;
        if (currentPlayer == 1) {
            player = player1;
        } else {
            player = player2;
        }
        Hero hero = player.getHero();

        if (hero == null) {
            result.put("error", "No hero assigned to the player.");
            return result;
        }

        if (player.getMana() < hero.getMana()) {
            result.put("affectedRow", abilityX);
            result.put("command", "useHeroAbility");
            result.put("error", "Not enough mana to use hero's ability.");
            return result;

        } else if (hero.getAbilityUsed() == 1) {
            result.put("affectedRow", abilityX);
            result.put("command", "useHeroAbility");
            result.put("error", "Hero has already attacked this turn.");
            return result;

        } else if ((Objects.equals(hero.getName(), "Lord Royce")
                || Objects.equals(hero.getName(), "Empress Thorina"))
                && enemyRow(player, abilityX) == 0) {

            result.put("affectedRow", abilityX);
            result.put("command", "useHeroAbility");
            result.put("error", "Selected row does not belong to the enemy.");
            return result;

        } else if ((Objects.equals(hero.getName(), "King Mudface")
                || Objects.equals(hero.getName(), "General Kocioraw"))
                && enemyRow(player, abilityX) == 1) {

            result.put("affectedRow", abilityX);
            result.put("command", "useHeroAbility");
            result.put("error", "Selected row does not belong to the current player.");
            return result;
        }
        Hero heroTwo = hero.createHero(hero.getName(), hero.getMana(),
                hero.getDescription(), hero.getColors());
        heroTwo.useAbility(board, abilityX);
        hero.setAbilityUsed(1);
        player.useHero();
        return result;
    }

    public ObjectNode getCardsInHand(final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = player1;
        } else {
            player = player2;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        // Adăugăm "command" pentru a indica acțiunea
        result.put("command", "getCardsInHand");
        result.put("playerIdx", playerIdx);


        // Creăm un ArrayNode pentru a stoca cărțile din mână
        ArrayNode cardsArray = objectMapper.createArrayNode();

        // Parcurgem mâna jucătorului folosind un for clasic
        for (int i = 0; i < player.getHand().size(); i++) {
            Card card = player.getHand().get(i);
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());

            if (card instanceof Minion) {
                Minion minionCard = (Minion) card;
                cardNode.put("attackDamage", minionCard.getAttackDamage());
                cardNode.put("health", minionCard.getHealth());
            }

            cardNode.put("description", card.getDescription());

            // Adăugăm culorile cărții într-un ArrayNode
            ArrayNode colors = objectMapper.createArrayNode();
            for (int j = 0; j < card.getColors().size(); j++) {
                colors.add(card.getColors().get(j));
            }
            cardNode.set("colors", colors);

            // Adăugăm numele cărții
            cardNode.put("name", card.getName());

            // Adăugăm obiectul `cardNode` în `cardsArray`
            cardsArray.add(cardNode);
        }

        // Setăm lista de cărți în câmpul "output"
        result.set("output", cardsArray);

        return result;
    }

    public ObjectNode getPlayerDeck(final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = player1;
        } else {
            player = player2;
        }
        // Construim răspunsul JSON
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();

        // Adăugăm comanda și indexul jucătorului
        result.put("command", "getPlayerDeck");
        result.put("playerIdx", playerIdx);

        // Construim lista de cărți ca un ArrayNode
        ArrayNode outputArray = objectMapper.createArrayNode();

        // Iterăm prin fiecare carte din deck-ul jucătorului
        if (player.getDeck() != null) { // Asigurăm că deck-ul nu este null
            for (Card card : player.getDeck()) {
                ObjectNode cardNode = objectMapper.createObjectNode();
                cardNode.put("mana", card.getMana());

                cardNode.put("attackDamage", ((Minion) card).getAttackDamage());
                cardNode.put("health", ((Minion) card).getHealth());
                cardNode.put("description", card.getDescription());

                // Adăugăm culorile cărții într-un ArrayNode
                ArrayNode colorsArray = objectMapper.createArrayNode();
                for (String color : card.getColors()) {
                    colorsArray.add(color);
                }
                cardNode.set("colors", colorsArray);

                // Adăugăm numele cărții
                cardNode.put("name", card.getName());

                // Adăugăm cartea în lista de output
                outputArray.add(cardNode);
            }
        } else {
            System.out.println("Deck is not initialized.");
        }

        // Adăugăm lista de cărți la rezultatul final
        result.set("output", outputArray);
        return result;
    }

    public ObjectNode getCardsOnTable() {
        ObjectNode result = board.getCardsOnBoard();
        result.put("command", "getCardsOnTable");
        return result;
    }

    public ObjectNode getPlayerTurn() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("command", "getPlayerTurn");
        result.put("output", currentPlayer);
        return result;
    }

    public ObjectNode getPlayerHero(final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = player1;
        } else {
            player = player2;
        }
        // Construim răspunsul JSON și adăugăm întâi "command" și "playerIdx"
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("command", "getPlayerHero");
        result.put("playerIdx", playerIdx);

        // Creăm un sub-obiect pentru detaliile eroului
        ObjectNode heroInfo = player.getHeroCard();
        result.set("output", heroInfo);

        return result;
    }

    public ObjectNode getCardAtPosition(final int x, final int y) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        // Adăugăm comanda și coordonatele la rezultat
        result.put("command", "getCardAtPosition");
        result.put("x", x);
        result.put("y", y);

        // Obținem cartea de la poziția specificată
        Minion card = board.getCard(x, y);

        // Verificăm dacă există o carte la poziția specificată
        if (card != null) {
            // Creăm un nod pentru detaliile cărții
            ObjectNode cardNode = mapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            // Adăugăm culorile cărții într-un ArrayNode
            ArrayNode colors = mapper.createArrayNode();
            for (String color : card.getColors()) {
                colors.add(color);
            }
            cardNode.set("colors", colors);
            cardNode.put("name", card.getName());

            // Adăugăm nodul card la rezultatul final
            result.set("output", cardNode);
        } else {
            // Dacă nu există o carte la acea poziție, setăm mesajul de eroare
            result.put("output", "No card available at that position.");
        }

        return result;
    }


    public ObjectNode getPlayerMana(final int playerIdx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("command", "getPlayerMana");
        result.put("playerIdx", playerIdx);
        int mana;
        if (playerIdx == 1) {
            mana = player1.getMana();
        } else {
            mana = player2.getMana();
        }
        result.put("output", mana);
        return result;
    }

    public ObjectNode getFrozenCardsOnTable() {
        ObjectNode result = board.getFreezedOnBoard();
        result.put("command", "getFrozenCardsOnTable");
        return result;
    }

}
