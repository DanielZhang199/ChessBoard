package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class QueenTest {
    private Queen testQueenW;
    private Queen testQueenB;
    private GameBoard board;

    @BeforeEach
    public void setup() {
        testQueenW = new Queen("W", 59);
        testQueenB = new Queen("B", 3);
        board = new GameBoard(true);
        // board has white king on e1, queens on d1 and d8 when needed, and black king on e8
    }

    @Test
    public void testConstructor() {
        assertEquals("Q", testQueenB.getName());
        assertEquals("Q", testQueenW.getName());
        assertEquals("W", testQueenW.getSide());
        assertEquals("B", testQueenB.getSide());
    }


    @Test
    public void testGetMovesNoObstacles() {
        board.addPiece(testQueenW);
        testQueenW.setPosition(43, board);
        Set<Integer> moveList = testQueenW.getLegalMoves(board);
        assertEquals(25, moveList.size()); // queen should be able to see 25 squares
        List<Integer> squares = Arrays.asList(3, 11, 19, 27, 35, 51, 59, 40, 41, 42, 44, 45, 46, 47, 16, 25, 34, 52, 61,
                15, 22, 29, 36, 50, 57);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }


    // test that queen can't move when surround by own pieces
    @Test
    public void testBlockedByPieces() {
        board.addPiece(testQueenW);
        board.addPiece(new Rook("W", 58));
        board.addPiece(new Pawn("W", 50));
        board.addPiece(new Pawn("W", 51));
        board.addPiece(new Pawn("W", 52));
        assertEquals(0, testQueenW.getLegalMoves(board).size());
    }

    @Test
    public void testBlockedByPiecesMirror() { // same check for black pieces
        board.addPiece(testQueenB);
        board.addPiece(new Rook("B", 2));
        board.addPiece(new Pawn("B", 10));
        board.addPiece(new Pawn("B", 11));
        board.addPiece(new Pawn("B", 12));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(0, testQueenB.getLegalMoves(board).size());
    }

    @Test
    public void testCanCaptureEnemy() {  // test that queen will stop before own pieces, but can capture enemy pieces
        board.addPiece(testQueenW);
        board.addPiece(new Rook("W", 58));
        board.addPiece(new Knight("B", 35));
        board.addPiece(new Bishop("B", 31));
        board.addPiece(new Pawn("W", 41));
        Set<Integer> moveList = testQueenW.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(50, 51, 43, 35, 52, 45, 38, 31);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    @Test
    public void testCanCaptureEnemyMirror() {  // test that queen will stop before own pieces, but can capture enemy pieces
        board.addPiece(testQueenB);
        board.addPiece(new Rook("B", 2));
        board.addPiece(new Knight("W", 27));
        board.addPiece(new Bishop("W", 24));
        board.addPiece(new Pawn("B", 21));
        board.movePiece(60, 59);
        Set<Integer> moveList = testQueenB.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(10, 17, 24, 11, 19, 27, 12);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    // when king is in check, only moves are those that prevent check
    @Test
    public void testChecks1() {
        board.addPiece(testQueenW);
        board.addPiece(new Pawn("B", 51));
        Set<Integer> moveList = testQueenW.getLegalMoves(board);
        assertTrue(moveList.contains(51));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks1Mirror() {
        board.addPiece(testQueenB);
        board.addPiece(new Pawn("W", 19));
        board.movePiece(19, 11);
        Set<Integer> moveList = testQueenB.getLegalMoves(board);
        assertTrue(moveList.contains(11));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks2() {
        board.addPiece(testQueenW);
        board.addPiece(new Pawn("B", 51));
        board.addPiece(new Rook("B", 12));
        assertEquals(0, testQueenW.getLegalMoves(board).size()); // have to move king to prevent check
    }


    // queen can't move away when pinned
    @Test
    public void testPin() {
        board.addPiece(testQueenW);
        testQueenW.setPosition(44, board);
        board.addPiece(new Rook("B", 12));
        Set<Integer> moveList = testQueenW.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(52, 36, 28, 20, 12);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }

    // we don't need to check if queen can/can't capture enemy king, since that can never happen on your turn in chess
}
