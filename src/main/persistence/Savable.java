package persistence;

import org.json.JSONObject;

public interface Savable {
    // EFFECTS: returns this as JSON object
    JSONObject toJson();
}
