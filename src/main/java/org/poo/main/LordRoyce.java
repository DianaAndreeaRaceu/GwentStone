package org.poo.main;

import java.util.ArrayList;

public final class LordRoyce extends Hero {

    public LordRoyce(final String name, final int mana, final String description,
                     final ArrayList<String> colors) {
        super(name, mana, description, colors);
    }

    /**
     Use Lord Royce's ability.
     */
    public void useAbility(final Board board, final int x) {
        for (int y = 0; y < board.getColumns(); y++) {
            if (board.getCard(x, y) != null) {
                board.getCard(x, y).freeze();
            }
        }
    }
}
