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

    /**
     *Return the current player.
     */
    public Player getCurrentPlayer() {
        if (currentPlayer == 1) {
            return player1;
        }
        return player2;
    }

    /**
     *Unfreeze player one's cards on board.
     */
    public void unfreezeCardsPlayer1() {
        for (int x = 2; x < board.getRows(); x++) {
            for (int y = 0; y < board.getColumns(); y++) {
                if (board.getCard(x, y) != null && board.getCard(x, y).getIsFrozen() == 1) {
                    board.getCard(x, y).unfreeze();
                }
            }
        }
    }

    /**
     *Unfreeze player two's cards on board.
     */
    public void unfreezeCardsPlayer2() {
        for (int x = 0; x < board.getRows() / 2; x++) {
            for (int y = 0; y < board.getColumns(); y++) {
                if (board.getCard(x, y) != null && board.getCard(x, y).getIsFrozen() == 1) {
                    board.getCard(x, y).unfreeze();
                }
            }
        }
    }

    /**
     *Switch the players
     */
    private void switchPlayer() {
        if (currentPlayer == 1) {
            currentPlayer = 2;
        } else {
            currentPlayer = 1;
        }
    }

    /**
     *Begining of a new round
     */
    public void startNextRound() {
        player1.resetTurn();
        player2.resetTurn();

        player1.increaseMana();
        player2.increaseMana();

        player1.addCard();
        player2.addCard();

        board.resetHasAttackedForAllMinions();

        player1.getHero().setAbilityUsed(0);
        player2.getHero().setAbilityUsed(0);

    }

    /**
     *End of the round for the current player
     */
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

    /**
     *This method is determining the row for a certain card.
     */
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

    /**
     *Place one card on the board.
     */
    public ObjectNode placeCard(final int indexHand) {
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result;
        Player current = getCurrentPlayer();
        Card card = current.getHand().get(indexHand);

        if (card.getMana() > current.getMana()) {
            result = objectMapper.createObjectNode();
            result.put("command", "placeCard");
            result.put("handIdx", indexHand);
            result.put("error", "Not enough mana to place card on table.");
            return result;
        }

        int row = detRowForCard(current, card);
        if (board.firstPositionFree(row) == -1) {
            result = objectMapper.createObjectNode();
            result.put("command", "placeCard");
            result.put("handIdx", indexHand);
            result.put("error", "Cannot place card on table since row is full.");
            return result;
        }

        current.setMana(current.getMana() - card.getMana());
        board.addMinion((Minion) card, row);
        current.getHand().remove(indexHand);
        return null;
    }

    /**
     *Return 1 if the enemy has tnaks on board, else return 0.
     */
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

    /**
     *Card attack on board.
     */
    public ObjectNode cardAttack(final int attackX, final int attackY,
                                 final int targetX, final int targetY) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        Minion attack = board.getCard(attackX, attackY);
        Minion target = board.getCard(targetX, targetY);

        ObjectNode cardAttacker = mapper.createObjectNode();
        cardAttacker.put("x", attackX);
        cardAttacker.put("y", attackY);
        result.set("cardAttacker", cardAttacker);

        ObjectNode cardAttacked = mapper.createObjectNode();
        cardAttacked.put("x", targetX);
        cardAttacked.put("y", targetY);
        result.set("cardAttacked", cardAttacked);

        result.put("command", "cardUsesAttack");

        if ((currentPlayer == 2 && targetX < board.getRows() / 2)
                || (currentPlayer == 1 && targetX >= board.getRows() / 2)) {
            result.put("error", "Attacked card does not belong to the enemy.");
            return result;
        }

        if (attack.getHasAttacked() == 1) {
            result.put("error", "Attacker card has already attacked this turn.");
            return result;
        }

        if (attack.getIsFrozen() == 1) {
            result.put("error", "Attacker card is frozen.");
            return result;
        }

        if (tankInEnemy(targetX) == 1 && target.isTank() == 0) {
            result.put("error", "Attacked card is not of type 'Tank'.");
            return result;
        }

        attack.attack(target);
        if (target.getHealth() <= 0) {
            board.removeMinion(targetX, targetY);
        }
        return null;

    }

    /**
     *Method for using card ability.
     */
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

            result.put("command", "cardUsesAbility");
            result.put("error", "Attacker card is frozen.");
            return result;
        } else if (attack.getHasAttacked() == 1) {
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            ObjectNode cardAttacked = mapper.createObjectNode();
            cardAttacked.put("x", targetX);
            cardAttacked.put("y", targetY);
            result.set("cardAttacked", cardAttacked);

            result.put("command", "cardUsesAbility");
            result.put("error", "Attacker card has already attacked this turn.");
            return result;
        } else if (Objects.equals(attack.getName(), "Disciple")) {

            if ((attackX >= board.getRows() / 2 && targetX < board.getRows() / 2)
                    || (attackX < board.getRows() / 2 && targetX >= board.getRows() / 2)) {
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                ObjectNode cardAttacked = mapper.createObjectNode();
                cardAttacked.put("x", targetX);
                cardAttacked.put("y", targetY);
                result.set("cardAttacked", cardAttacked);

                result.put("command", "cardUsesAbility");
                result.put("error", "Attacked card does not belong to the current player.");
                return result;
            }
        } else if (Objects.equals(attack.getName(), "The Ripper")
                || Objects.equals(attack.getName(), "Miraj")
                || Objects.equals(attack.getName(), "The Cursed One")) {
            if ((attackX >= board.getRows() / 2 && targetX >= board.getRows() / 2)
                    || (attackX < board.getRows() / 2 && targetX < board.getRows() / 2)) {
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                ObjectNode cardAttacked = mapper.createObjectNode();
                cardAttacked.put("x", targetX);
                cardAttacked.put("y", targetY);
                result.set("cardAttacked", cardAttacked);

                result.put("command", "cardUsesAbility");
                result.put("error", "Attacked card does not belong to the enemy.");
                return result;
            } else if (tankInEnemy(targetX) == 1 && target.isTank() == 0) {
                ObjectNode cardAttacker = mapper.createObjectNode();
                cardAttacker.put("x", attackX);
                cardAttacker.put("y", attackY);
                result.set("cardAttacker", cardAttacker);

                ObjectNode cardAttacked = mapper.createObjectNode();
                cardAttacked.put("x", targetX);
                cardAttacked.put("y", targetY);
                result.set("cardAttacked", cardAttacked);

                result.put("command", "cardUsesAbility");
                result.put("error", "Attacked card is not of type 'Tank'.");
                return result;
            }
        }
        Minion minionTwo = attack.createMinion(attack.getName(), attack.getMana(),
                attack.getDescription(), attack.getColors());

        minionTwo.useAbility(attack, target);
        attack.setHasAttacked(1);
        if (target.getHealth() == 0) {
            board.removeMinion(targetX, targetY);
        }
        return result;
    }

    /**
     *Method for attacking the enemy hero.
     */
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
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            result.put("command", "useAttackHero");
            result.put("error", "Attacker card is frozen.");
            return result;
        }

        if (attack.getHasAttacked() == 1) {
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

            result.put("command", "useAttackHero");
            result.put("error", "Attacker card has already attacked this turn.");
            return result;
        }
        int targetRow;
        if (target == player1) {
            targetRow = 2;
        } else {
            targetRow = 1;
        }
        if (tankInEnemy(targetRow) == 1) {
            ObjectNode cardAttacker = mapper.createObjectNode();
            cardAttacker.put("x", attackX);
            cardAttacker.put("y", attackY);
            result.set("cardAttacker", cardAttacker);

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
        return result;
    }

    /**
     *Marks end of the game.
     */
    public void gameOver(final Player winner) {
        gameStatistics.increaseGames();
        if (winner == player1) {
            gameStatistics.increaseWins(1);
        } else {
            gameStatistics.increaseWins(2);
        }
    }

    /**
     *Verify if one row belong to the enemy.
     */
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

    /**
     *Method for using hero's ability.
     */
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

        }

        if (hero.getAbilityUsed() == 1) {
            result.put("affectedRow", abilityX);
            result.put("command", "useHeroAbility");
            result.put("error", "Hero has already attacked this turn.");
            return result;

        }

        if ((Objects.equals(hero.getName(), "Lord Royce")
                || Objects.equals(hero.getName(), "Empress Thorina"))
                && enemyRow(player, abilityX) == 0) {

            result.put("affectedRow", abilityX);
            result.put("command", "useHeroAbility");
            result.put("error", "Selected row does not belong to the enemy.");
            return result;

        }

        if ((Objects.equals(hero.getName(), "King Mudface")
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

    /**
     *Show cards in player's hand.
     */
    public ObjectNode getCardsInHand(final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = player1;
        } else {
            player = player2;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("command", "getCardsInHand");
        result.put("playerIdx", playerIdx);
        ArrayNode cardsArray = objectMapper.createArrayNode();

        for (int i = 0; i < player.getHand().size(); i++) {
            Card card = player.getHand().get(i);
            ObjectNode cardNode = objectMapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            Minion minionCard = (Minion) card;
            cardNode.put("attackDamage", minionCard.getAttackDamage());
            cardNode.put("health", minionCard.getHealth());
            cardNode.put("description", card.getDescription());

            ArrayNode colors = objectMapper.createArrayNode();
            for (int j = 0; j < card.getColors().size(); j++) {
                colors.add(card.getColors().get(j));
            }
            cardNode.set("colors", colors);
            cardNode.put("name", card.getName());
            cardsArray.add(cardNode);
        }
        result.set("output", cardsArray);
        return result;
    }

    /**
     *Show cards in player's deck.
     */
    public ObjectNode getPlayerDeck(final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = player1;
        } else {
            player = player2;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("command", "getPlayerDeck");
        result.put("playerIdx", playerIdx);
        ArrayNode outputArray = objectMapper.createArrayNode();

        if (player.getDeck() != null) {
            for (int i = 0; i < player.getDeck().size(); i++) {
                Card card = player.getDeck().get(i);
                ObjectNode cardNode = objectMapper.createObjectNode();
                cardNode.put("mana", card.getMana());

                Minion minionCard = (Minion) card;
                cardNode.put("attackDamage", minionCard.getAttackDamage());
                cardNode.put("health", minionCard.getHealth());
                cardNode.put("description", card.getDescription());

                ArrayNode colorsArray = objectMapper.createArrayNode();
                for (int j = 0; j < card.getColors().size(); j++) {
                    colorsArray.add(card.getColors().get(j));
                }
                cardNode.set("colors", colorsArray);
                cardNode.put("name", card.getName());
                outputArray.add(cardNode);
            }
        }
        result.set("output", outputArray);
        return result;
    }

    /**
     *Show the cards on table.
     */
    public ObjectNode getCardsOnTable() {
        ObjectNode result = board.getCardsOnBoard();
        result.put("command", "getCardsOnTable");
        return result;
    }

    /**
     *Show the current player.
     */
    public ObjectNode getPlayerTurn() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("command", "getPlayerTurn");
        result.put("output", currentPlayer);
        return result;
    }

    /**
     *Show the player's hero.
     */
    public ObjectNode getPlayerHero(final int playerIdx) {
        Player player;
        if (playerIdx == 1) {
            player = player1;
        } else {
            player = player2;
        }
        ObjectMapper objectMapper = new ObjectMapper();
        ObjectNode result = objectMapper.createObjectNode();
        result.put("command", "getPlayerHero");
        result.put("playerIdx", playerIdx);
        ObjectNode heroInfo = player.getHeroCard();
        result.set("output", heroInfo);
        return result;
    }

    /**
     *Show the card on board, at this position.
     */
    public ObjectNode getCardAtPosition(final int x, final int y) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        result.put("command", "getCardAtPosition");
        result.put("x", x);
        result.put("y", y);

        Minion card = board.getCard(x, y);
        if (card != null) {
            ObjectNode cardNode = mapper.createObjectNode();
            cardNode.put("mana", card.getMana());
            cardNode.put("attackDamage", card.getAttackDamage());
            cardNode.put("health", card.getHealth());
            cardNode.put("description", card.getDescription());

            ArrayNode colors = mapper.createArrayNode();
            for (int i = 0; i < card.getColors().size(); i++) {
                colors.add(card.getColors().get(i));
            }
            cardNode.set("colors", colors);
            cardNode.put("name", card.getName());
            result.set("output", cardNode);
        } else {
            result.put("output", "No card available at that position.");
        }
        return result;
    }

    /**
     *Show player's mana.
     */
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

    /**
     *Show frozen cards on table.
     */
    public ObjectNode getFrozenCardsOnTable() {
        ObjectNode result = board.getFreezedOnBoard();
        result.put("command", "getFrozenCardsOnTable");
        return result;
    }

}
