package persistence;

import model.*;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class JsonConverterTest {
    @Test
    public void FileNotExistTest() {
        try {
            JsonConverter.getMoveList("./data/FileNotFound.json");
            fail("IOException should have been thrown.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void InvalidFileNameTest() {
        try {
            JsonConverter.saveMoveList(new MoveList(), "./data/I\nvalidFileName.json");
            fail("IOException should have been thrown.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReadWriteEmptyMoveList() {
        try {
            JsonConverter.saveMoveList(new MoveList(), "./data/testReadWriteEmptyMoveList.json");
            assertEquals(0, JsonConverter.getNotationList("./data/testReadWriteEmptyMoveList.json").size());
            assertEquals(0, JsonConverter.getMoveList("./data/testReadWriteEmptyMoveList.json").getSize());
        } catch (FileNotFoundException e) {
            fail("No FileNotFoundException should have been thrown");
        } catch (IOException e) {
            fail("No IOException should have been thrown");
        }
    }

    @Test
    public void testReadWriteNormalMoveList() {
        try {
            MoveList ml = new MoveList();
            ml.addMove(new Move(new Pawn("W", 52), 52, 36, false));
            // the next move doesn't make any sense, but is used for branch coverage
            ml.addMove(new Move(new Pawn("B", 12), 12, 28, true, new Rook("W", 28)));
            JsonConverter.saveMoveList(ml, "./data/testReadWriteNormalMoveList.json");

            MoveList newML = JsonConverter.getMoveList("./data/testReadWriteNormalMoveList.json");
            assertEquals(2, newML.getSize());
            assertEquals(ml.getNotationList(), newML.getNotationList());

            Move original1 = newML.getPreviousMove();
            Move new1 = ml.getPreviousMove();

            newML.undo();
            ml.undo();
            Move original2 = newML.getPreviousMove();
            Move new2 = ml.getPreviousMove();
            assertTrue(moveEquals(original1, new1));
            assertTrue(moveEquals(original2, new2));

            ArrayList<String> notationList = JsonConverter.getNotationList("./data/testReadWriteNormalMoveList.json");
            assertEquals(2, notationList.size());
            assertEquals("e4", notationList.get(0));
            assertEquals("exe5+", notationList.get(1));
        } catch (FileNotFoundException e) {
            fail("No FileNotFoundException should have been thrown");
        } catch (IOException e) {
            fail("No IOException should have been thrown");
        }
    }

    // make sure all fields on the move are equal, since you normally wouldn't need to do this
    private boolean moveEquals(Move x, Move y) {
        return x.getEnd() == y.getEnd() && x.getStart() == y.getStart() && x.isCheck()== y.isCheck()
                && pieceEquals(x.getPiece(), y.getPiece()) && pieceEquals(x.getCaptured(), y.getCaptured());
    }

    private boolean pieceEquals(Piece x, Piece y) {
        if (x == null) {
            return y == null;
        }
        return x.isMoved() == y.isMoved() && x.getAllegiance().equals(y.getAllegiance())
                && x.getName().equals(y.getName()) && x.getPosition() == y.getPosition();
    }
}
