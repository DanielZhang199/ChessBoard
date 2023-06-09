package model;

import model.exceptions.NotValidSquareException;
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
        assertEquals(0, testList.getNotationList().size());
        assertEquals(0, testList.getSize());
        assertEquals(0, testList.getMoveList().size());
    }

    @Test
    public void testAddMove() {
        Pawn testPiece = new Pawn("W", 52);
        testList.addMove(new Move(testPiece, 52, 36, false));
        assertEquals(1, testList.getSize());
        assertEquals("e4", testList.getNotationList().get(0));
        Move firstMove = testList.getPreviousMove();
        assertEquals(52, firstMove.getStart());
        assertEquals(36, firstMove.getEnd());
        assertEquals(testPiece, firstMove.getPiece());  // note testPiece is a copy of piece on board
        // this is not equal to piece on board
        assertEquals(52, firstMove.getPiece().getPosition()); // snapshotted piece should be on start square
        assertFalse(firstMove.isCheck());
        assertNull(firstMove.getCaptured());
    }

    @Test
    public void testCastleBothSides1() {
        testList.addMove(new Move(new King("W", 60), 60, 62, false));
        testList.addMove(new Move(new King("B", 4), 4, 2, false));
        String firstMove = testList.getNotationList().get(0);
        String secondMove = testList.getNotationList().get(1);
        assertEquals("O-O", firstMove);
        assertEquals("O-O-O", secondMove);
    }

    @Test
    public void testCastleBothSides2() {
        testList.addMove(new Move(new King("W", 60), 60, 58, false));
        testList.addMove(new Move(new King("B", 4), 4, 6, false));
        String firstMove = testList.getNotationList().get(0);
        String secondMove = testList.getNotationList().get(1);
        assertEquals("O-O-O", firstMove);
        assertEquals("O-O", secondMove);
    }

    @Test
    public void testNotCastle() {
        testList.addMove(new Move(new King("W", 60), 60, 59, false));
        testList.addMove(new Move(new King("B", 4), 4, 5, false));
        String firstMove = testList.getNotationList().get(0);
        String secondMove = testList.getNotationList().get(1);
        assertEquals("Kd1", firstMove);
        assertEquals("Kf8", secondMove);
    }

    @Test
    public void testAddCapture() {
        // this is a special case where the square of capture and ending square are not the same
        Pawn testPiece = new Pawn("B", 36);  // note these pieces are only copies of pieces on board
        Pawn capturedPiece1 = new Pawn("W", 51);
        testList.addMove(new Move(capturedPiece1, 51, 35, false));
        Pawn capturedPiece2 = new Pawn("W", 35);  // we must create a new piece
        testList.addMove(new Move(testPiece, 36, 43, false, capturedPiece2));
        assertEquals(2, testList.getSize());
        assertEquals("exd3", testList.getNotationList().get(1));  // google 'en passant'
        assertEquals("d4", testList.getNotationList().get(0));

        Move previousMove = testList.getPreviousMove();
        assertEquals(36, previousMove.getStart());
        assertEquals(43, previousMove.getEnd());
        assertEquals(36, previousMove.getPiece().getPosition());
        assertFalse(previousMove.isCheck());
        assertNotNull(previousMove.getCaptured());
        assertEquals(35, previousMove.getCaptured().getPosition());
    }

    @Test
    public void testIsCheckmate() {
        testList.addMove(new Move(new Queen("W", 45), 45, 13, true, new Pawn("B", 13)));
        assertEquals(1, testList.getSize());
        assertEquals("Qxf7+", testList.getNotationList().get(0));
        testList.wasCheckmate();
        assertEquals(1, testList.getNotationList().size());
        assertEquals("Qxf7#", testList.getNotationList().get(0));
    }

    @Test
    public void testUndoMove() {
        Pawn testPiece = new Pawn("W", 52);
        testList.addMove(new Move(testPiece, 52, 36, false));
        assertEquals(1, testList.getSize());
        testList.undo();
        assertEquals(0, testList.getSize());
    }
    // while it may be fundamentally different to undo a capture vs non-capture on an actual game board, the process for
    // MoveList is pretty much the exact same, so there doesn't need to be another test for undo captures.

    // Static method tests
    @Test
    public void testFromCoordinate() {
        assertEquals("c3", MoveList.fromCoordinate(42));
        assertEquals("h8", MoveList.fromCoordinate(7));
        assertEquals("g5", MoveList.fromCoordinate(30));
    }

    @Test
    public void testToCoordinate() {
        try {
            assertEquals(42, MoveList.toCoordinate("c3"));
            assertEquals(7, MoveList.toCoordinate("h8"));
            assertEquals(30, MoveList.toCoordinate("g5"));
        } catch (NotValidSquareException e) {
            fail();
        }
    }

    @Test
    public void testToCoordinateExceptions() {
        assertThrows(NotValidSquareException.class, () -> MoveList.toCoordinate("b12"));
        assertThrows(NotValidSquareException.class, () -> MoveList.toCoordinate("6e"));
        assertThrows(NotValidSquareException.class, () -> MoveList.toCoordinate("i3"));
        assertThrows(NotValidSquareException.class, () -> MoveList.toCoordinate("d0"));
        assertThrows(NotValidSquareException.class, () -> MoveList.toCoordinate("d9"));
        assertThrows(NotValidSquareException.class, () -> MoveList.toCoordinate("aa"));
    }
}
