package model;

import java.util.ArrayList;

// Common supertype for all pieces
public class Piece {
    protected int position;
    protected String allegiance;  // "B" for black, "W" for white

    // EFFECTS: sets the side and position of the piece
    protected Piece(String allegiance, int position) {
        this.allegiance = allegiance;
        this.position = position;
    }

    // REQUIRES: position is a valid square to which the piece can move, and within interval [0, 63]
    // MODIFIES: this
    // EFFECTS: changes position of the piece to specified new position
    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }

    public String getAllegiance() {
        return allegiance;
    }

    // the following getter methods will depend on piece subtype calling, they do not do anything on piece class
    public ArrayList<Integer> getMoves(GameBoard board) {
        return null;
    }

    public String getName() {
        return null;
    }
}
