package org.poo.main;

import java.util.ArrayList;

public class Hero extends Card {
    private int health;
    private int abilityUsed = 0;
    private static final int DEFAULT_HEALTH = 30;

    public Hero(final String name, final int mana, final String description,
                final ArrayList<String> colors) {
        super(name, mana, description, colors);
        this.health = DEFAULT_HEALTH;
    }

    public int getHealth() {
        return health;
    }

    public void setHealth(final int health) {
        this.health = health;
    }

    public void setAbilityUsed(final int abilityUsed) {
        this.abilityUsed = abilityUsed;
    }

    public int getAbilityUsed() {
        return abilityUsed;
    }

    public void useAbility(final Board board, final int x) {

    }

    public Hero createHero(String heroName, int mana, String description, ArrayList<String> colors) {
        switch (heroName) {
            case "King Mudface":
                return new KingMudface(heroName, mana, description, colors);
            case "General Kocioraw":
                return new GeneralKocioraw(heroName, mana, description, colors);
            case "Lord Royce":
                return new LordRoyce(heroName, mana, description, colors);
            case "Empress Thorina":
                return new EmpressThorina(heroName, mana, description, colors);
        }
        return null;
    }


}
