package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Board {
    private Minion[][] board;
    private static final int ROWS = 4;
    private static final int COLUMNS = 5;

    public Board() {
        board = new Minion[ROWS][COLUMNS];
    }

    public int getRows() {
        return ROWS;
    }

    public int getColumns() {
        return COLUMNS;
    }

    /**
     * Returns the first free position in the row or -1 if the row is full.
     */
    public int firstPositionFree(final int x) {
        for (int y = 0; y < COLUMNS; y++) {
            if (board[x][y] == null) {
                return y; //first free position
            }
        }
        return -1;
    }

    /**
     * Check if the row is empty.
     */
    public int emptyRow(final int x) {
        for (int y = 0; y < COLUMNS; y++) {
            if (board[x][y] != null) {
                return 0;
            }
        }
        return 1;
    }

    /**
     * Add the minion to the game board, on the row x.
     */
    public void addMinion(final Minion minion, final int x) {
        if (firstPositionFree(x) != -1) {
            board[x][firstPositionFree(x)] = minion;
        }
    }

    /**
     * Remove the minion with (x, y) coordinates of the game board.
     */
    public void removeMinion(final int x, final int y) {
        board[x][y] = null;

        for (int i = y; i < COLUMNS - 1; i++) {
            board[x][i] = board[x][i + 1];
        }
        board[x][COLUMNS - 1] = null;
    }

    /**
     * Returns the first free position in the row or -1 if the row is full.
     */
    public void printBoard() {
        System.out.println("Current board state:");
        for (int i = 0; i < ROWS; i++) {
            System.out.print("Row " + i + ": ");
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] != null) {
                    System.out.print("[" + board[i][j].getName()
                            + " HP:" + board[i][j].getHealth() + "] ");
                } else {
                    System.out.print("[Empty] ");
                }
            }
            System.out.println();
        }
    }

    /**
     * Resets the attack of each minion on the board.
     */
    public void resetHasAttackedForAllMinions() {
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (board[i][j] != null) {
                    board[i][j].setHasAttacked(0); // Setați has_attacked la 0 pentru fiecare minion
                }
            }
        }
    }

    /**
     * Returns the minion on (x, y) position on board.
     */
    public Minion getCard(final int x, final int y) {
        return board[x][y];
    }

    /**
     * Returns the index of the minion in the row on the board that has the highest health.
     */
    public int getMaxHealthCard(final int x) {
        int maxH = 0;
        int index = 0;
        if (emptyRow(x) == 1) {
            return -1;
        }
        for (int y = 0; y < COLUMNS; y++) {
            if (board[x][y] != null && board[x][y].getHealth() > maxH) {
                maxH = board[x][y].getHealth();
                index = y;
            }
        }
        return index;
    }

    /**
     * Displays the cards on the board in JSON format.
     */
    public ObjectNode getCardsOnBoard() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        // Adăugăm comanda la rezultat
        result.put("command", "getCardsOnTable");

        // Creăm un ArrayNode pentru a stoca toate rândurile
        ArrayNode outputArray = mapper.createArrayNode();

        // Parcurgem rândurile de la 0 la 3 în ordinea corectă
        for (int i = 0; i < ROWS; i++) {
            // Creează un nou ArrayNode pentru fiecare rând
            ArrayNode rowArray = mapper.createArrayNode();

            // Parcurgem coloanele de la 0 la 4 pentru fiecare rând
            for (int j = 0; j < COLUMNS; j++) {
                Minion card = getCard(i, j);  // Obține cartea de la poziția (i, j)

                if (card != null) {
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

                    // Adăugăm cartea la rândul curent (rowArray)
                    rowArray.add(cardNode);
                }
            }

            // Adaugă `rowArray` la `outputArray` fără inversare
            outputArray.add(rowArray);
        }

        // Setăm lista de rânduri sub cheia "output" în `result`
        result.set("output", outputArray);

        return result;
    }


    /**
     * It displays in JSON format the cards on the board that are frozen.
     */
    public ObjectNode getFreezedOnBoard() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        ArrayNode outputArray = mapper.createArrayNode();
        for (int i = 0; i < ROWS; i++) {
            for (int j = 0; j < COLUMNS; j++) {
                if (getCard(i, j) != null && getCard(i, j).getIsFrozen() == 1) {
                    Minion card = getCard(i, j);
                    ObjectNode cardNode = mapper.createObjectNode();
                    cardNode.put("mana", card.getMana());
                    cardNode.put("attackDamage", card.getAttackDamage());
                    cardNode.put("health", card.getHealth());
                    cardNode.put("description", card.getDescription());
                    ArrayNode colors = mapper.createArrayNode();
                    for (int k = 0; k < card.getColors().size(); k++) {
                        colors.add(card.getColors().get(k));
                    }
                    cardNode.set("colors", colors);
                    cardNode.put("name", card.getName());

                    outputArray.add(cardNode);
                }
            }
        }
        result.set("output", outputArray);
        return result;
    }

}
