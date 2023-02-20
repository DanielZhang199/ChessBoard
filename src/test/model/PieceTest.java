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

}