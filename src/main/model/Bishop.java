package model;

import java.util.Set;

// Bishop piece
public class Bishop extends Piece {
    // while bishops are associated with a colour square, this is a consequence of their movement rather than an
    // intrinsic property of the piece itself

    public Bishop(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares bishop can move to; note that this includes moves that would leave
    // the bishop's king in check
    public Set<Integer> getMoves(GameBoard board) {
        return getMovesDiagonal(board);
    }

    public String getName() {
        return "B";
    }
}