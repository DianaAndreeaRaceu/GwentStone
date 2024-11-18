package org.poo.main;

import java.util.ArrayList;

public class Miraj extends Minion{

    public Miraj(final String name, final int mana, final String description,
                    final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors, health, attackDamage);
    }

    public void useAbility(final Minion attack, final Minion target) {
        int aux = attack.getHealth();
        attack.setHealth(target.getHealth());
        target.setHealth(aux);
        System.out.println("s-au interschimbat sanatatile !!!!!!!!!!!!!");
    }
}
