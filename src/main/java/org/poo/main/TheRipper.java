package org.poo.main;

import java.util.ArrayList;

public final class TheRipper extends Minion {
    public TheRipper(final String name, final int mana, final String description,
                    final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors, health, attackDamage);
    }

    /**
     * Use The Ripper's ability.
     */
    public void useAbility(final Minion attack, final Minion target) {
        target.setAttackDamage(target.getAttackDamage() - 2);
        if (target.getAttackDamage() < 0) {
            target.setAttackDamage(0);
        }
    }
}
