package org.poo.main;

import java.util.ArrayList;

public class Card {
    private String name;
    private int mana;
    private String description;
    private ArrayList<String> colors;

    public Card(final String name, final int mana,
                final String description,  final ArrayList<String> colors) {
        this.name = name;
        this.mana = mana;
        this.description = description;
        this.colors = colors;
    }

    public final String getName() {
        return name;
    }

    public final void setName(final String name) {
        this.name = name;
    }

    public final int getMana() {
        return mana;
    }

    public final void setMana(final int mana) {
        this.mana = mana;
    }

    public final String getDescription() {
        return description;
    }

    public final void setDescription(final String description) {
        this.description = description;
    }

    public final ArrayList<String> getColors() {
        return colors;
    }

    public final void setColors(final ArrayList<String> colors) {
        this.colors = colors;
    }
}
