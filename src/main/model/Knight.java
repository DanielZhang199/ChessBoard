package model;

import java.util.ArrayList;
import java.util.Set;

public class Knight extends Piece {
    private final String name = "N";

    public Knight(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares knight can move to following rules of chess
    public Set<Integer> getMoves(GameBoard board) {
        return null;
    }

    public String getName() {
        return name;
    }
}