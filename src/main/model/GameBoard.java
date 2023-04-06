package model;

import model.exceptions.PromotionException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

// represents the current game state with all pieces and their locations
public class GameBoard {
    private Map<Integer, Piece> pieces;  // list of pieces on board, key is the position of piece
    private String turn; // the side that gets to move next turn; either "W" or "B"
    private String status;  // end result of game, null if not applicable
    private Move lastMove;  // the most recent move of the game; useful for displays and for en passant
    private final EventLog log = EventLog.getInstance();

    // MODIFIES: this
    // EFFECTS: creates a board with pieces on starting positions, with white to move, and no moves played yet.
    // if empty is set to true, only kings will be put on the board when initialized
    public GameBoard(boolean empty) {
        pieces = new HashMap<>();
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
        pieces = new HashMap<>();
        setup();
        turn = "W";
        status = null;
        lastMove = null;
    }

    // EFFECTS: returns true if there exists a piece with coordinates equal to pos
    public boolean existsPiece(int pos) {
        return pieces.get(pos) != null;
    }

    // EFFECTS: returns the piece object on specified square, or null if piece does not exist
    public Piece getPiece(int pos) {
        return pieces.get(pos);
    }

    // REQUIRES: nothing
    // MODIFIES: this, piece
    // EFFECTS: IF the move is possible (as dictated by piece on starting square) the turn goes the other player, and
    // piece moves from start to end square; if there is an enemy piece on the ending square, that piece is removed from
    // the board. updates lastMove. method returns true
    // ELSE, method returns false.
    public boolean movePiece(int start, int end) {
        Piece moving = getPiece(start);
        if (moving == null || !moving.getLegalMoves(this).contains(end)) {
            return false;
        }
        Piece clone = clonePiece(moving);
        toggleTurn();
        try {
            Piece captured = updateBoard(moving, end);
            if (captured != null) {
                pieces.remove(captured.getPosition());
            }
            pieces.remove(start);
            moving.setPosition(end);
            addPiece(moving);
            lastMove = new Move(clone, start, end, isCheck(), captured);
        } catch (PromotionException e) {
            pieces.remove(moving.getPosition());
            Piece captured = e.getCaptured();
            lastMove = new Move(clone, start, end, isCheck(), captured);
        }
        log.logEvent(new Event("Made move: " + MoveList.toNotation(lastMove)));
        return true;
    }

    // REQUIRES: p is in this.pieces, 0 <= end <= 63
    // MODIFIES: this, p
    // EFFECTS: changes position of the piece and also the key of the piece in the board.
    // call this method when trying to move pieces around in tests
    protected void changePos(Piece p, int end) {
        pieces.remove(p.getPosition());
        p.setPosition(end);
        pieces.put(end, p);
    }

    // REQUIRES: board must always have a king; but this should always be the case.
    // EFFECTS: returns true if the side moving is currently in check; otherwise returns false
    public boolean isCheck() {
        int kingSquare = 63;
        // set a default square so game doesn't crash when there is no king, although this should never happen
        for (Piece p : pieces.values()) {
            if (p.getName().equals("K") && p.getAllegiance().equals(turn)) {
                kingSquare = p.getPosition();
                break;
            }
        }
        for (Piece p : pieces.values()) {
            if (!p.getAllegiance().equals(turn)) {
                if (p.getMoves(this).contains(kingSquare)) {
                    return true;
                }
            }
        }
        return false;
    }

