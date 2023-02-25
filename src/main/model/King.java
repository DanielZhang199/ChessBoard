package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class King extends Piece {

    public King(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns set of all squares king can move to, excluding castling
    // as you can not castle to capture a piece, or capture the enemy king (obviously)
    public Set<Integer> getMoves(GameBoard board) {
        Set<Integer> result = new HashSet<>(Arrays.asList(position + 1, position - 1, position + 7, position + 8,
                position + 9, position - 7, position - 8, position - 9));
        result.removeIf(i -> i > 63 || i < 0);
        result.removeIf(i -> board.existsPiece(i) && board.getPiece(i).getAllegiance().equals(allegiance));
        return result;
    }

    // EFFECTS: returns set of all squares king can move to without being in check, including castling if possible.
    @Override
    public Set<Integer> getLegalMoves(GameBoard board) {
        Set<Integer> result = super.getLegalMoves(board);

        // testing for check is much more intensive than testing for previous movement, so we check !moved first
        if (!moved && !board.isCheck()) {
            if (allegiance.equals("W")) {
                if (board.existsPiece(63) && !board.getPiece(63).isMoved() && result.contains(61)) {
                    result.add(62);
                } else if (board.existsPiece(56) && !board.getPiece(56).isMoved() && result.contains(59)) {
                    result.add(58);
                }
            } else if (allegiance.equals("B")) {
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
