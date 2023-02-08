package model;

import java.util.ArrayList;

// represents the current game state with all pieces and their locations
public class GameBoard {
    private ArrayList<Piece> pieces;  // list of pieces on board, squares are listed 0 to 63; refer to README diagram
    private String turn; // the side that gets to move next turn; either "W" or "B"
    private String status;  // end result of game, null if not applicable

    // MODIFIES: this
    // EFFECTS: creates a board with pieces on starting positions, with white to move, and no moves played yet.
    // if empty is set to true, only kings will be put on the board when initialized
    public GameBoard(boolean empty) {
        pieces = new ArrayList<>();
        if (!empty) {
            setup();
        } else {
            addKings();
        }
        turn = "W";
        status = null;
    }

    // EFFECTS: returns true if there exists a piece with the coordinates on the board
    public boolean existsPiece(int pos) {
        return false;
    }

    // REQUIRES: there must be a piece on given coordinates
    // EFFECTS: returns the piece object on specified square
    public Piece getPiece(int pos) {
        return null;
    }

    // REQUIRES: piece exists on start square, and can move to end square when following rules of chess
    // MODIFIES: this
    // EFFECTS: turn goes the other player, and piece moves from start to end square; if there is an enemy piece on the
    // ending square, that piece is removed from the board.
    public void movePiece(int start, int end) {

    }

    // The next two methods will be used in special cases or for testing

    // REQUIRES: piece is not on a square which is already occupied; piece is not a king, or pawn on first or last ranks
    // MODIFIES: this
    // EFFECTS: adds a piece to the board
    public void addPiece(Piece piece) {

    }

    // REQUIRES: there exists a piece on the board at the given position; and that piece is not a king
    // MODIFIES: this
    // EFFECTS: removes a piece from the board
    public void removePiece(int position) {

    }

    // REQUIRES: move being made is a valid move
    // EFFECTS: returns true if the side moving is in check after move, i.e. if opponent is able to capture
    // king if it were to be making the move
    public boolean testCheck(int start, int end) {
        return false;
    }

    // REQUIRES: board is in reachable chess position
    // MODIFIES: this
    // EFFECTS: Checks if the board is in a position that is a checkmate or drawn. Sets status field as follows:
    // possible end conditions are "checkmate", "stalemate, "ins". The checks for 3 move repetition and 50 move rule
    // will be done in MoveList class instead. Returns true if game is over.
    public boolean checkStatus() {
        return false;
    }

    // REQUIRES: Game is over, and checkStatus was called
    // EFFECTS: returns a string that represents which side won, or if it was a tie
    public String getStatus() {
        return status;
    }

    public int getNumPieces() {
        return pieces.size();
    }

    public String getTurn() {
        return turn;
    }

    // REQUIRES: board is not already set up
    // MODIFIES: this
    // EFFECTS: sets each piece on the board to starting position
    private void setup() {

    }

    // REQUIRES: kings are not already on the board
    // MODIFIES: this
    // EFFECTS: adds two kings to starting squares
    private void addKings() {
        pieces.add(new King("W", 60));
        pieces.add(new King("B", 4));
    }
}
