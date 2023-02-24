package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class RookTest {
    private Rook testRookW;
    private Rook testRookB;
    private GameBoard board;

    @BeforeEach
    public void setup() {
        testRookW = new Rook("W", 56);
        testRookB = new Rook("B", 0);
        board = new GameBoard(true);
        // board has white king on e1, rooks on a1 and a8 as needed, and black king on e8
    }

    @Test
    public void testConstructor() {
        assertEquals("R", testRookB.getName());
        assertEquals("R", testRookW.getName());
        assertEquals("W", testRookW.getAllegiance());
        assertEquals("B", testRookB.getAllegiance());
    }


    @Test
    public void testGetLegalMovesNoObstacles() {
        board.addPiece(testRookW);
        testRookW.setPosition(43);
        Set<Integer> moveList = testRookW.getLegalMoves(board);
        assertEquals(14, moveList.size()); // rook should be able to see 14 squares
        List<Integer> squares = Arrays.asList(3, 11, 19, 27, 35, 51, 59, 40, 41, 42, 44, 45, 46, 47);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }


    // test that rook can't move when surrounded by own pieces
    @Test
    public void testBlockedByPieces() {
        board.addPiece(testRookW);
        board.addPiece(new Rook("W", 57));
        board.addPiece(new Pawn("W", 48));
        board.addPiece(new Pawn("W", 49));
        assertEquals(0, testRookW.getLegalMoves(board).size());
    }

    @Test
    public void testBlockedByPiecesMirror() { // same check for black pieces
        board.addPiece(testRookB);
        board.addPiece(new Rook("B", 1));
        board.addPiece(new Pawn("B", 8));
        board.addPiece(new Pawn("B", 9));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(0, testRookB.getLegalMoves(board).size());
    }

    @Test
    public void testCanCaptureEnemy() {  // test that rook will stop before own pieces, but can capture enemy pieces
        board.addPiece(testRookW);
        board.addPiece(new Rook("W", 58));
        board.addPiece(new Knight("B", 32));
        Set<Integer> moveList = testRookW.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(57, 48, 40, 32);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    @Test
    public void testCanCaptureEnemyMirror() {
        board.addPiece(testRookB);
        board.addPiece(new Bishop("B", 3));
        board.addPiece(new Knight("W", 48));
        board.movePiece(60, 59);
        Set<Integer> moveList = testRookB.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(1, 2, 8, 16, 24, 32, 40, 48);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    // when king is in check, only moves are those that prevent check
    @Test
    public void testChecks1() {
        board.addPiece(testRookW);
        board.addPiece(new Queen("B", 59));
        Set<Integer> moveList = testRookW.getLegalMoves(board);
        assertTrue(moveList.contains(59));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks1Mirror() {
        board.addPiece(testRookB);
        board.addPiece(new Rook("W", 57));
        board.movePiece(57, 1);
        Set<Integer> moveList = testRookB.getLegalMoves(board);
        assertTrue(moveList.contains(1));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks2() {
        board.addPiece(testRookW);
        board.addPiece(new Pawn("B", 51));
        board.addPiece(new Rook("B", 12));
        assertEquals(0, testRookW.getLegalMoves(board).size()); // have to move king to prevent check
    }


    // rook can't move away when pinned
    @Test
    public void testPin() {
        board.addPiece(testRookW);
        testRookW.setPosition(44);
        board.addPiece(new Rook("B", 12));
        Set<Integer> moveList = testRookW.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(52, 36, 28, 20, 12);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }
}
