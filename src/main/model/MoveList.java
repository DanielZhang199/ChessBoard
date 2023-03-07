package model;

import model.exceptions.NotValidSquareException;

import java.util.ArrayList;
import java.util.Arrays;

// class for list of moves in computer and human-readable notation
// therefore can also translate between board coordinates and algebraic notation
public class MoveList {
    // INVARIANT: both arrays have same .size()
    private final ArrayList<Move> moves; // list of moves for computer
    private final ArrayList<String> notationList; // list of moves for human players to see

    // list of chess columns (i.e. files) for reference
    private static final ArrayList<String> files = new ArrayList<>(
            Arrays.asList("a", "b", "c", "d", "e", "f", "g", "h"));

    // MODIFIES: this
    // EFFECTS: creates an empty list of moves, and human algebraic translations
    public MoveList() {
        moves = new ArrayList<>();
        notationList = new ArrayList<>();
    }

    // EFFECTS: copies and returns a move list
    public ArrayList<Move> getMoves() {
        return new ArrayList<>(moves);
    }

    // EFFECTS: returns the size of arrays
    public int getSize() {
        return notationList.size();
    }

    // EFFECTS: copies and returns a human-readable list
    public ArrayList<String> getNotationList() {
        return new ArrayList<>(notationList);
    }

    // REQUIRES: move is instantiated with correct information, and start and end squares are correct; piece objects
    // should be copies of the pieces on board on its original square, prior to moving
    // MODIFIES: this
    // EFFECTS: adds a move to both arrays of MoveList (this includes castling) in appropriate formats
    public void addMove(Move move) {
        moves.add(move);
        notationList.add(toNotation(move));
    }

    // EFFECTS: converts Move to algebraic notation and returns it as String, keep in mind 2 things:
    // - algorithm isn't perfect: it does not specify which piece moved if multiple pieces of the same name can move
    // to the same square; this is simply not possible to do without knowing what the board is like.
    // - this cannot be reversed without knowledge of the board state, which we don't have.
    private String toNotation(Move move) {
        if (move.getPiece().getName().equals("K")) {
            if ((move.getStart() == 60 && move.getEnd() == 62) || (move.getStart() == 4 && move.getEnd() == 6)) {
                return "O-O";
            } else if ((move.getStart() == 60 && move.getEnd() == 58) || (move.getStart() == 4 && move.getEnd() == 2)) {
                return "O-O-O";
            }
        }
        String piece = move.getPiece().getName();
        if (piece.equals("P")) {
            piece = "";
        }
        String capture = "";
        String check = "";
        if (move.isCheck()) {
            check = "+";
        }
        if (move.getCaptured() != null) {
            if (piece.isEmpty()) {
                piece = files.get(move.getStart() % 8);
            }
            capture = "x";
        }
        return piece + capture + fromCoordinate(move.getEnd()) + check;
    }

    // REQUIRES: 0 <= square < 64
    // EFFECTS: converts a coordinate into algebraic notation
    public static String fromCoordinate(int square) {
        return  files.get(square % 8) + (8 - square / 8);
    }

    // EFFECTS: converts an algebraic notation square to a coordinate (i.e. e4 -> 36)
    public static int toCoordinate(String square) throws NotValidSquareException {
        if (square.length() != 2) {
            throw new NotValidSquareException();
        }
        if (!(files.contains(square.substring(0, 1)) && Character.isDigit(square.charAt(1)))) {
            throw new NotValidSquareException();
        }
        int file = files.indexOf(square.substring(0, 1));
        int rank = square.charAt(1) - '0';

        if (rank > 8 || rank < 1) {
            throw new NotValidSquareException();
        }

        return file + 8 * (8 - rank);
    }


    // REQUIRES: at least one move was made, and previous move is a check (otherwise it can't be checkmate)
    // MODIFIES: this
    // EFFECTS: adds "#" symbol to end of previous move of notation. If white won, calls addResult to make white the
    // victor, else calls addResult to make black the victor
    public void wasCheckmate() {
        String move = notationList.get(notationList.size() - 1);
        move = move.substring(0, move.length() - 1) + "#";  // this replaces the "+" with "#"
        notationList.remove(notationList.size() - 1);
        notationList.add(move);
    }

    // REQUIRES: at least one move was made
    // MODIFIES: this
    // EFFECTS: removes the latest move from both arrays
    public void undo() {
        moves.remove(moves.size() - 1);
        notationList.remove(notationList.size() - 1);
    }

    // REQUIRES: at least one move was made
    // EFFECTS: returns the last move in array
    public Move getPreviousMove() {
        return moves.get(moves.size() - 1);
    }
}
