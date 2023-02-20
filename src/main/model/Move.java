package model;

// immutable object designed to save info on previous moves
public class Move {
    private Piece piece;
    private Piece captured; // this is optional
    private int start;
    private int end;
    private boolean check;


    // EFFECTS: creates a move object for non capture moves
    public Move(Piece piece, int start, int end, boolean check) {

    }
    
    // EFFECTS: creates a move object for capture moves
    public Move(Piece piece, int start, int end, boolean check, Piece captured) {

    }

    public Piece getPiece() {
        return piece;
    }

    public Piece getCaptured() {
        return captured;
    }

    public int getStart() {
        return start;
    }

    public int getEnd() {
        return end;
    }

    public boolean isCheck() {
        return check;
    }
}
