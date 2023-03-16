package model;

import org.json.JSONObject;
import persistence.Savable;

import java.util.HashSet;
import java.util.Set;

// Common supertype for all pieces
// For all methods here that take GameBoard as a parameter, and in subtypes, REQUIRES: board is in legal position is
// implied to always be present.
public abstract class Piece implements Savable {
    protected int position;
    protected String allegiance;  // "B" for black, "W" for white
    protected boolean moved;  // whether the piece has moved

    // REQUIRES: position is in [0, 63], allegiance is either "W" or "B"
    // EFFECTS: sets the side and position of the piece, affirms that the piece has not yet moved
    protected Piece(String allegiance, int position) {
        this.allegiance = allegiance;
        this.position = position;
        this.moved = false;
    }

    // REQUIRES: position is in [0, 63], allegiance is either "W" or "B", type is either "P", "N", "B", "R", "Q", or "K"
    // EFFECTS: creates a piece given its fields, useful for cloning by board and after reading data from json
    public static Piece createPiece(String allegiance, int position, boolean moved, String type) {
        Piece clone = instantiatePiece(allegiance, position, type);
        if (moved) {
            clone.setPosition(position);
        }
        return clone;
    }

    // EFFECTS: creates a piece of a given subtype given the type and constructor parameters
    private static Piece instantiatePiece(String allegiance, int position, String type) {
        switch (type) {
            case "P":
                return new Pawn(allegiance, position);
            case "N":
                return new Knight(allegiance, position);
            case "B":
                return new Bishop(allegiance, position);
            case "R":
                return new Rook(allegiance, position);
            case "Q":
                return new Queen(allegiance, position);
            default:
                return new King(allegiance, position);
        }
    }

    // REQUIRES: position is a valid square to which the piece can move as governed by the board,
    // position is within interval [0, 63]
    // MODIFIES: this
    // EFFECTS: changes position of the piece to specified new position, flags the piece to have moved, moves piece in
    // board that the piece is on as well.
    public void setPosition(int position, GameBoard gb) {
        // call this method to change position of piece on board
        gb.changePos(this, position);
    }

    protected void setPosition(int position) {
        // this method is for internal use only (i.e. when board is changing position of pieces
        this.position = position;
        if (!moved) {
            this.moved = true;
        }
    }

    public int getPosition() {
        return position;
    }

    public String getAllegiance() {
        return allegiance;
    }

    public boolean isMoved() {
        return moved;
    }

    // EFFECTS: returns the type of piece this is
    public abstract String getName();

    // EFFECTS: returns a set of squares that the piece can move to:
    // this method is public as when testing for check, we need to know if pieces can capture the king despite moving
    // into check if it were their turn.
    public abstract Set<Integer> getMoves(GameBoard b);

    // EFFECTS: returns the set of all legal moves that this piece can make.
    public Set<Integer> getLegalMoves(GameBoard b) {
        Set<Integer> moves = getMoves(b);
        Set<Integer> illegalMoves = new HashSet<>();
        for (int i : moves) {
            if (b.testCheck(position, i)) {
                illegalMoves.add(i);
            }
        }
        moves.removeAll(illegalMoves);
        return moves;
    }

    // EFFECTS: abstract method that scans for pieces and stops in specified steps and starting square, until end square
    private Set<Integer> scanUp(int start, int step, int stop, GameBoard b, boolean diagonal) {
        // abstract function for scanning lines in positive direction
        Set<Integer> result = new HashSet<>();
        start += step;
        for (int i = start; i <= stop; i += step) {
            if (b.existsPiece(i)) {
                if (!(b.getPiece(i).getAllegiance().equals(this.allegiance))) {
                    result.add(i);
                }
                break;
            } else {
                result.add(i);
            }
            if (diagonal && (i % 8 == 7 || i % 8 == 0)) {  // hitting wall stops pieces moving diagonally
                break;
            }
        }
        return result;
    }

    // EFFECTS: abstract method that scans for pieces and stops in specified steps and starting square, until end square
    // works exactly the same as the previous method, just decrements by step, and tests for geq instead of leq to stop
    // the loop
    private Set<Integer> scanDown(int start, int step, int stop, GameBoard b, boolean diagonal) {
        // abstract function for scanning lines in negative direction
        Set<Integer> result = new HashSet<>();
        start -= step;
        for (int i = start; i >= stop; i -= step) {
            if (b.existsPiece(i)) {
                if (!(b.getPiece(i).getAllegiance().equals(this.allegiance))) {
                    result.add(i);
                }
                break;
            } else {
                result.add(i);
            }
            if (diagonal && (i % 8 == 7 || i % 8 == 0)) {  // hit wall
                break;
            }
        }
        return result;
    }

    // EFFECTS: gets moves for the 4 diagonal directions and returns as a set
    protected Set<Integer> getMovesDiagonal(GameBoard b) {
        Set<Integer> result;
        // iterate through each diagonal (basically up/down and left/right one square each)
        result = new HashSet<>(scanDown(this.position, 9, 0, b, true));
        result.addAll(scanDown(this.position, 7, 0, b, true));
        result.addAll(scanUp(this.position, 9, 63, b, true));
        result.addAll(scanUp(this.position, 7, 63, b, true));
        return result;
    }

    // EFFECTS: gets moves for the 4 orthogonal directions and returns as a set
    protected Set<Integer> getMovesOrthogonal(GameBoard b) {
        Set<Integer> result;
        // iterate through each row/column
        result = new HashSet<>(scanDown(this.position, 1, this.position - this.position % 8, b, false));
        result.addAll(scanUp(this.position, 1, this.position + 7 - this.position % 8, b, false));
        result.addAll(scanDown(this.position, 8, 0, b, false));
        result.addAll(scanUp(this.position, 8, 63, b, false));
        return result;
    }

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("allegiance", allegiance);
        result.put("position", position);
        result.put("moved", moved);
        result.put("type", this.getName());
        return result;
    }
}
