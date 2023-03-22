package model.exceptions;

import model.Piece;

public class PromotionException extends Exception {
    private final Piece captured;

    public PromotionException(Piece p) {
        captured = p;
    }

    public Piece getCaptured() {
        return captured;
    }
}
