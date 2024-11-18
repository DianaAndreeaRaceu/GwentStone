package org.poo.main;

import java.util.ArrayList;

public class TheRipper extends Minion{
    public TheRipper(final String name, final int mana, final String description,
                    final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors, health, attackDamage);
    }

    /**
     * Use the 'The Ripper' minion's ability.
     */
    public void useAbility(final Minion attack, final Minion target) {
        target.setAttackDamage(target.getAttackDamage() - 2);
        if (target.getAttackDamage() < 0) {
            target.setAttackDamage(0);
        }
        System.out.println("Atacul a scazut cu 2  Pentru " + target.getName() + "si este " + target.getAttackDamage());
    }
}
