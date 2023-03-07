package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    private final Piece testPiece = new Rook("B", 0);

    @Test
    public void testConstructor() {
        assertEquals(0, testPiece.getPosition());
        assertEquals("B", testPiece.getAllegiance());
        assertFalse(testPiece.isMoved());
    }

    @Test
    public void testSetPosition() {
        testPiece.setPosition(10);
        assertEquals(10, testPiece.getPosition());
    }

    @Test
    public void testSetMultiple() {
        testPiece.setPosition(10);
        assertEquals(10, testPiece.getPosition());
        testPiece.setPosition(32);
        assertEquals(32, testPiece.getPosition());
        testPiece.setPosition(10);
        assertEquals(10, testPiece.getPosition());
    }

    @Test
    public void testMove() {
        testPiece.setPosition(1);
        assertTrue(testPiece.isMoved());
    }

    @Test
    public void testCreatePiece() {
        Piece rook = Piece.createPiece("W", 20, false, "R");
        Piece pawn = Piece.createPiece("B", 21, true, "P");
        Piece bishop = Piece.createPiece("W", 22, false, "B");
        Piece knight = Piece.createPiece("B", 23, true, "N");
        Piece queen = Piece.createPiece("W", 24, false, "Q");
        Piece king = Piece.createPiece("B", 25, true, "K");

        assertEquals("R", rook.getName());
        assertEquals("W", rook.getAllegiance());
        assertEquals(20, rook.getPosition());
        assertFalse(rook.isMoved());
        assertEquals("P", pawn.getName());
        assertEquals("B", bishop.getName());
        assertEquals("N", knight.getName());
        assertEquals("Q", queen.getName());
        assertEquals("K", king.getName());
    }
}