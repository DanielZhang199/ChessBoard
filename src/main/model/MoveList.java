package model;

import java.util.ArrayList;
import java.util.Arrays;


// class for list of moves in computer and human-readable notation
public class MoveList {
    // INVARIANT: moves.size() == humanList.size()
    private final ArrayList<Move> moves; // list of moves for computer
    private final ArrayList<String> notationList; // list of moves for human players to see

    // list of chess columns for reference
    private static final ArrayList<String> columns = new ArrayList<>(
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

    // EFFECTS: copies and returns a human-readable list
    public ArrayList<String> getNotationList() {
        return new ArrayList<>(notationList);
    }

    // REQUIRES: move is a valid move, and start and end squares are correct; piece should be copies of piece on board
    // on its original square
    // MODIFIES: this
    // EFFECTS: adds a non-capture move to both arrays of MoveList (this includes castling); one for humans and one for
    // the code, such that all information of the move is independent of the board state.
    public void addMove(Move move) {
        moves.add(move);
        notationList.add(toNotation(move));
    }

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
                piece = columns.get(move.getStart() % 8);
            }
            capture = "x";
        }
        return piece + capture + getCoordinate(move.getEnd()) + check;
    }

    private String getCoordinate(int square) {
        return  columns.get(square % 8) + (8 - square / 8);
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

    // REQUIRES: score is either '1-0', '0-1', or '1/2-1/2'
    // MODIFIES: this
    // EFFECTS: adds score to end of notation list to signify game end
    public void addResult(String score) {
        notationList.add(score);
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
