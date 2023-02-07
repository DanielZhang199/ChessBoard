package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class GameBoardTest {
    GameBoard newBoard;
    GameBoard emptyBoard;

    @BeforeEach
    public void setup() {
        newBoard = new GameBoard(false);
        emptyBoard = new GameBoard(true);
    }

    @Test
    public void testConstructorState() {
        assertEquals("W", newBoard.getTurn());
        assertNull(newBoard.getStatus());
        assertEquals("K", emptyBoard.getPiece(60).getName());
        assertEquals("K", emptyBoard.getPiece(4).getName());
        assertEquals(2, emptyBoard.getNumPieces());
        assertEquals(32, newBoard.getNumPieces());

        for (int i = 0; i <= 15; i++) {
            assertTrue(newBoard.existsPiece(i));
            assertEquals("B", newBoard.getPiece(i).getAllegiance());
            assertTrue(newBoard.existsPiece(i + 40));
            assertEquals("W", newBoard.getPiece(i + 40).getAllegiance());
        }

        for (int i = 16; i <= 47; i++) {
            assertFalse(newBoard.existsPiece(i));
        }
    }
    @Test
    public void testConstructorPieces() {
        for (int i = 8; i <= 15; i++) {
            assertEquals("P", newBoard.getPiece(i).getName());
            assertEquals("P", newBoard.getPiece(i + 40).getName());
        }

        assertEquals("R", newBoard.getPiece(0).getName());
        assertEquals("R", newBoard.getPiece(7).getName());
        assertEquals("R", newBoard.getPiece(56).getName());
        assertEquals("R", newBoard.getPiece(63).getName());

        assertEquals("N", newBoard.getPiece(1).getName());
        assertEquals("N", newBoard.getPiece(6).getName());
        assertEquals("N", newBoard.getPiece(57).getName());
        assertEquals("N", newBoard.getPiece(62).getName());

        assertEquals("B", newBoard.getPiece(2).getName());
        assertEquals("B", newBoard.getPiece(5).getName());
        assertEquals("B", newBoard.getPiece(58).getName());
        assertEquals("B", newBoard.getPiece(61).getName());

        assertEquals("Q", newBoard.getPiece(3).getName());
        assertEquals("Q", newBoard.getPiece(59).getName());
    }

    // existsPiece(), getPiece() will be tested implicitly

    @Test
    public void testFirstMove() {
        newBoard.movePiece(52, 36); // this would be the move 1. e4
        assertEquals("B", newBoard.getTurn());
        assertFalse(newBoard.existsPiece(52));  // piece is gone on e2
        assertTrue(newBoard.existsPiece(36));  // there is piece on e4

        // make sure piece is a white pawn
        assertEquals("P", newBoard.getPiece(36).getName());
        assertEquals("W", newBoard.getPiece(36).getAllegiance());
        // in theory, it would be best to test that no other pieces moved
        // in practice that would be a huge waste of time, since it will probably cause some other test to fail

        newBoard.movePiece(12, 28); //  1. e4 e5
        assertEquals("W", newBoard.getTurn());
        assertFalse(newBoard.existsPiece(12));  // piece is gone on e7
        assertTrue(newBoard.existsPiece(28));  // there is piece on e5

        // make sure piece is a black pawn
        assertEquals("P", newBoard.getPiece(28).getName());
        assertEquals("B", newBoard.getPiece(28).getAllegiance());
    }

    @Test
    public void testCaptures() {
        newBoard.movePiece(52, 36);
        newBoard.movePiece(12, 28);
        newBoard.movePiece(62, 45);
        newBoard.movePiece(6, 21);
        newBoard.movePiece(45, 28);
        // 1. e4 e5 2. Nf3 Nf6 3. Nxe5
        assertEquals("B", newBoard.getTurn());

        // make sure piece is a white knight
        assertEquals("N", newBoard.getPiece(28).getName());
        assertEquals("W", newBoard.getPiece(28).getAllegiance());
        assertEquals(31, newBoard.getNumPieces());


        newBoard.movePiece(21, 36); // 3... Nxe4
        assertEquals("W", newBoard.getTurn());

        // make sure piece is a black knight
        assertEquals("N", newBoard.getPiece(36).getName());
        assertEquals("B", newBoard.getPiece(36).getAllegiance());
        assertEquals(30, newBoard.getNumPieces());
    }

    @Test
    public void testAddRook() {
        emptyBoard.addPiece(new Rook("W", 56));
        assertTrue(emptyBoard.existsPiece(56));
        assertEquals("R", newBoard.getPiece(56).getName());
        assertEquals("W", newBoard.getPiece(56).getAllegiance());

        emptyBoard.addPiece(new Rook("B", 0));
        assertTrue(emptyBoard.existsPiece(0));
        assertEquals("R", newBoard.getPiece(0).getName());
        assertEquals("B", newBoard.getPiece(0).getAllegiance());
    }
}
