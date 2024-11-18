package org.poo.main;

import java.util.ArrayList;

public class KingMudface extends Hero{

    public KingMudface(final String name, final int mana, final String description,
                     final ArrayList<String> colors) {
        super(name, mana, description, colors);
    }

    public void useAbility(final Board board, final int x) {
        for (int y = 0; y < board.getColumns(); y++) {
            if (board.getCard(x, y) != null) {
                board.getCard(x, y).setHealth(board.getCard(x, y).getHealth() + 1);
            }
        }
    }
}
