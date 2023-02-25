package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KnightTest {
    private Knight testKnightW;
    private Knight testKnightB;
    private GameBoard board;

    @BeforeEach
    public void setup() {
        testKnightW = new Knight("W", 57);
        testKnightB = new Knight("B", 1);
        board = new GameBoard(true);
        // board has white king on e1, knights on b1 and b8 as needed, and black king on e8
    }

    @Test
    public void testConstructor() {
        assertEquals("N", testKnightB.getName());
        assertEquals("N", testKnightW.getName());
        assertEquals("W", testKnightW.getAllegiance());
        assertEquals("B", testKnightB.getAllegiance());
    }

    @Test
    public void testGetMovesNoObstacles() {
        board.addPiece(testKnightW);
        testKnightW.setPosition(35);
        Set<Integer> moveList = testKnightW.getLegalMoves(board);
        assertEquals(8, moveList.size()); // knight should be able to see 8 squares
        List<Integer> squares = Arrays.asList(18, 20, 25, 29, 41, 45, 50, 52);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }

    @Test
    public void testGetMovesSideB() {
        board.addPiece(testKnightW);
        testKnightW.setPosition(51);
        Set<Integer> moveList = testKnightW.getLegalMoves(board);
        assertEquals(6, moveList.size()); // knight should be able to see 6 squares
        List<Integer> squares = Arrays.asList(57, 41, 34, 36, 45, 61);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }

    @Test
    public void testGetMovesSideR() {
        board.addPiece(testKnightW);
        testKnightW.setPosition(47);
        Set<Integer> moveList = testKnightW.getLegalMoves(board);
        assertEquals(4, moveList.size()); // knight should be able to see 4 squares
        List<Integer> squares = Arrays.asList(30, 37, 53, 62);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }

    @Test
    public void testGetMovesCorner() {
        board.addPiece(testKnightW);
        testKnightW.setPosition(56);
        Set<Integer> moveList = testKnightW.getLegalMoves(board);
        assertEquals(2, moveList.size());
        List<Integer> squares = Arrays.asList(41, 50);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }


    // test that knight can not be blocked by pieces unless they occupy the destination square
    @Test
    public void testBlockedByPieces() {
        board.addPiece(testKnightW);
        board.addPiece(new Rook("W", 40));
        board.addPiece(new Pawn("W", 50));
        board.addPiece(new Pawn("W", 51));
        assertEquals(1, testKnightW.getLegalMoves(board).size());
        assertTrue(testKnightW.getLegalMoves(board).contains(42));
    }

    @Test
    public void testBlockedByPiecesMirror() {
        board.addPiece(testKnightB);
        board.addPiece(new Rook("B", 16));
        board.addPiece(new Pawn("B", 10));
        board.addPiece(new Pawn("B", 11));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(1, testKnightB.getLegalMoves(board).size());
        assertTrue(testKnightB.getLegalMoves(board).contains(18));
    }

    @Test
    public void testCanCaptureEnemy() {  // test that knight will stop before own pieces, but can capture enemy pieces
        board.addPiece(testKnightW);
        board.addPiece(new Rook("W", 40));
        board.addPiece(new Knight("B", 42));
        Set<Integer> moveList = testKnightW.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(42, 51);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    @Test
    public void testCanCaptureEnemyMirror() {
        board.addPiece(testKnightB);
        board.addPiece(new Bishop("B", 16));
        board.addPiece(new Knight("W", 18));
        board.movePiece(60, 59);
        Set<Integer> moveList = testKnightB.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(18, 11);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    // when king is in check, only moves are those that prevent check
    @Test
    public void testChecks1() {
        board.addPiece(testKnightW);
        board.addPiece(new Queen("B", 24));
        Set<Integer> moveList = testKnightW.getLegalMoves(board);
        assertTrue(moveList.contains(42));
        assertTrue(moveList.contains(51));
        assertEquals(2, moveList.size());
    }

    @Test
    public void testChecks1Mirror() {
        board.addPiece(testKnightB);
        board.addPiece(new Queen("W", 56));
        board.movePiece(56, 32);
        Set<Integer> moveList = testKnightB.getLegalMoves(board);
        assertTrue(moveList.contains(11));
        assertTrue(moveList.contains(18));
        assertEquals(2, moveList.size());
    }

    @Test
    public void testChecks2() {
        board.addPiece(testKnightW);
        board.addPiece(new Pawn("B", 51));
        board.addPiece(new Rook("B", 12));
        assertEquals(0, testKnightW.getLegalMoves(board).size()); // have to move king to prevent check
    }


    // knight can't move away when pinned
    @Test
    public void testPin() {
        board.addPiece(testKnightW);
        testKnightW.setPosition(44);
        board.addPiece(new Rook("B", 12));
        assertEquals(0, testKnightW.getLegalMoves(board).size());
    }
}
