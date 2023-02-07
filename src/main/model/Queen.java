package model;

import java.util.Set;

// Queen piece
public class Queen extends Piece {

    public Queen(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns set of all squares queen can move to following rules of chess
    public Set<Integer> getMoves(GameBoard board) {
        return null;
    }

    public String getName() {
        return "Q";
    }
}
