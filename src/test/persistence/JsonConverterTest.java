package persistence;

import model.Move;
import model.MoveList;
import model.Pawn;
import org.junit.jupiter.api.Test;
import persistence.JsonConverter;

import java.io.FileNotFoundException;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class JsonConverterTest {

    @Test
    public void FileNotExistTest() {
        JsonConverter converter = new JsonConverter("./data/FileNotFound.json");

        try {
            converter.saveMoveList(new MoveList());
            fail("FileNotFoundException should have been thrown.");
        } catch (FileNotFoundException e) {
            // pass
        }

        try {
            converter.getMoveList();
            fail("IOException should have been thrown.");
        } catch (IOException e) {
            // pass
        }
    }

    @Test
    public void testReadWriteEmptyMoveList() {
        try {
            JsonConverter converter = new JsonConverter("./data/testReadWriteEmptyMoveList.json");
            converter.saveMoveList(new MoveList());

            MoveList ml = converter.getMoveList();
            assertEquals(0, ml.getSize());
        } catch (FileNotFoundException e) {
            fail("No FileNotFoundException should have been thrown");
        } catch (IOException e) {
            fail("No IOException should have been thrown");
        }
    }

    @Test
    public void testReadWriteNormalMoveList() {
        try {
            JsonConverter converter = new JsonConverter("./data/testReadWriteNormalMoveList.json");
            MoveList ml = new MoveList();
            ml.addMove(new Move(new Pawn("W", 52), 52, 36, false));
            ml.addMove(new Move(new Pawn("B", 12), 12, 28, true));
            converter.saveMoveList(ml);

            MoveList newML = converter.getMoveList();
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
        } catch (FileNotFoundException e) {
            fail("No FileNotFoundException should have been thrown");
        } catch (IOException e) {
            fail("No IOException should have been thrown");
        }
    }

    private boolean moveEquals(Move original, Move test) {
        return false;
    }
}
