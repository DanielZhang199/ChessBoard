package model;

import java.util.ArrayList;

// Queen piece with a position and side/allegiance
public class Queen implements Piece {
    private String allegiance;  // "B" for black, "W" for white
    private final String name = "Q";
    private int position;

    public Queen(String allegiance, int position) {
        this.allegiance = allegiance;
        this.position = position;
    }


    // EFFECTS: returns list of all squares queen can move to following rules of chess
    @Override
    public ArrayList<Integer> getMoves() {
        return null;
    }

    @Override
    public int getPosition() {
        return 0;
    }

    // REQUIRES: position is a valid place queen can move to
    // MODIFIES: this
    // EFFECTS: changes position of the queen to specified new position
    @Override
    public void setPosition(int position) {

    }

    // this will be for the code, getName will be used for notation
    @Override
    public String getID() {
        return allegiance + name;
    }

    @Override
    public String getName() {
        return name;
    }
}
