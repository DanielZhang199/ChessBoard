package model;

import java.util.ArrayList;
import java.util.Set;

public class Pawn extends Piece {
    private final String name = "P"; // pawns don't have a character for notation, so this is just for consistency

    public Pawn(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares pawn can move to following rules of chess
    public Set<Integer> getMoves(GameBoard board) {
        return null;
    }

    public String getName() {
        return name;
    }
}