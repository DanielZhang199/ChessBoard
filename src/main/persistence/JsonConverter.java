package persistence;

import model.Move;
import model.MoveList;
import model.Piece;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;

// utility class with functionality that can read to and write from a file.
public class JsonConverter {
    private static final int INDENT = 4;

    // EFFECTS: reads from specified file and returns a move list generated the moves in that file.
    public static MoveList getMoveList(String file) throws IOException {

        // based off of code from the JsonSerializationDemo
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            stream.forEach(builder::append);
        }

        JSONArray jsonMoves = new JSONObject(builder.toString()).getJSONArray("moves");
        MoveList result = new MoveList();
        for (Object move : jsonMoves) {
            result.addMove(jsonToMove((JSONObject) move));
        }

        return result;
    }

    public static ArrayList<String> getNotationList(String file) throws IOException {
        StringBuilder builder = new StringBuilder();
        try (Stream<String> stream = Files.lines(Paths.get(file), StandardCharsets.UTF_8)) {
            stream.forEach(builder::append);
        }
        JSONArray jsonMoves = new JSONObject(builder.toString()).getJSONArray("notation");
        ArrayList<String> result = new ArrayList<>();
        for (Object move : jsonMoves) {
            result.add(move.toString());
        }
        return result;
    }

    // EFFECTS: converts json object into a move object
    private static Move jsonToMove(JSONObject json) {
        Piece captured;
        if (json.isNull("captured")) {
            captured = null;
        } else {
            captured = jsonToPiece(json.getJSONObject("captured"));
        }
        Piece piece = jsonToPiece(json.getJSONObject("piece"));
        int start = json.getInt("start");
        int end = json.getInt("end");
        boolean check = json.getBoolean("check");

        return new Move(piece, start, end, check, captured);
    }

    // EFFECTS: converts json object into a piece object
    private static Piece jsonToPiece(JSONObject json) {
        return Piece.createPiece(json.getString("allegiance"), json.getInt("position"),
                                 json.getBoolean("moved"), json.getString("type"));
    }


    // EFFECTS: saves the inputted move list into json file
    public static void saveMoveList(MoveList ml, String file) throws IOException {
        PrintWriter writer = new PrintWriter(file);
        writer.print(ml.toJson().toString(INDENT));
        writer.close();
    }
}