    // REQUIRES: board must have been initialized to start from a normal starting position
    // MODIFIES: this
    // EFFECTS: updates board in special cases and returns piece being captured if applicable. Basically moves pieces
    // in cases outside normal movement or captures. The piece being captured needs to be found in this method due to
    // en passant tests being here.
    private Piece updateBoard(Piece moving, int end) throws PromotionException {
        // whether special moves are legal is determined by the piece class, this method only updates the board
        if (moving.getName().equals("P") && (end <= 7 || end >= 56)) {  // promotion
            Piece captured = getPiece(end);
            removePiece(end);
            addPiece(new Queen(moving.getAllegiance(), end));
            throw new PromotionException(captured);
            // assume that all pawns promote into queens
        } else if (moving.getName().equals("K") && !moving.isMoved()) { // castling
            if (end == 62) {
                // we trust that there is already a rook on 63, since otherwise this would not be legal end move
                changePos(getPiece(63), 61);
            } else if (end == 58) {
                changePos(getPiece(56), 59);
            } else if (end == 2) {
                changePos(getPiece(0), 3);
            } else if (end == 6) {
                changePos(getPiece(7), 5);
            }
        } else if (lastMove != null && lastMove.getPiece().getName().equals("P") && moving.getName().equals("P")) {
            if (lastMove.getEnd() == end + 8 && moving.getAllegiance().equals("W")) {
                // en passant for white
                return getPiece(end + 8);
            } else if (lastMove.getEnd() == end - 8  && moving.getAllegiance().equals("B")) { // en passant for black
                return getPiece(end - 8);
            }
        }
        return getPiece(end);  // may also return null, in which case no piece will be captured
    }

    // MODIFIES: this
    // EFFECTS: switches from one turn to another
    private void toggleTurn() {
        if (turn.equals("W")) {
            turn = "B";
        } else {
            turn = "W";
        }
    }

    // EFFECTS: Creates a perfect copy of the piece being input and returns it, this method is mostly used for clarity
    private Piece clonePiece(Piece p) {
        return Piece.createPiece(p.getAllegiance(), p.getPosition(), p.isMoved(), p.getName());
    }

    // The next two methods will be used in public visibility only for testing

    // REQUIRES: piece is not on a square which is already occupied; piece is not a king, or pawn on first or last ranks
    // MODIFIES: this
    // EFFECTS: adds a piece to the board
    public void addPiece(Piece piece) {
        pieces.put(piece.getPosition(), piece);
    }

    // REQUIRES: the piece at the coordinates is not a king
    // MODIFIES: this
    // EFFECTS: removes a piece from the board; if piece is king, method will work, but chess can't be played without
    // a king.
    public void removePiece(int position) {
        pieces.remove(position);
    }

    // REQUIRES: there must be a piece at start coordinates, and piece.getMoves().contains(end) value.
    // EFFECTS: returns true if the side moving is in check after move, i.e. if opponent is able to capture
    // king if it were to be making the move; exception will be thrown if there is no piece found at start
    public boolean testCheck(int start, int end) {
        HashMap<Integer, Piece> original = tempBoard();
        Piece moving = getPiece(start);
        try {
            Piece captured = updateBoard(moving, end);
            if (captured != null) {
                pieces.remove(captured.getPosition());
            }
        } catch (PromotionException e) {
            pieces.remove(moving.getPosition());
        }
        changePos(moving, end);
        boolean val = isCheck();
        this.pieces = original;
        return val;
    }

    // MODIFIES: this
    // EFFECTS: creates the move on game board with cloned pieces, but saves previous state in return value
    // i.e. this method replaces board with a deep clone of the board
    private HashMap<Integer, Piece> tempBoard() {
        HashMap<Integer, Piece> original = new HashMap<>(this.pieces);
        this.pieces = new HashMap<>();
        for (int i : original.keySet()) {
            addPiece(clonePiece(original.get(i)));
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
            log.logEvent(new Event("Game Over: " + status));
            return true;
        } else if (testNoMoves()) {
            if (isCheck()) {
                if (turn.equals("W")) {
                    status = "Black Wins By Checkmate";
                    log.logEvent(new Event("Game Over: " + status));
                } else {
                    status = "White Wins By Checkmate";
                    log.logEvent(new Event("Game Over: " + status));
                }
            } else {
                status = "Stalemate";
                log.logEvent(new Event("Game Over: " + status));
            }
            return true;
        }
        return false;
    }

