package org.poo.main;

import java.util.ArrayList;

public final class Disciple extends Minion {

    public Disciple(final String name, final int mana, final String description,
                    final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors, health, attackDamage);
    }

    /**
     *Use Disciple's ability.
     */
    public void useAbility(final Minion attack, final Minion target) {
        target.setHealth(target.getHealth() + 2);
    }
}
