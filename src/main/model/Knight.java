package model;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

public class Knight extends Piece {

    public Knight(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares knight can move to; knights cannot be blocked, except on end square
    public Set<Integer> getMoves(GameBoard board) {
        Set<Integer> result = new HashSet<>(Arrays.asList(position - 17, position - 15, position - 10, position - 6,
                position + 6, position + 10, position + 15, position + 17));
        filterMoves(result);
        result.removeIf(i -> i > 63 || i < 0);
        result.removeIf(i -> board.existsPiece(i) && board.getPiece(i).getAllegiance().equals(allegiance));
        return result;
    }

    private void filterMoves(Set<Integer> result) {
        if (position % 8 <= 1) {
            result.removeAll(Arrays.asList(position + 6, position - 10));
            if (position % 8 == 0) {
                result.removeAll(Arrays.asList(position + 15, position - 17));
            }
        } else if (position % 8 >= 6) {
            result.removeAll(Arrays.asList(position + 10, position - 6));
            if (position % 8 == 7) {
                result.removeAll(Arrays.asList(position + 17, position - 15));
            }
        }
    }

    public String getName() {
        return "N";
    }
}