package model;

import java.util.ArrayList;


// class for list of moves in computer and human-readable notation
public class MoveList {
    // INVARIANT: moves.size() == humanList.size()
    private ArrayList<Move> moves; // list of moves for computer
    private ArrayList<String> humanList; // list of moves for human players to see

    // MODIFIES: this
    // EFFECTS: creates an empty list of moves, and human algebraic translations
    public MoveList() {

    }

    // EFFECTS: copies and returns a move list
    public ArrayList<Move> getMoves() {
        return new ArrayList<>(moves);
    }

    // EFFECTS: copies and returns a human-readable list
    public ArrayList<String> getHumanList() {
        return new ArrayList<>(humanList);
    }

    // REQUIRES: move is a valid move, and start and end squares are correct; piece should be copies of piece on board
    // on its original square
    // MODIFIES: this
    // EFFECTS: adds a non-capture move to both arrays of MoveList (this includes castling); one for humans and one for
    // the code, such that all information of the move is independent of the board state.
    public void addMove(Piece piece, int start, int end, boolean inCheck) {

    }

    // REQUIRES: move is a valid move, and start and end squares are correct, captured piece is being taken off board
    // all pieces passed to method should be copies of pieces on board on original positions.
    // MODIFIES: this
    // EFFECTS: adds a capture move to both arrays of MoveList (this includes en-passant), this will save the piece
    // object being captured inside ArrayList 'moves'
    public void addMove(Piece piece, int start, int end, boolean inCheck, Piece captured) {

    }

    // REQUIRES: at least one move was made, and previous move is a check (otherwise it can't be checkmate)
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
    // EFFECTS: removes the latest move from both arrays
    public void undo() {
    }

    // REQUIRES: at least one move was made
    // EFFECTS: returns the last move in array
    public Move getPreviousMove() {
        return null;
    }
}
