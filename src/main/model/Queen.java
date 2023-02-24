package model;

import java.util.Set;

// Queen piece
public class Queen extends Piece {

    public Queen(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns set of all squares queen can move to
    public Set<Integer> getMoves(GameBoard board) {
        Set<Integer> result = getMovesOrthogonal(board);
        result.addAll(getMovesDiagonal(board));
        return result;
    }

    public String getName() {
        return "Q";
    }
}
