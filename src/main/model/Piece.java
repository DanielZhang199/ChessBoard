package model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;

// Common supertype for all pieces
public abstract class Piece {
    protected int position;
    protected String allegiance;  // "B" for black, "W" for white
    protected boolean moved;  // whether the piece has moved

    // EFFECTS: sets the side and position of the piece
    protected Piece(String allegiance, int position) {
        this.allegiance = allegiance;
        this.position = position;
        this.moved = false;
    }

    // REQUIRES: position is a valid square to which the piece can move, and within interval [0, 63]
    // MODIFIES: this
    // EFFECTS: changes position of the piece to specified new position, flags the piece to have moved
    public void setPosition(int position) {
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
    abstract String getName();

    // EFFECTS: returns a set of squares that the piece can move to
    abstract Set<Integer> getMoves(GameBoard b);

    // EFFECTS: calls getMoves, and filters out illegal moves (those that would result in staying in check)
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

    private Set<Integer> scanDown(int start, int step, int stop, GameBoard b, boolean diagonal) {
        // abstract function for scanning lines in negative direction
        Set<Integer> result = new HashSet<>();
        start -= step;
        for (int i = start; i >= stop; i -= step) {
            if (b.existsPiece(i)) {
                if (!(b.getPiece(i).getAllegiance().equals(getAllegiance()))) {
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

    protected Set<Integer> getMovesDiagonal(GameBoard b) {
        Set<Integer> result;
        // iterate through each diagonal (basically up/down and left/right one square each)
        result = new HashSet<>(scanDown(this.position, 9, 0, b, true));
        result.addAll(scanDown(this.position, 7, 0, b, true));
        result.addAll(scanUp(this.position, 9, 63, b, true));
        result.addAll(scanUp(this.position, 7, 63, b, true));
        return result;
    }

    protected Set<Integer> getMovesOrthogonal(GameBoard b) {
        Set<Integer> result;
        // iterate through each row/column
        result = new HashSet<>(scanDown(this.position, 1, this.position - this.position % 8, b, false));
        result.addAll(scanUp(this.position, 1, this.position + 7 - this.position % 8, b, false));
        result.addAll(scanDown(this.position, 8, 0, b, false));
        result.addAll(scanUp(this.position, 8, 63, b, false));
        return result;
    }
}
