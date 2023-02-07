package model;

import java.util.ArrayList;

public class Pawn extends Piece {
    private final String name = "P"; // pawns don't have a character for notation, so this is just for consistency

    public Pawn(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares pawn can move to following rules of chess
    @Override
    public ArrayList<Integer> getMoves(GameBoard board) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}