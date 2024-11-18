package org.poo.main;

import java.util.ArrayList;

public class TheCursedOne extends Minion{
    public TheCursedOne(final String name, final int mana, final String description,
                    final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors, health, attackDamage);
    }

    /**
     * Use the 'The Cursed One' minion's ability.
     */
    public void useAbility(final Minion attack, final Minion target) {
        int aux = target.getHealth();
        target.setHealth(target.getAttackDamage());
        target.setAttackDamage(aux);
        System.out.println("sau interschimbat atacul cu health-ul pentru " + target.getName());
    }
}
