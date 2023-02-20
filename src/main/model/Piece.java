package model;

import java.util.ArrayList;
import java.util.Set;

// Common supertype for all pieces
public abstract class Piece {
    protected int position;
    protected String allegiance;  // "B" for black, "W" for white
    protected boolean moved;  // whether the piece has moved

    // EFFECTS: sets the side and position of the piece
    protected Piece(String allegiance, int position) {
        this.allegiance = allegiance;
        this.position = position;
        this.moved = false;
    }

    // REQUIRES: position is a valid square to which the piece can move, and within interval [0, 63]
    // MODIFIES: this
    // EFFECTS: changes position of the piece to specified new position
    public void setPosition(int position) {
        this.position = position;
        if (!moved) {
            this.moved = true;
        }
    }

    public int getPosition() {
        return position;
    }

    public String getAllegiance() {
        return allegiance;
    }

    public boolean isMoved() {
        return moved;
    }

    abstract String getName();

    abstract Set<Integer> getMoves(GameBoard b);
}
