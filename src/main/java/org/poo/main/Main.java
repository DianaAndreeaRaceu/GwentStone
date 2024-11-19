package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.poo.checker.Checker;
import org.poo.checker.CheckerConstants;
import org.poo.fileio.*;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Random;

/**
 * The entry point to this homework. It runs the checker that tests your implentation.
 */
public final class Main {
    /**
     * for coding style
     */
    private Main() {
    }

    /**
     * DO NOT MODIFY MAIN METHOD
     * Call the checker
     * @param args from command line
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void main(final String[] args) throws IOException {
        File directory = new File(CheckerConstants.TESTS_PATH);
        Path path = Paths.get(CheckerConstants.RESULT_PATH);

        if (Files.exists(path)) {
            File resultFile = new File(String.valueOf(path));
            for (File file : Objects.requireNonNull(resultFile.listFiles())) {
                file.delete();
            }
            resultFile.delete();
        }
        Files.createDirectories(path);

        for (File file : Objects.requireNonNull(directory.listFiles())) {
            String filepath = CheckerConstants.OUT_PATH + file.getName();
            File out = new File(filepath);
            boolean isCreated = out.createNewFile();
            if (isCreated) {
                action(file.getName(), filepath);
            }
        }

        Checker.calculateScore();
    }

    /**
     * @param filePath1 for input file
     * @param filePath2 for output file
     * @throws IOException in case of exceptions to reading / writing
     */
    public static void action(final String filePath1,
                              final String filePath2) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        Input inputData = objectMapper.readValue(new File(CheckerConstants.TESTS_PATH + filePath1),
                Input.class);
        ArrayNode output = objectMapper.createArrayNode();

        ArrayList<GameInput> games = inputData.getGames();
        Statistics gameStatistics = new Statistics();
        for (int i = 0; i < games.size(); i++) {
            StartGameInput startGame = games.get(i).getStartGame();

            ArrayList<Card> playerOneDeck = buildDeck(inputData.getPlayerOneDecks()
                    .getDecks().get(startGame.getPlayerOneDeckIdx()));
            ArrayList<Card> playerTwoDeck = buildDeck(inputData.getPlayerTwoDecks()
                    .getDecks().get(startGame.getPlayerTwoDeckIdx()));

            int shuffleSeed = startGame.getShuffleSeed();
            Collections.shuffle(playerOneDeck, new Random(shuffleSeed));
            Collections.shuffle(playerTwoDeck, new Random(shuffleSeed));

            Hero playerOneHero = new Hero(
                    startGame.getPlayerOneHero().getName(), startGame.getPlayerOneHero().getMana(),
                    startGame.getPlayerOneHero().getDescription(),
                    startGame.getPlayerOneHero().getColors()
            );
            Hero playerTwoHero = new Hero(
                    startGame.getPlayerTwoHero().getName(), startGame.getPlayerTwoHero().getMana(),
                    startGame.getPlayerTwoHero().getDescription(),
                    startGame.getPlayerTwoHero().getColors()
            );
            Player player1 = new Player(1, playerOneDeck, playerOneHero);
            Player player2 = new Player(1, playerTwoDeck, playerTwoHero);
            if (!playerOneDeck.isEmpty()) {
                player1.addCard();
            }
            if (!playerTwoDeck.isEmpty()) {
                player2.addCard();
            }
            Board board = new Board();
            Game game = new Game(player1, player2, board, gameStatistics);
            game.setCurrentPlayer(startGame.getStartingPlayer());
            ArrayList<ActionsInput> actions = inputData.getGames().get(i).getActions();
            for (ActionsInput action : actions) {
                String command = action.getCommand();
                System.out.println("Executing command: " + command);
                ObjectNode actionResult = null;
                switch (action.getCommand()) {
                    case "getCardsInHand":
                        actionResult = game.getCardsInHand(action.getPlayerIdx());
                        break;

                    case "getPlayerDeck":
                        actionResult = game.getPlayerDeck(action.getPlayerIdx());
                        break;

                    case "getCardsOnTable":
                        actionResult = game.getCardsOnTable();
                        break;

                    case "getPlayerTurn":
                        actionResult = game.getPlayerTurn();
                        break;

                    case "getPlayerHero":
                        actionResult = game.getPlayerHero(action.getPlayerIdx());
                        break;

                    case "getCardAtPosition":
                        actionResult = game.getCardAtPosition(action.getX(), action.getY());
                        break;

                    case "getFrozenCardsOnTable":
                        actionResult = game.getFrozenCardsOnTable();
                        break;

                    case "getPlayerMana":
                        actionResult = game.getPlayerMana(action.getPlayerIdx());
                        break;

                    case "getTotalGamesPlayed":
                        actionResult = gameStatistics.getTotalGamesPlayedJson();
                        break;

                    case "getPlayerOneWins":
                        actionResult = gameStatistics.getPlayerWinsJson(1);
                        break;

                    case "getPlayerTwoWins":
                        actionResult = gameStatistics.getPlayerWinsJson(2);
                        break;

                    case "placeCard":
                        actionResult = game.placeCard(action.getHandIdx());
                        break;

                    case "endPlayerTurn":
                        game.endTurn();
                        break;

                    case "useAttackHero":
                        actionResult = game.attackHero(action.getCardAttacker().getX(),
                                action.getCardAttacker().getY());
                        break;

                    case "cardUsesAttack":
                        actionResult = game.cardAttack(action.getCardAttacker().getX(),
                                action.getCardAttacker().getY(), action.getCardAttacked().getX(),
                                action.getCardAttacked().getY());
                        break;

                    case "cardUsesAbility":
                        actionResult = game.useCardAbility(action.getCardAttacker().getX(),
                                action.getCardAttacker().getY(), action.getCardAttacked().getX(),
                                action.getCardAttacked().getY());
                        break;

                    case "useHeroAbility":
                        actionResult = game.useHeroAbility(action.getAffectedRow());
                        break;

                    default:
                        actionResult = objectMapper.createObjectNode();
                        actionResult.put("error", "Command not recognized.");
                        break;
                }
                if (actionResult != null && !actionResult.isEmpty()) {
                    output.add(actionResult);
                }
            }
        }
        ObjectWriter objectWriter = objectMapper.writerWithDefaultPrettyPrinter();
        objectWriter.writeValue(new File(filePath2), output);
    }

    private static ArrayList<Card> buildDeck(final ArrayList<CardInput> cardInputs) {
        ArrayList<Card> deck = new ArrayList<>();
        for (CardInput cardInput : cardInputs) {
            if (cardInput.getAttackDamage() != 0 || cardInput.getHealth() != 0) {
                deck.add(new Minion(cardInput.getName(), cardInput.getMana(),
                        cardInput.getDescription(), cardInput.getColors(),
                        cardInput.getHealth(), cardInput.getAttackDamage()));
            }
        }
        return deck;
    }

}
