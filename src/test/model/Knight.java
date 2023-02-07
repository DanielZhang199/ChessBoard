package model;

import java.util.ArrayList;

public class Knight extends Piece {
    private final String name = "N";

    public Knight(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares knight can move to following rules of chess
    @Override
    public ArrayList<Integer> getMoves(GameBoard board) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}