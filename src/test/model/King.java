package model;

import java.util.ArrayList;

public class King extends Piece {
    private final String name = "K";

    public King(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares king can move to following rules of chess
    @Override
    public ArrayList<Integer> getMoves(GameBoard board) {
        return null;
    }

    @Override
    public String getName() {
        return name;
    }
}
