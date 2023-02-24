package model;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class King extends Piece {

    public King(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares king can move to following rules of chess
    public Set<Integer> getMoves(GameBoard board) {
        Set<Integer> result = new HashSet<>(Arrays.asList(position + 1, position - 1, position + 7, position + 8,
                position + 9, position - 7, position - 8, position - 9));
        result.removeIf(i -> i > 63 || i < 0);
        result.removeIf(i -> board.existsPiece(i) && board.getPiece(i).getAllegiance().equals(allegiance));
        return result;
    }

    @Override
    public Set<Integer> getLegalMoves(GameBoard board) {
        Set<Integer> result = super.getLegalMoves(board);

        // can castle if not in check
        if (!board.isCheck()) {
            if (!moved && allegiance.equals("W")) {
                if (board.existsPiece(63) && !board.getPiece(63).isMoved() && result.contains(61)) {
                    result.add(62);
                } else if (board.existsPiece(56) && !board.getPiece(56).isMoved() && result.contains(59)) {
                    result.add(58);
                }
            } else if (!moved && allegiance.equals("B")) {
                if (board.existsPiece(7) && !board.getPiece(7).isMoved() && result.contains(5)) {
                    result.add(6);
                } else if (board.existsPiece(0) && !board.getPiece(0).isMoved() && result.contains(3)) {
                    result.add(2);
                }
            }
        }
        return result;
    }

    public String getName() {
        return "K";
    }
}
