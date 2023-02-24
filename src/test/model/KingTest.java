package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class KingTest {
    private Piece testKingW;   // need to be apparent type piece, as kings are already on board
    private Piece testKingB;
    private GameBoard board;

    @BeforeEach
    public void setup() {
        board = new GameBoard(true);
        testKingW = board.getPiece(60);
        testKingB = board.getPiece(4);
        // board has white king on e1, and black king on e8
    }

    @Test
    public void testConstructor() {
        assertEquals("K", testKingB.getName());
        assertEquals("K", testKingW.getName());
        assertEquals("W", testKingW.getAllegiance());
        assertEquals("B", testKingB.getAllegiance());
    }

    @Test
    public void testGetMovesNoObstacles() {
        testKingW.setPosition(52);
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        assertEquals(8, moveList.size()); // king should be able to see 8 squares adjacent and diagonal
        List<Integer> squares = Arrays.asList(43, 44, 45, 51, 53, 59, 60, 61);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }
    // technically castling is a king move, so it should be implemented here
    @Test
    public void testCastleKingSideW() {
        board.addPiece(new Rook("W", 63));
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        assertEquals(6, moveList.size());
        assertTrue(moveList.contains(62));
    }

    @Test
    public void testCastleQueenSideW() {
        board.addPiece(new Rook("W", 56));
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        assertEquals(6, moveList.size());
        assertTrue(moveList.contains(58));
    }

    @Test
    public void testCastleKingSideB() {
        board.addPiece(new Rook("B", 7));
        board.movePiece(60, 59);
        Set<Integer> moveList = testKingB.getLegalMoves(board);
        assertEquals(6, moveList.size());
        assertTrue(moveList.contains(6));
    }

    @Test
    public void testCastleQueenSideB() {
        board.addPiece(new Rook("B", 0));
        board.movePiece(60, 59);
        Set<Integer> moveList = testKingB.getLegalMoves(board);
        assertEquals(6, moveList.size());
        assertTrue(moveList.contains(2));
    }

    @Test
    public void testNoCastleWhenKingMoved() {
        testKingW.setPosition(60);
        board.addPiece(new Rook("W", 63));
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        assertEquals(5, moveList.size());
        assertFalse(moveList.contains(62));
    }

    @Test
    public void testNoCastleWhenRookMoved() {
        board.addPiece(new Rook("W", 55));
        board.movePiece(55, 63);
        board.movePiece(4, 3);
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        assertEquals(5, moveList.size());
        assertFalse(moveList.contains(62));
    }

    // test that king can't capture own pieces
    @Test
    public void testBlockedByPieces() {
        board.addPiece(new Pawn("W", 53));
        board.addPiece(new Pawn("W", 52));
        board.addPiece(new Pawn("W", 51));
        board.addPiece(new Bishop("W", 61));
        assertEquals(1, testKingW.getLegalMoves(board).size());
        assertTrue(testKingW.getLegalMoves(board).contains(59));
    }

    @Test
    public void testBlockedByPiecesMirror() {
        board.addPiece(new Pawn("B", 11));
        board.addPiece(new Pawn("B", 12));
        board.addPiece(new Pawn("B", 13));
        board.addPiece(new Bishop("B", 5));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(1, testKingB.getLegalMoves(board).size());
        assertTrue(testKingB.getLegalMoves(board).contains(3));
    }

    @Test
    public void testCanCaptureEnemy() {  // test that king can capture enemy pieces
        board.addPiece(new Rook("W", 51));
        board.addPiece(new Knight("B", 53));
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(52, 53, 61);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    @Test
    public void testCanCaptureEnemyMirror() {
        board.addPiece(new Bishop("B", 11));
        board.addPiece(new Knight("W", 12));
        board.movePiece(60, 59);
        Set<Integer> moveList = testKingB.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(3, 12, 13, 5);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    // when king is in check, only moves are those that move out of check
    @Test
    public void testChecksWhite() {
        board.addPiece(new Queen("B", 52));
        Set<Integer> moveList = testKingW.getLegalMoves(board);
        assertTrue(moveList.contains(52));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecksBlack() {
        board.addPiece(new Queen("W", 15));
        board.movePiece(15, 36);
        Set<Integer> moveList = testKingB.getLegalMoves(board);
        List<Integer> squares = Arrays.asList(3, 11, 13, 5);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    @Test
    public void testChecksWhite2() {
        board.addPiece(new Pawn("W", 51));
        board.addPiece(new Pawn("W", 52));
        board.addPiece(new Rook("W", 53));
        board.addPiece(new Rook("B", 63));
        assertEquals(0, testKingW.getLegalMoves(board).size()); // have to move rook to prevent check
    }

}
