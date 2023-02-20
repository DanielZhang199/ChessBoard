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
        Set<Integer> moveList = testPawnW.getMoves(board);
        assertEquals(2, moveList.size()); // pawn can go either one or two squares on second and seventh ranks
        assertTrue(moveList.contains(44));
        assertTrue(moveList.contains(36));
    }

    @Test
    public void testGetMovesNoObstacles2() {
        testPawnW.setPosition(44);
        board.addPiece(testPawnW);
        Set<Integer> moveList = testPawnW.getMoves(board);
        assertEquals(1, moveList.size());
        assertTrue(moveList.contains(36));
    }

    @Test
    public void testGetMovesNoObstaclesMirror() {
        board.addPiece(testPawnB);
        Set<Integer> moveList = testPawnB.getMoves(board);
        assertEquals(2, moveList.size());
        assertTrue(moveList.contains(20));
        assertTrue(moveList.contains(28));
    }


    // test that pawn can be blocked by pieces of either colour
    @Test
    public void testBlockedByPieces1() {
        board.addPiece(testPawnW);
        board.addPiece(new Rook("W", 36));
        assertEquals(1, testPawnW.getMoves(board).size());
        assertTrue(testPawnW.getMoves(board).contains(44));
        testPawnW.setPosition(44);
        board.removePiece(36);
        board.addPiece(new Rook("B", 36));
        assertEquals(0, testPawnW.getMoves(board).size());
    }

    @Test
    public void testBlockedByPieces2() {
        board.addPiece(testPawnB);
        testPawnB.setPosition(44);
        board.addPiece(new Rook("W", 52));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(0, testPawnB.getMoves(board).size());
    }

    @Test
    public void testCanCaptureEnemy() { // pawns capture one square diagonal
        board.addPiece(testPawnW);
        board.addPiece(new Rook("W", 43));
        board.addPiece(new Knight("B", 45));
        Set<Integer> moveList = testPawnW.getMoves(board);
        assertTrue(moveList.contains(45));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testCanCaptureEnemyMirror() {
        board.addPiece(testPawnB);
        board.addPiece(new Rook("W", 19));
        board.addPiece(new Knight("B", 21));
        Set<Integer> moveList = testPawnB.getMoves(board);
        assertTrue(moveList.contains(19));
        assertEquals(1, moveList.size());
    }

    // when king is in check, only legal moves are those that prevent check
    @Test
    public void testChecks1() {
        board.addPiece(testPawnW);
        board.addPiece(new Knight("B", 45));
        Set<Integer> moveList = testPawnW.getMoves(board);
        assertTrue(moveList.contains(45));
        assertEquals(1, moveList.size());  // there is only one legal move (Bd2)
    }

    @Test
    public void testChecks1Mirror() {
        board.addPiece(testPawnB);
        board.addPiece(new Knight("W", 36));
        board.movePiece(36, 19);
        Set<Integer> moveList = testPawnB.getMoves(board);
        assertTrue(moveList.contains(19));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks2() {
        board.addPiece(testPawnW);
        board.addPiece(new Pawn("B", 51));
        assertEquals(0, testPawnW.getMoves(board).size()); // have to move king to prevent check
    }

    @Test
    public void testPin() {
        board.addPiece(testPawnW);
        testPawnW.setPosition(53);
        board.addPiece(new Bishop("B", 39));
        assertEquals(0, testPawnW.getMoves(board).size());
    }
}
