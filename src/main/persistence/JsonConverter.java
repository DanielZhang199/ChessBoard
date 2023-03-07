package persistence;

import model.MoveList;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

// class with functionality that can read to and write from a file.
public class JsonConverter {
    private final String file;

    // EFFECTS: creates a new instance that can read/write to specified file
    public JsonConverter(String file) {
        this.file = file;
    }

    public MoveList getMoveList() throws IOException {
        return null;
    }

    public void saveMoveList(MoveList ml) throws FileNotFoundException {
        PrintWriter writer = new PrintWriter(file);
        writer.print(ml.toJson());
        writer.close();
    }


}
