package model;

import org.json.JSONObject;
import persistence.Savable;

// immutable object designed to save info on previous moves
// since Move is immutable, it can be passed around without needing to be copied
public class Move implements Savable {
    private final Piece piece;
    private final Piece captured; // this is optional
    private final int start;
    private final int end;
    private final boolean check;

    // EFFECTS: creates a move object for non capture moves
    public Move(Piece piece, int start, int end, boolean check) {
        this.piece = piece;
        this.start = start;
        this.end = end;
        this.check = check;
        this.captured = null;
    }

    // EFFECTS: creates a move object for capture moves
    public Move(Piece piece, int start, int end, boolean check, Piece captured) {
        this.piece = piece;
        this.start = start;
        this.end = end;
        this.check = check;
        this.captured = captured;
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

    @Override
    public JSONObject toJson() {
        JSONObject result = new JSONObject();
        result.put("piece", piece.toJson());
        if (captured == null) {
            result.put("captured", JSONObject.NULL);
        } else {
            result.put("captured", captured.toJson());
        }
        result.put("start", start);
        result.put("end", end);
        result.put("check", check);
        return result;
    }
}
