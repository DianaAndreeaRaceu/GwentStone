package org.poo.main;

import java.util.ArrayList;

public final class EmpressThorina extends Hero {

    public EmpressThorina(final String name, final int mana, final String description,
                     final ArrayList<String> colors) {
        super(name, mana, description, colors);
    }

    /**
     Use Empress Thorina's ability.
     */
    public void useAbility(final Board board, final int x) {
        if (board.getMaxHealthCard(x) != -1) {
            board.removeMinion(x, board.getMaxHealthCard(x));
        }
    }
}
