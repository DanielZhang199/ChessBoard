package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class MoveTest {
    Piece rook = new Rook("W", 56);
    Piece queen = new Queen("B", 21);
    Piece pawn = new Pawn("W", 53);
    Move testMove1 = new Move(rook, 56, 59, false);
    Move testMove2 = new Move(queen, 21, 53, true, pawn);

    @Test
    public void testConstructor() {
        assertEquals(rook, testMove1.getPiece());
        assertEquals(queen, testMove2.getPiece());
        assertEquals(pawn, testMove2.getCaptured());
        assertNull(testMove1.getCaptured());

        assertEquals(56, testMove1.getStart());
        assertEquals(59, testMove1.getEnd());
        assertEquals(21, testMove2.getStart());
        assertEquals(53, testMove2.getEnd());

        assertTrue(testMove2.isCheck());
        assertFalse(testMove1.isCheck());
    }
}
