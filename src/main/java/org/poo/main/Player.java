package org.poo.main;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.ArrayList;
import java.util.List;

public final class Player {
    private int mana;
    private int increaseMana = 1;
    private static final int LIMIT_MANA = 10;
    private Hero hero;
    private List<Card> hand;
    private List<Card> deck;
    private int turnEnded = 0;

    public Player(final int mana, final List<Card> selectedDeck, final Hero hero) {
        this.mana = mana;
        this.hand = new ArrayList<>();
        this.hero = hero;
        this.deck = new ArrayList<>(selectedDeck);
    }

    public int getMana() {
        return mana;
    }

    public void setMana(final int mana) {
        this.mana = mana;
    }

    public Hero getHero() {
        return hero;
    }

    public void setHero(final Hero hero) {
        this.hero = hero;
    }

    public List<Card> getHand() {
        return hand;
    }

    public List<Card> getDeck() {
        return deck;
    }

    public int getTurnEnded() {
        return turnEnded;
    }


    /**
     * Add a card from deck to the player's hand.
     */
    public void addCard() {
        if (deck != null && !deck.isEmpty()) {
            Card card = deck.remove(0);
            hand.add(card);
        }
    }

    /**
     * Increases the player's mana according to each new round.
     */
    public void increaseMana() {
        if (increaseMana < LIMIT_MANA) {
            increaseMana++;
        }
        mana += increaseMana;
    }

    /**
     * Subtracts points from the player's mana for using the hero.
     */
    public void useHero() {
        mana -= hero.getMana();
    }

    /**
     * Marks the end of the player's turn.
     */
    public void endTurn() {
        turnEnded = 1;
    }

    /**
     * Reset player's turn.
     */
    public void resetTurn() {
        turnEnded = 0;
    }

    /**
     * Displays the player's hero in JSON format.
     */
    public ObjectNode getHeroCard() {
        ObjectMapper mapper = new ObjectMapper();
        ObjectNode result = mapper.createObjectNode();

        if (this.hero == null) {
            result.put("error", "No hero assigned to the player.");
            return result;
        }

        ArrayNode colors = mapper.createArrayNode();

        result.put("mana", hero.getMana());
        result.put("description", hero.getDescription());
        for (int j = 0; j < hero.getColors().size(); j++) {
            colors.add(hero.getColors().get(j));
        }
        result.set("colors", colors);
        result.put("name", hero.getName());
        result.put("health", hero.getHealth());
        return result;
    }

}
