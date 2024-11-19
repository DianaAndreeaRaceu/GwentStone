package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

public final class Statistics {
    private int wins1;
    private int wins2;
    private int games;

    public Statistics() {
        this.wins1 = 0;
        this.wins2 = 0;
        this.games = 0;
    }

    /**
     *Count the current game that ended.
     */
    public void increaseGames() {
        games++;
    }

    /**
     *Count winner player's score.
     */
    public void increaseWins(final int index) {
        if (index == 1) {
            wins1++;
        } else {
            wins2++;
        }
    }

    /**
     *Show total games played.
     */
    public ObjectNode getTotalGamesPlayedJson() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        result.put("command", "getTotalGamesPlayed");
        result.put("output", games);
        return result;
    }

    /**
     *Show player's wins.
     */
    public ObjectNode getPlayerWinsJson(final int playerIdx) {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();
        //result.put("playerIdx", playerIdx);

        if (playerIdx == 1) {
            result.put("command", "getPlayerOneWins");
            result.put("output", wins1);
        } else if (playerIdx == 2) {
            result.put("command", "getPlayerTwoWins");
            result.put("output", wins2);
        }

        return result;
    }
}
