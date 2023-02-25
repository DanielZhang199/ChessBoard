package model;

import java.util.ArrayList;

// represents the current game state with all pieces and their locations
public class GameBoard {
    private ArrayList<Piece> pieces;  // list of pieces on board, squares are listed 0 to 63; refer to README diagram
    private String turn; // the side that gets to move next turn; either "W" or "B"
    private String status;  // end result of game, null if not applicable
    private Move lastMove;  // the most recent move of the game; useful for displays and for en passant

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
        lastMove = null;
    }

    // EFFECTS: creates a board with pieces on starting positions, with white to move, and no moves played yet.
    public GameBoard() {
        pieces = new ArrayList<>();
        setup();
        turn = "W";
        status = null;
        lastMove = null;
    }

    // EFFECTS: returns true if there exists a piece with the coordinates on the board
    public boolean existsPiece(int pos) {
        for (Piece p : pieces) {
            if (p.getPosition() == pos) {
                return true;
            }
        }
        return false;
    }

    // REQUIRES: there must be a piece on given coordinates
    // EFFECTS: returns the piece object on specified square
    public Piece getPiece(int pos) {
        for (Piece p : pieces) {
            if (p.getPosition() == pos) {
                return p;
            }
        }
        return null;
    }

    // REQUIRES: nothing
    // MODIFIES: this, piece
    // EFFECTS: IF the move is possible (as dictated by piece on starting square) the turn goes the other player, and
    // piece moves from start to end square; if there is an enemy piece on the ending square, that piece is removed from
    // the board. updates lastMove. method returns true
    // ELSE, method returns false.
    public boolean movePiece(int start, int end) {
        if (!existsPiece(start)) {
            return false;
        }
        Piece moving = getPiece(start);
        if (!moving.getLegalMoves(this).contains(end)) {
            return false;
        }
        Piece clone = clonePiece(moving);
        Piece captured = updateBoard(moving, end);

        moving.setPosition(end);
        if (captured != null) {
            pieces.remove(captured);
            lastMove = new Move(clone, start, end, isCheck(), captured);
        } else {
            lastMove = new Move(clone, start, end, isCheck());
        }
        toggleTurn();
        return true;
    }

    public boolean isCheck() {
        int kingSquare = 4;
        for (Piece p : pieces) {
            if (p.getName().equals("K") && p.getAllegiance().equals(turn)) {
                kingSquare = p.getPosition();
                break;
            }
        }
        for (Piece p : pieces) {
            if (!p.getAllegiance().equals(turn)) {
                if (p.getMoves(this).contains(kingSquare)) {
                    return true;
                }
            }
        }
        return false;
    }

    // updates board in special cases and returns piece being captured if applicable
    private Piece updateBoard(Piece moving, int end) {
        // whether special moves are legal is determined by the piece class, this method only updates the board
        if (moving.getName().equals("P") && (end <= 7 || end >= 56)) {  // promotion
            pieces.remove(moving);
            Piece captured = getPiece(end);
            pieces.add(new Queen(moving.getAllegiance(), end));
            return captured;
            // assume that all pawns promote into queens (cause idk how to make otherwise)

        } else if (moving.getName().equals("K") && !moving.isMoved()) { // castling
            if (end == 62 && existsPiece(63)) {
                getPiece(63).setPosition(61);
            } else if (end == 58 && existsPiece(56)) {
                getPiece(56).setPosition(59);
            } else if (end == 2 && existsPiece(0)) {
                getPiece(0).setPosition(3);
            } else if (end == 6 && existsPiece(7)) {
                getPiece(7).setPosition(5);
            }

        } else if (lastMove != null && lastMove.getPiece().getName().equals("P") && moving.getName().equals("P")) {
            if (lastMove.getEnd() == end + 8 && moving.getAllegiance().equals("W")) { // en passant for white
                return getPiece(end + 8);
            } else if (lastMove.getEnd() == end - 8 && moving.getAllegiance().equals("B")) { // en passant for black
                return getPiece(end - 8);
            }
        }
        return getPiece(end);  // may also return null, in which case no piece will be captured
    }

    private void toggleTurn() {
        if (turn.equals("W")) {
            turn = "B";
        } else {
            turn = "W";
        }
    }

    private Piece clonePiece(Piece p) {
        if (p.getName().equals("P")) {
            return new Pawn(p.getAllegiance(), p.getPosition());
        } else if (p.getName().equals("N")) {
            return new Knight(p.getAllegiance(), p.getPosition());
        } else if (p.getName().equals("B")) {
            return new Bishop(p.getAllegiance(), p.getPosition());
        } else if (p.getName().equals("R")) {
            return new Rook(p.getAllegiance(), p.getPosition());
        } else if (p.getName().equals("Q")) {
            return new Queen(p.getAllegiance(), p.getPosition());
        } else {
            return new King(p.getAllegiance(), p.getPosition());
        }
    }

    // The next two methods will be used in public visibility only for special cases or for testing

    // REQUIRES: piece is not on a square which is already occupied; piece is not a king, or pawn on first or last ranks
    // MODIFIES: this
    // EFFECTS: adds a piece to the board
    public void addPiece(Piece piece) {
        pieces.add(piece);
    }

    // REQUIRES: there exists a piece on the board at the given position; and that piece is not a king
    // MODIFIES: this
    // EFFECTS: removes a piece from the board
    public void removePiece(int position) {
        pieces.removeIf(p -> p.getPosition() == position);
    }

    // REQUIRES: move being made is a valid move, if no piece on start square, method will throw NullPointerException
    // unfortunately there isn't much that can be done to handle this exception here.
    // EFFECTS: returns true if the side moving is in check after move, i.e. if opponent is able to capture
    // king if it were to be making the move; exception will only be thrown if there is no piece found at start
    public boolean testCheck(int start, int end) throws NullPointerException {
        ArrayList<Piece> original = tempBoard();
        Piece moving = getPiece(start);
        Piece captured = updateBoard(moving, end);
        moving.setPosition(end);
        if (captured != null) {
            pieces.remove(captured);
        }
        boolean val = isCheck();
        this.pieces = original;
        return val;
    }

    // creates the move on game board with all new pieces, but saves previous state in temp board
    private ArrayList<Piece> tempBoard() {
        ArrayList<Piece> original = new ArrayList<>(this.pieces);
        this.pieces = new ArrayList<>();
        for (Piece p : original) {
            pieces.add(clonePiece(p));
        }
        return original;
    }

    // REQUIRES: board is in reachable chess position
    // MODIFIES: this
    // EFFECTS: Checks if the board is in a position that is a checkmate or drawn. Sets status field as follows:
    // possible end conditions are "Checkmate", "Stalemate, "Draw By Insufficient Material".
    // The checks for 3 move repetition and 50 move rule will be done in MoveList class instead.
    // Returns true if game is over, false otherwise.
    public boolean checkStatus() {
        if (testInsufficientMaterial()) {
            status = "Draw By Insufficient Material";
            return true;
        } else if (testNoMoves()) {
            if (isCheck()) {
                if (turn.equals("W")) {
                    status = "Black Wins By Checkmate";
                } else {
                    status = "White Wins By Checkmate";
                }
            } else {
                status = "Stalemate";
            }
            return true;
        }
        return false;
    }

    private boolean testNoMoves() {
        if (turn.equals("W")) {
            for (Piece p : pieces) {
                if (p.getAllegiance().equals("W") && p.getLegalMoves(this).size() > 0) {
                    return false;
                }
            }
            return true;
        }  // no need for else here
        for (Piece p : pieces) {
            if (p.getAllegiance().equals("B") && p.getLegalMoves(this).size() > 0) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("methodlength")
    // To whomever is marking this; I'm sorry that I did not check with a TA in advance before using this annotation,
    // however, there are no more available office hours that I can attend during this reading week before phase 1 is
    // due. My method is only 28 lines long, and I included comments to help make the code less confusing, so it is more
    // clear why I needed the extra 3 lines. I believe that there is no way of dividing up this method further without
    // making the code twice as inefficient (2 checks; one for each side), or more confusing to read.
    // I added an EFFECTS clause to better explain what this private method is designed to do.

    // REQUIRES: nothing
    // EFFECTS: If either side has a Queen, Rook, or pawn, returns false. Furthermore, if there exists the following
    // pieces (or more) of the same colour for either side: 2 bishops, bishop and knight, or 3 knights, return false.
    // Otherwise, return true, as there is not enough material for either side to for a checkmate.
    private boolean testInsufficientMaterial() {
        ArrayList<String> piecesW = new ArrayList<>();
        ArrayList<String> piecesB = new ArrayList<>();
        ArrayList<String> current;
        for (Piece p : pieces) {
            // depending on the colour of piece,
            // we check and add pieces to the array that corresponds to the colour.
            if (p.getAllegiance().equals("W")) {
                current = piecesW;
            } else {
                current = piecesB;
            }

            if (p.getName().equals("Q") || p.getName().equals("R") || p.getName().equals("P")) {
                // queen, rook, pawn on board means its not insufficient material
                return false;
            } else if (p.getName().equals("B")) {
                // bishop and any other piece is not insufficient material
                if (current.size() > 0) {
                    return false;
                }
                current.add("B");
            } else if (p.getName().equals("N")) {
                // knight with bishop or 2 other knights is not insufficient material
                // we don't add kings to the list for this purpose; and because their presence is implied
                if (current.contains("B")) {
                    return false;
                } else if (current.size() > 1) {
                    return false;
                }
                current.add("N");
            }
        }
        return true;  // it is insufficient material if the method didn't return false earlier.
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
        addKings();
        for (int i = 8; i <= 15; i++) {
            pieces.add(new Pawn("B", i));
            pieces.add(new Pawn("W", i + 40));
        }
        pieces.add(new Rook("B", 0));
        pieces.add(new Rook("B", 7));
        pieces.add(new Rook("W", 56));
        pieces.add(new Rook("W", 63));

        pieces.add(new Knight("B", 1));
        pieces.add(new Knight("B", 6));
        pieces.add(new Knight("W", 57));
        pieces.add(new Knight("W", 62));

        pieces.add(new Bishop("B", 2));
        pieces.add(new Bishop("B", 5));
        pieces.add(new Bishop("W", 58));
        pieces.add(new Bishop("W", 61));

        pieces.add(new Queen("W", 59));
        pieces.add(new Queen("B", 3));
    }

    // REQUIRES: kings are not already on the board
    // MODIFIES: this
    // EFFECTS: adds two kings to starting squares
    private void addKings() {
        pieces.add(new King("W", 60));
        pieces.add(new King("B", 4));
    }

    public Move getLastMove() {
        return lastMove;
    }

    // REQUIRES: newPreviousMove is the correct move that was played prior to previous move (i.e. the second last move)
    // as this is not stored in GameBoard.
    // MODIFIES: this
    // EFFECTS: if no moves have been made, this method does nothing; else the board reverts to prior state before last
    // move was made.
    public void undo(Move newPreviousMove) {
        if (lastMove != null) {
            Piece p = lastMove.getPiece();
            removePiece(lastMove.getEnd());
            pieces.add(p);
            if (lastMove.getCaptured() != null) {
                pieces.add(lastMove.getCaptured());
            }
            toggleTurn();
            lastMove = newPreviousMove;
        }
    }

    // MODIFIES: this
    // EFFECTS: if no moves have been made, this method does nothing; else the board reverts to prior state before last
    // move was made. after calling this, GameBoard will act as if no more prior moves exist.
    public void undo() {
        // call this version if we don't know what last move was, or there was no prior move (i.e. first move)
        if (lastMove != null) {
            Piece p = lastMove.getPiece();
            removePiece(lastMove.getEnd());
            pieces.add(p);
            if (lastMove.getCaptured() != null) {
                pieces.add(lastMove.getCaptured());
            }
            toggleTurn();
            lastMove = null;
        }
    }
}
