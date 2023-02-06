package model;

import java.util.ArrayList;

// represents the current board displayed to the user
public class GameBoard {
    private ArrayList<Piece> pieces;  // list of pieces on board, squares are listed 0 to 63
    private String turn; // the side that gets to move next turn
    private ArrayList<String> moves; // list of previous moves (i.e. "Ra1d1", "Nb8c6"); format is used for simplicity

    // MODIFIES: this
    // EFFECTS: creates a board with pieces on starting positions, with white to move, and no moves played yet.
    public GameBoard() {
        pieces = new ArrayList<Piece>();
        setup();
        turn = "white";
        moves = new ArrayList<String>();
    }

    // EFFECTS: returns true if there exists a piece with the coordinates on the board
    public boolean existsPiece(int coords) {
        return false;
    }

    // REQUIRES: there must be a piece on given coordinates
    // EFFECTS: returns the piece object on specified square
    public Piece getPiece(int coords) {
        return null;
    }

    // REQUIRES: piece exists on start square, and can move to end square when following rules of chess
    // MODIFIES: this
    // EFFECTS: turn goes the other player, and piece moves from start to end square; if there is an enemy piece on the
    // ending square, that piece is removed from the board.
    public void movePiece(int start, int end) {

    }

    // REQUIRES: side is either "white" or "black"
    // EFFECTS: returns true if the side passed as parameter is currently in check, i.e. if opponent is able to capture
    // king on the next move.
    public boolean inCheck(String side) {
        return false;
    }

    public ArrayList<Piece> getPieces() {
        return pieces;
    }

    public ArrayList<String> getMoves() {
        return moves;
    }

    public String getTurn() {
        return turn;
    }

    // MODIFIES: this
    // EFFECTS: sets each piece on the board to starting position
    private void setup() {

    }
}
