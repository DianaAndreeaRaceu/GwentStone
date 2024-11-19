package org.poo.main;

import java.util.ArrayList;

public final class TheCursedOne extends Minion {
    public TheCursedOne(final String name, final int mana, final String description,
                    final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors, health, attackDamage);
    }

    /**
     * Use The Cursed One's ability.
     */
    public void useAbility(final Minion attack, final Minion target) {
        int aux = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(aux);
    }
}