    // EFFECTS: returns true when the moving side has no possible moves to make
    private boolean testNoMoves() {
        if (turn.equals("W")) {
            for (Piece p : pieces.values()) {
                if (p.getAllegiance().equals("W") && p.getLegalMoves(this).size() > 0) {
                    return false;
                }
            }
            return true;
        }  // no need for else here
        for (Piece p : pieces.values()) {
            if (p.getAllegiance().equals("B") && p.getLegalMoves(this).size() > 0) {
                return false;
            }
        }
        return true;
    }

    @SuppressWarnings("methodlength")
    // My method is only 28 lines long, and I included comments to help make the code less confusing, so it is more
    // clear why I needed the extra 3 lines. I believe that there is no way of dividing up this method further without
    // making the code twice as inefficient (such has having 2 methods, one for each side), or more confusing to read.
    // I added an EFFECTS clause to better explain what this private method is designed to do.

    // REQUIRES: nothing
    // EFFECTS: If either side has a Queen, Rook, or pawn, returns false. Furthermore, if there exists the following
    // pieces (or more) of the same colour for either side: 2 bishops, bishop and knight, or 3 knights, return false.
    // Otherwise, return true, as there is not enough material for either side to for a checkmate.
    private boolean testInsufficientMaterial() {
        ArrayList<String> piecesW = new ArrayList<>();
        ArrayList<String> piecesB = new ArrayList<>();
        ArrayList<String> current;
        for (Piece p : pieces.values()) {
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

    // EFFECTS: returns a string that represents which side won, or if it was a tie, or null if not applicable
    public String getStatus() {
        return status;
    }

    public int getNumPieces() {
        return pieces.size();
    }

    public String getTurn() {
        return turn;
    }

    // REQUIRES: board is not already set up, except for the kings, which are always on the board
    // MODIFIES: this
    // EFFECTS: sets each piece on the board to starting position
    private void setup() {
        addKings();
        for (int i = 8; i <= 15; i++) {
            addPiece(new Pawn("B", i));
            addPiece(new Pawn("W", i + 40));
        }
        addPiece(new Rook("B", 0));
        addPiece(new Rook("B", 7));
        addPiece(new Rook("W", 56));
        addPiece(new Rook("W", 63));

        addPiece(new Knight("B", 1));
        addPiece(new Knight("B", 6));
        addPiece(new Knight("W", 57));
        addPiece(new Knight("W", 62));

        addPiece(new Bishop("B", 2));
        addPiece(new Bishop("B", 5));
        addPiece(new Bishop("W", 58));
        addPiece(new Bishop("W", 61));

        addPiece(new Queen("W", 59));
        addPiece(new Queen("B", 3));
    }

    // REQUIRES: kings are not already on board
    // MODIFIES: this
    // EFFECTS: add kings to the board
    private void addKings() {
        addPiece(new King("W", 60));
        addPiece(new King("B", 4));
        log.logEvent(new Event("Created new board"));
    }

    public Move getLastMove() {
        return lastMove;
    }

    // REQUIRES: newPreviousMove is the correct move that was played prior to previous move (the second-latest move)
    // as this is not stored in GameBoard.
    // MODIFIES: this
    // EFFECTS: if no moves have been made, this method does nothing; else the board reverts to prior state before last
    // move was made.
    public void undo(Move newPreviousMove) {
        if (lastMove != null) {
            log.logEvent(new Event("Undoing last move of " + MoveList.toNotation(lastMove)));
            Piece p = lastMove.getPiece();
            removePiece(lastMove.getEnd());
            addPiece(p);
            if (lastMove.getCaptured() != null) {
                addPiece(lastMove.getCaptured());
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
            log.logEvent(new Event("Undoing last move of " + MoveList.toNotation(lastMove)));
            Piece p = lastMove.getPiece();
            removePiece(lastMove.getEnd());
            addPiece(p);
            if (lastMove.getCaptured() != null) {
                addPiece(lastMove.getCaptured());
            }
            toggleTurn();
            lastMove = null;
        }
    }
}
