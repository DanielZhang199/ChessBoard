package model;

import java.util.ArrayList;
import java.util.Set;

// Rook piece
public class Rook extends Piece {
    private final String name = "R";

    public Rook(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares rook can move to following rules of chess
    public Set<Integer> getMoves(GameBoard board) {
        return null;
    }

    public String getName() {
        return name;
    }
}