package model;

import java.util.HashSet;
import java.util.Set;

public class Pawn extends Piece {

    public Pawn(String allegiance, int position) {
        super(allegiance, position);
    }

    // EFFECTS: returns list of all squares pawn can move to
    // note that white and black pawns move in different directions
    public Set<Integer> getMoves(GameBoard board) {
        if (allegiance.equals("W")) {
            return getMovesWhite(board);
        } else {
            return getMovesBlack(board);
        }
    }

    private Set<Integer> getMovesWhite(GameBoard board) {
        Set<Integer> result = new HashSet<>();
        if (position % 8 != 7 && board.existsPiece(position - 7)
                && board.getPiece(position - 7).getAllegiance().equals("B")) {
            result.add(position - 7);
        }
        if (position % 8 != 0 && board.existsPiece(position - 9)
                && board.getPiece(position - 9).getAllegiance().equals("B")) {
            result.add(position - 9);
        }
        if (!board.existsPiece(position - 8)) {
            result.add(position - 8);
            if (!moved && !board.existsPiece(position - 16)) {
                result.add(position - 16);
            }
        }
        if (position >= 24 && position <= 31) {
            addEnPassantW(board, result);
        }
        return result;
    }

    private void addEnPassantW(GameBoard board, Set<Integer> result) {
        Move m = board.getLastMove();
        if (m != null && m.getPiece().getName().equals("P")) {
            if (m.getStart() == position - 17 && m.getEnd() == position - 1) {
                result.add(position - 9);
            } else if (m.getStart() == position - 15 && m.getEnd() == position + 1) {
                result.add(position - 7);
            }
        }
    }

    private Set<Integer> getMovesBlack(GameBoard board) {
        Set<Integer> result = new HashSet<>();
        if (board.existsPiece(position + 7) && board.getPiece(position + 7).getAllegiance().equals("W")) {
            result.add(position + 7);
        }
        if (board.existsPiece(position + 9) && board.getPiece(position + 9).getAllegiance().equals("W")) {
            result.add(position + 9);
        }
        if (!board.existsPiece(position + 8)) {
            result.add(position + 8);
            if (!moved && !board.existsPiece(position + 16)) {
                result.add(position + 16);
            }
        }
        if (position >= 32 && position <= 39) {
            addEnPassantB(board, result);
        }
        return result;
    }

    private void addEnPassantB(GameBoard board, Set<Integer> result) {
        Move m = board.getLastMove();
        if (m != null && m.getPiece().getName().equals("P")) {
            if (m.getStart() == position + 17 && m.getEnd() == position + 1) {
                result.add(position + 9);
            } else if (m.getStart() == position + 15 && m.getEnd() == position - 1) {
                result.add(position + 7);
            }
        }
    }

    public String getName() {
        return "P";
    }
}