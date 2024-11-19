package org.poo.main;

import java.util.ArrayList;
import java.util.Objects;

public class Minion extends Card {
    private int health;
    private int attackDamage;
    private int hasAttacked = 0;
    private int isFrozen = 0;

    public Minion(final String name, final int mana, final String description,
                  final ArrayList<String> colors, final int health, final int attackDamage) {
        super(name, mana, description, colors);
        this.health = health;
        this.attackDamage = attackDamage;
    }

    /**
     * Get minion's health.
     */
    public int getHealth() {
        return health;
    }

    /**
     * Set minion's health.
     */
    public void setHealth(final int health) {
        this.health = health;
    }

    /**
     * Get minion's attack damage.
     */
    public int getAttackDamage() {
        return attackDamage;
    }

    /**
     * Set minion's attack damage.
     */
    public void setAttackDamage(final int attackDamage) {
        this.attackDamage = attackDamage;
    }

    /**
     * Get minion's HasAttacked.
     */
    public int getHasAttacked() {
        return hasAttacked;
    }

    /**
     * Set minion's HasAttacked.
     */
    public void setHasAttacked(final int hasAttacked) {
        this.hasAttacked = hasAttacked;
    }

    /**
     * Get minion's IsFrozen.
     */
    public int getIsFrozen() {
        return isFrozen;
    }

    /**
     * Unfreezes the minion.
     */
    public void unfreeze() {
        isFrozen = 0;
    }

    /**
     * Freezes the minion.
     */
    public void freeze() {
        isFrozen = 1;
    }

    /**
     * Updates the attacked minion's health and marks the other's attack.
     */
    public void attack(final Minion other) {
        other.health = other.health - attackDamage;
        hasAttacked = 1;
    }

    /**
     * Check if the minion is a tank type.
     */
    public int isTank() {
        if (Objects.equals(this.getName(), "Goliath") || Objects.equals(this.getName(), "Warden")) {
            return 1;
        } else {
            return 0;
        }
    }

    /**
     * Determines if the minion should be placed on the back row.
     */
    public int isBackRow() {
        if (getName().equals("Sentinel")
                || getName().equals("Berserker")
                || getName().equals("The Cursed One")
                || getName().equals("Disciple")) {
            return 1;
        }
        return 0;
    }


    /**
     * Use minion's ability.
     */
    public void useAbility(final Minion attack, final Minion target) {

    }

    /**
     * Create a copy according the name of the minion.
     */
    public Minion createMinion(final String name, final int mana, final String description,
                               final ArrayList<String> colors) {
        switch (name) {
            case "Disciple":
                return new Disciple(name, mana, description, colors, health, attackDamage);
            case "Miraj":
                return new Miraj(name, mana, description, colors, health, attackDamage);
            case "The Cursed One":
                return new TheCursedOne(name, mana, description, colors, health, attackDamage);
            case "The Ripper":
                return new TheRipper(name, mana, description, colors, health, attackDamage);
            default: return null;
        }
    }
}
