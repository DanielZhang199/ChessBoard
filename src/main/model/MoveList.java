package model;

import java.util.ArrayList;


// class for list of moves in computer and human-readable notation
public class MoveList {
    private ArrayList<Move> moves; // list of moves for computer
    private ArrayList<String> humanList; // list of moves for human players to see

    // MODIFIES: this
    // EFFECTS: creates an empty list of moves, and human algebraic translations
    public MoveList() {

    }

    // REQUIRES: at least one move was made
    // EFFECTS: copies and returns a move list
    public ArrayList<String> getMoves() {
        return null;
    }

    // REQUIRES: at least one move was made
    // EFFECTS: copies and returns a human-readable list
    public ArrayList<String> getHumanList() {
        return null;
    }

    // REQUIRES: move is a valid move, and start and end squares are correct
    // MODIFIES: this
    // EFFECTS: adds a non-capture move to both arrays of MoveList (this includes castling); one for humans and one for
    // the code, such that all information of the move is independent of the board state.
    public void addMove(Piece piece, int start, int end, boolean inCheck) {

    }

    // REQUIRES: move is a valid move, and start and end squares are correct, captured piece is being taken off board
    // MODIFIES: this
    // EFFECTS: adds a capture move to both arrays of MoveList (this includes en-passant), this will save the piece
    // object being captured inside ArrayList 'moves'
    public void addMove(Piece piece, int start, int end, boolean inCheck, Piece captured) {

    }

    // REQUIRES: at least one move was made
    // MODIFIES: this
    // EFFECTS: adds "#" symbol to end of previous move on humanList. If white won, calls addResult to make white the
    // victor, else calls addResult to make black the victor
    public void wasCheckmate() {

    }

    // REQUIRES: score is either '1-0', '0-1', or '1/2-1/2'
    // MODIFIES: this
    // EFFECTS: adds score to end of humanList to signify game end
    public void addResult(String score) {

    }

    // REQUIRES: at least one move was made
    // MODIFIES: this
    // EFFECTS: removes the latest move from both arrays, and returns the Move object associated with the removed move
    public Move undo() {
        return null;
    }
}
