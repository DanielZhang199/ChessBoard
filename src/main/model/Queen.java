package model;

import java.util.ArrayList;

// Queen piece
public class Queen extends Piece {
    private final String name = "Q";

    public Queen(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares queen can move to following rules of chess
    @Override
    public ArrayList<Integer> getMoves(GameBoard board) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
