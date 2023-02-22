package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class MoveListTest {
    private MoveList testList;

    @BeforeEach
    public void setup() {
        testList = new MoveList();
    }

    @Test
    public void testConstructor() {
        assertEquals(0, testList.getMoves().size());
        assertEquals(0, testList.getHumanList().size());
    }

    @Test
    public void testAddMove() {
        Pawn testPiece = new Pawn("W", 52);
        testList.addMove(testPiece, 52, 36, false);
        assertEquals(1, testList.getHumanList().size());
        assertEquals("e4", testList.getHumanList().get(0));
        Move firstMove = testList.getMoves().get(0);
        assertEquals(52, firstMove.getStart());
        assertEquals(36, firstMove.getEnd());
        assertEquals(testPiece, firstMove.getPiece());  // note testPiece is a copy of piece on board
        // this is not equal to piece on board
        assertEquals(52, firstMove.getPiece().getPosition()); // snapshotted piece should be on start square
        assertFalse(firstMove.isCheck());
        assertNull(firstMove.getCaptured());
    }

    @Test
    public void testAddCapture() {
        // this is a special case where the square of capture and ending square are not the same
        Pawn testPiece = new Pawn("B", 36);  // note these pieces are only copies of pieces on board
        Pawn capturedPiece1 = new Pawn("W", 51);
        testList.addMove(capturedPiece1, 51, 35, false);
        Pawn capturedPiece2 = new Pawn("W", 35);  // we must create a new piece
        testList.addMove(testPiece, 36, 43, false, capturedPiece2);
        assertEquals(2, testList.getHumanList().size());
        assertEquals("exd3", testList.getHumanList().get(1));  // google 'en passant'

        Move firstMove = testList.getMoves().get(1);
        assertEquals(36, firstMove.getStart());
        assertEquals(43, firstMove.getEnd());
        assertEquals(43, firstMove.getPiece().getPosition());
        assertFalse(firstMove.isCheck());
        assertNotNull(firstMove.getCaptured());
        assertEquals(35, firstMove.getCaptured().getPosition());
    }

    @Test
    public void testIsCheckmate() {
        testList.addMove(new Queen("W", 45), 45, 13, true, new Pawn("B", 13));
        assertEquals(1, testList.getHumanList().size());
        assertEquals("Qxf7+", testList.getHumanList().get(0));
        testList.wasCheckmate();
        assertEquals(1, testList.getHumanList().size());
        assertEquals("Qxf7#", testList.getHumanList().get(0));
    }

    @Test
    public void testAddScore() {
        testList.addMove(new King("W", 52), 60, 52, false);
        testList.addResult("1/2-1/2");
        assertEquals(2, testList.getHumanList().size());
        assertEquals("Ke7", testList.getHumanList().get(0));
        assertEquals("1/2-1/2", testList.getHumanList().get(1));

    }

    @Test
    public void testUndoMove() {
        Pawn testPiece = new Pawn("W", 52);
        testList.addMove(testPiece, 52, 36, false);
        assertEquals(1, testList.getHumanList().size());
        Move move = testList.undo();
        assertEquals(0, testList.getMoves().size());
        assertEquals(52, move.getStart());
        assertEquals(36, move.getEnd());
        assertEquals(testPiece, move.getPiece());
        assertEquals(0, testList.getHumanList().size());
    }
}
