package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class PawnTest {
    private Pawn testPawnW;
    private Pawn testPawnB;
    private GameBoard board;

    @BeforeEach
    public void setup() {
        testPawnW = new Pawn("W", 52);
        testPawnB = new Pawn("B", 12);
        board = new GameBoard(true);
        // board has white king on e1 and black king on e8, pawns are in front of kings
    }

    @Test
    public void testConstructor() {
        assertEquals("P", testPawnB.getName());
        assertEquals("P", testPawnW.getName());
        assertEquals("W", testPawnW.getAllegiance());
        assertEquals("B", testPawnB.getAllegiance());
    }

    @Test
    public void testGetMovesNoObstacles() {
        board.addPiece(testPawnW);
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertEquals(2, moveList.size()); // pawn can go either one or two squares on second and seventh ranks
        assertTrue(moveList.contains(44));
        assertTrue(moveList.contains(36));
    }

    @Test
    public void testGetMovesNoObstacles2() {
        testPawnW.setPosition(44);
        board.addPiece(testPawnW);
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertEquals(1, moveList.size());
        assertTrue(moveList.contains(36));
    }

    @Test
    public void testGetMovesNoObstaclesMirror() {
        board.addPiece(testPawnB);
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertEquals(2, moveList.size());
        assertTrue(moveList.contains(20));
        assertTrue(moveList.contains(28));
    }


    // test that pawn can be blocked by pieces of either colour
    @Test
    public void testBlockedByPieces1() {
        board.addPiece(testPawnW);
        board.addPiece(new Rook("W", 36));
        assertEquals(1, testPawnW.getLegalMoves(board).size());
        assertTrue(testPawnW.getLegalMoves(board).contains(44));
        testPawnW.setPosition(44);
        board.removePiece(36);
        board.addPiece(new Rook("B", 36));
        assertEquals(0, testPawnW.getLegalMoves(board).size());
    }

    @Test
    public void testBlockedByPieces2() {
        board.addPiece(testPawnB);
        testPawnB.setPosition(44);
        board.addPiece(new Rook("W", 52));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(0, testPawnB.getLegalMoves(board).size());
    }

    @Test
    public void testCanCaptureEnemy() { // pawns capture one square diagonal
        board.addPiece(testPawnW);
        board.addPiece(new Rook("W", 43));
        board.addPiece(new Knight("B", 45));
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertTrue(moveList.contains(45));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testCanDoEnPassant1() {  // one test for each direction.
        board.addPiece(testPawnW);
        board.addPiece(new Pawn("B", 11));
        testPawnW.setPosition(36);
        board.movePiece(36, 28);
        board.movePiece(11, 27);
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertTrue(moveList.contains(19));
        assertTrue(moveList.contains(20));
        assertEquals(2, moveList.size());
    }

    @Test
    public void testCanDoEnPassant2() {
        board.addPiece(testPawnW);
        board.addPiece(new Pawn("B", 13));
        testPawnW.setPosition(36);
        board.movePiece(36, 28);
        board.movePiece(13, 29);
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertTrue(moveList.contains(21));
        assertTrue(moveList.contains(20));
        assertEquals(2, moveList.size());
    }

    @Test
    public void testCanDoEnPassant3() {
        board.addPiece(testPawnB);
        board.addPiece(new Pawn("W", 51));
        testPawnB.setPosition(36);
        board.movePiece(51, 35);
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertTrue(moveList.contains(43));
        assertTrue(moveList.contains(44));
        assertEquals(2, moveList.size());
    }

    @Test
    public void testCanDoEnPassant4() {
        board.addPiece(testPawnB);
        board.addPiece(new Pawn("W", 53));
        testPawnB.setPosition(36);
        board.movePiece(53, 37);
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertTrue(moveList.contains(44));
        assertTrue(moveList.contains(45));
        assertEquals(2, moveList.size());
    }

    @Test
    public void testCanNotDoEnPassant1() {  // one test for each direction.
        board.addPiece(testPawnW);
        board.addPiece(new Pawn("B", 10));
        testPawnW.setPosition(36);
        board.movePiece(36, 28);
        board.movePiece(10, 26);
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertFalse(moveList.contains(19));
        assertTrue(moveList.contains(20));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testCanNotDoEnPassant2() {
        board.addPiece(testPawnW);
        board.addPiece(new Pawn("B", 14));
        testPawnW.setPosition(36);
        board.movePiece(36, 28);
        board.movePiece(14, 30);
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertFalse(moveList.contains(21));
        assertTrue(moveList.contains(20));
    }

    @Test
    public void testCanNotDoEnPassant3() {
        board.addPiece(testPawnB);
        board.addPiece(new Pawn("W", 50));
        testPawnB.setPosition(36);
        board.movePiece(50, 34);
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertTrue(moveList.contains(44));
        assertFalse(moveList.contains(43));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testCanNotDoEnPassant4() {
        board.addPiece(testPawnB);
        board.addPiece(new Pawn("W", 54));
        testPawnB.setPosition(36);
        board.movePiece(54, 38);
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertTrue(moveList.contains(44));
        assertFalse(moveList.contains(45));
        assertEquals(1, moveList.size());
    }


    @Test
    public void testCanCaptureEnemyMirror() {
        board.addPiece(testPawnB);
        board.addPiece(new Rook("W", 19));
        board.addPiece(new Knight("B", 21));
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertTrue(moveList.contains(19));
        assertEquals(3, moveList.size());
    }

    // when king is in check, only legal moves are those that prevent check
    @Test
    public void testChecks1() {
        board.addPiece(testPawnW);
        board.addPiece(new Knight("B", 45));
        Set<Integer> moveList = testPawnW.getLegalMoves(board);
        assertTrue(moveList.contains(45));
        assertEquals(1, moveList.size());  // there is only one legal move (Bd2)
    }

    @Test
    public void testChecks1Mirror() {
        board.addPiece(testPawnB);
        board.addPiece(new Knight("W", 36));
        board.movePiece(36, 19);
        Set<Integer> moveList = testPawnB.getLegalMoves(board);
        assertTrue(moveList.contains(19));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks2() {
        board.addPiece(testPawnW);
        board.addPiece(new Pawn("B", 51));
        assertEquals(0, testPawnW.getLegalMoves(board).size()); // have to move king to prevent check
    }

    @Test
    public void testPin() {
        board.addPiece(testPawnW);
        testPawnW.setPosition(53);
        board.addPiece(new Bishop("B", 39));
        assertEquals(0, testPawnW.getLegalMoves(board).size());
    }
}
