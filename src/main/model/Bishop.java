package model;

import java.util.ArrayList;

// Bishop piece
public class Bishop extends Piece {
    // while bishops are associated with a colour square, this is a consequence of their movement rather than an
    // intrinsic property of the piece itself
    private final String name = "B";

    public Bishop(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares bishop can move to following rules of chess
    @Override
    public ArrayList<Integer> getMoves(GameBoard board) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}