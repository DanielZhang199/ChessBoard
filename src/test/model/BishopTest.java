package model;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import java.util.Arrays;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class BishopTest {
    private Bishop testBishopW;
    private Bishop testBishopB;
    private GameBoard board;

    @BeforeEach
    public void setup() {
        testBishopW = new Bishop("W", 58);
        testBishopB = new Bishop("B", 2);
        board = new GameBoard(true);
        // board has white king on e1, bishops on c1 and c8 as needed, and black king on e8
    }

    @Test
    public void testConstructor() {
        assertEquals("B", testBishopB.getName());
        assertEquals("B", testBishopW.getName());
        assertEquals("W", testBishopW.getAllegiance());
        assertEquals("B", testBishopB.getAllegiance());
    }

    @Test
    public void testGetMovesNoObstacles() {
        board.addPiece(testBishopW);
        testBishopW.setPosition(43);
        Set<Integer> moveList = testBishopW.getMoves(board);
        assertEquals(11, moveList.size()); // bishop should be able to see 11 squares
        List<Integer> squares = Arrays.asList(16, 25, 34, 52, 61, 57, 50, 36, 29, 22, 15);

        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
    }


    // test that bishop can be blocked by pieces
    @Test
    public void testBlockedByPieces() {
        board.addPiece(testBishopW);
        board.addPiece(new Rook("W", 49));
        board.addPiece(new Pawn("W", 51));
        assertEquals(0, testBishopW.getMoves(board).size());
    }

    @Test
    public void testBlockedByPiecesMirror() {
        board.addPiece(testBishopB);
        board.addPiece(new Rook("B", 9));
        board.addPiece(new Pawn("B", 11));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented
        assertEquals(0, testBishopB.getMoves(board).size());
    }

    @Test
    public void testCanCaptureEnemy() {  // test that bishop will stop before own pieces, but can capture enemy pieces
        board.addPiece(testBishopW);
        board.addPiece(new Rook("W", 40));
        board.addPiece(new Knight("B", 37));
        Set<Integer> moveList = testBishopW.getMoves(board);
        List<Integer> squares = Arrays.asList(49, 51, 44, 37);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    @Test
    public void testCanCaptureEnemyMirror() {
        board.addPiece(testBishopB);
        board.addPiece(new Rook("W", 16));
        board.addPiece(new Knight("B", 38));
        board.movePiece(60, 59); // to set board to black to move, once turns are implemented

        Set<Integer> moveList = testBishopB.getMoves(board);
        List<Integer> squares = Arrays.asList(9, 16, 11, 20, 29);
        for (int i : squares) {
            assertTrue(moveList.contains(i));
        }
        assertEquals(squares.size(), moveList.size());
    }

    // when king is in check, only legal moves are those that prevent check
    @Test
    public void testChecks1() {
        board.addPiece(testBishopW);
        board.addPiece(new Queen("B", 24));
        Set<Integer> moveList = testBishopW.getMoves(board);
        assertTrue(moveList.contains(51));
        assertEquals(1, moveList.size());  // there is only one legal move (Bd2)
    }

    @Test
    public void testChecks1Mirror() {
        board.addPiece(testBishopB);
        board.addPiece(new Queen("W", 56));
        board.movePiece(56, 32);
        Set<Integer> moveList = testBishopB.getMoves(board);
        assertTrue(moveList.contains(11));
        assertEquals(1, moveList.size());
    }

    @Test
    public void testChecks2() {
        board.addPiece(testBishopW);
        board.addPiece(new Pawn("B", 51));
        board.addPiece(new Rook("B", 12));
        assertEquals(0, testBishopW.getMoves(board).size()); // have to move king to prevent check
    }

    @Test
    public void testPin() {
        board.addPiece(testBishopW);
        testBishopW.setPosition(44);
        board.addPiece(new Rook("B", 12));
        assertEquals(0, testBishopW.getMoves(board).size());
    }
}
