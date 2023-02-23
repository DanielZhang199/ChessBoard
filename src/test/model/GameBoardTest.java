package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

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

        // empty board is set up correctly
        assertEquals("K", emptyBoard.getPiece(60).getName());
        assertEquals("K", emptyBoard.getPiece(4).getName());
        assertEquals(2, emptyBoard.getNumPieces());

        // new board is set up
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
        assertNull(newBoard.getLastMove());
    }
    @Test
    public void testConstructorPieces() {
        // test new board has all pieces in correct positions
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
        assertNotNull(newBoard.getLastMove());
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
    public void testCastling() {
        emptyBoard.addPiece(new Rook("W", 63));
        assertTrue(emptyBoard.movePiece(60, 62)); // if this test fails, there is an issue with King class
        assertEquals("R", newBoard.getPiece(61).getName());
        assertEquals("W", newBoard.getPiece(61).getAllegiance());
        assertEquals("K", newBoard.getPiece(62).getName());
        assertEquals("W", newBoard.getPiece(62).getAllegiance());
    }

    @Test
    public void testEnPassant() {
        newBoard.movePiece(52, 36);
        newBoard.movePiece(6, 21);
        newBoard.movePiece(36, 28);
        newBoard.movePiece(11, 27);
        assertTrue(newBoard.movePiece(28, 19)); // if this test fails, there is an issue with Pawn class
        assertEquals("P", newBoard.getPiece(19).getName());
        assertEquals("W", newBoard.getPiece(19).getAllegiance());
        assertFalse(newBoard.existsPiece(27));
    }

    // we don't need to test situations that forbid these moves since they should be accounted for in respective classes
    // the previous two tests are just to make sure that board is updating appropriately.

    @Test
    public void testAddPieces() {
        emptyBoard.addPiece(new Rook("W", 56));
        assertTrue(emptyBoard.existsPiece(56));
        assertEquals("R", emptyBoard.getPiece(56).getName());
        assertEquals("W", emptyBoard.getPiece(56).getAllegiance());
        assertEquals(3, emptyBoard.getNumPieces());
        emptyBoard.addPiece(new Bishop("B", 0));
        assertTrue(emptyBoard.existsPiece(0));
        assertEquals("B", emptyBoard.getPiece(0).getName());
        assertEquals("B", emptyBoard.getPiece(0).getAllegiance());
        assertEquals(4, emptyBoard.getNumPieces());
    }

    @Test
    public void testAddMany() {
        for (int i = 48; i <= 55; i++) {
            emptyBoard.addPiece(new Pawn("W", i));
        }
        for (int i = 48; i <= 55; i++) {
            assertTrue(emptyBoard.existsPiece(i));
        }
        assertEquals(10, emptyBoard.getNumPieces());
    }

    @Test
    public void testRemoveOne() {
        newBoard.removePiece(0);
        assertFalse(newBoard.existsPiece(0));
        assertEquals(31, newBoard.getNumPieces());
    }

    @Test
    public void testRemoveMany() {
        for (int i = 8; i <= 15; i++) {
            newBoard.removePiece(i);
        }
        assertEquals(24, newBoard.getNumPieces());

        for (int i = 8; i <= 15; i++) {
            assertFalse(newBoard.existsPiece(i));
        }
        assertEquals(16, newBoard.getNumPieces());
    }

    @Test
    public void testSimpleCheckWhite() {
        emptyBoard.addPiece(new Rook("B", 12));
        assertTrue(emptyBoard.testCheck(60, 52));  // this move keeps king in check
        assertFalse(emptyBoard.testCheck(60, 59));  // this move gets king out of check
    }

    @Test
    public void testSimpleCheckBlack() {
        emptyBoard.movePiece(4, 12);
        emptyBoard.addPiece(new Rook("W", 52));
        assertTrue(emptyBoard.testCheck(12, 20));  // this move keeps king in check
        assertFalse(emptyBoard.testCheck(12, 3));  // this move gets king out of check
    }

    // while you cannot move into a check by the rules of chess, pieces need to be able to tell if a move would lead to
    // check, in order to disallow that move from being made
    @Test
    public void testMoveIntoCheck() {
        emptyBoard.addPiece(new Rook("B", 12));
        emptyBoard.addPiece(new Rook("W", 52));
        assertTrue(emptyBoard.testCheck(52, 51));
        assertFalse(emptyBoard.testCheck(52, 12));
        assertFalse(emptyBoard.testCheck(52, 20));
    }

    @Test
    public void testCheckmate() {
        newBoard.movePiece(52, 36);
        newBoard.movePiece(13, 21);
        newBoard.movePiece(51, 35);
        newBoard.movePiece(14, 30);
        assertFalse(newBoard.checkStatus());
        assertNull(newBoard.getStatus());
        newBoard.movePiece(59, 31);
        // 1. e4 f6 2. d4 g5 3. Qh5#
        assertTrue(newBoard.checkStatus());
        assertEquals("White Wins By Checkmate", newBoard.getStatus());
    }

    @Test
    public void testStalemate() {
        emptyBoard.addPiece(new Queen("W", 19));
        emptyBoard.addPiece(new Queen("W", 63));
        assertFalse(emptyBoard.checkStatus());
        emptyBoard.movePiece(63, 61);
        assertTrue(emptyBoard.checkStatus());
        assertEquals("Stalemate", emptyBoard.getStatus());
    }

    @Test
    public void testStalemateBlack() {
        emptyBoard.addPiece(new Queen("B", 44));
        emptyBoard.addPiece(new Queen("B", 7));
        assertFalse(emptyBoard.checkStatus());  // white is in check, but game is not over
        emptyBoard.movePiece(60, 61);
        assertFalse(emptyBoard.checkStatus());
        emptyBoard.movePiece(7, 6);
        assertTrue(emptyBoard.checkStatus());
        assertEquals("Stalemate", emptyBoard.getStatus());
    }

    // there are different ways to decide when game is over by insufficient material, in this case the rule is that:
    // if both sides have EITHER <=2 knights OR <=1 bishop
    @Test
    public void testInsufficientMaterialKings() {
        assertTrue(emptyBoard.checkStatus());
        assertEquals("Draw by Insufficient Material", emptyBoard.getStatus());
    }

    @Test
    public void testInsufficientMaterialKnightBishop() {
        emptyBoard.addPiece(new Knight("B", 0));
        emptyBoard.addPiece(new Knight("B", 1));
        emptyBoard.addPiece(new Bishop("W", 56));
        assertTrue(emptyBoard.checkStatus());
        assertEquals("Draw by Insufficient Material", emptyBoard.getStatus());
    }

    @Test
    public void testKnightBishopGameContinues() {
        emptyBoard.addPiece(new Knight("W", 1));
        emptyBoard.addPiece(new Bishop("W", 56));
        assertFalse(emptyBoard.checkStatus());
    }

    @Test
    public void testBishopBishopGameContinues() {
        emptyBoard.addPiece(new Bishop("B", 1));
        emptyBoard.addPiece(new Bishop("B", 56));
        assertFalse(emptyBoard.checkStatus());
    }

    // although it is unlikely to have 3 knights in a game
    @Test
    public void test3KnightGameContinues() {
        emptyBoard.addPiece(new Knight("B", 1));
        emptyBoard.addPiece(new Knight("B", 2));
        emptyBoard.addPiece(new Knight("B", 3));
        assertFalse(emptyBoard.checkStatus());
    }

    // the following moves should implicitly test that the lastMove is accurate.
    @Test
    public void testUndoFirstMove() {
        newBoard.movePiece(51, 35);
        newBoard.undo();
        assertFalse(newBoard.existsPiece(35));
        assertEquals("P", emptyBoard.getPiece(51).getName());
        assertEquals("W", emptyBoard.getPiece(51).getAllegiance());
        assertNull(newBoard.getLastMove());
    }

    @Test
    public void testUndoMultiple() {
        newBoard.movePiece(52, 36);
        Move savedMove = newBoard.getLastMove();
        newBoard.movePiece(12, 28);
        newBoard.undo(savedMove);
        assertFalse(newBoard.existsPiece(28));
        assertTrue(newBoard.existsPiece(12));
        assertTrue(newBoard.existsPiece(36));
        assertEquals("P", emptyBoard.getPiece(12).getName());
        assertEquals("B", emptyBoard.getPiece(12).getAllegiance());
        newBoard.undo();  // no savedMove as there was no earlier moves made
        assertFalse(newBoard.existsPiece(36));
        assertEquals("P", emptyBoard.getPiece(52).getName());
        assertEquals("W", emptyBoard.getPiece(52).getAllegiance());
    }
}