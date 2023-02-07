package model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PieceTest {
    private final Piece testPiece = new Piece("B", 0);

    @Test
    public void testPosition() {
        assertEquals(0, testPiece.getPosition());
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
    public void testAllegiance() {
        assertEquals("B", testPiece.getAllegiance());
        Piece newTestPiece = new Piece("W", 12);
        assertEquals("W", newTestPiece.getAllegiance());
    }

    @Test
    public void testNullMethods() {
        assertNull(testPiece.getName());
        assertNull(testPiece.getMoves(new GameBoard(true)));
    }
}