package model;

import java.util.Set;

// Rook piece
public class Rook extends Piece {

    public Rook(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares rook can move threaten
    public Set<Integer> getMoves(GameBoard board) {
        return super.getMovesOrthogonal(board);
    }

    public String getName() {
        return "R";
    }
}