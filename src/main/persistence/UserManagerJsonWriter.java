package persistence;

import model.UserManager;
import org.json.JSONObject;


import java.io.*;

// Represents a writer that writes JSON representation of UserManager to file

public class UserManagerJsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;

    // EFFECTS: constructs writer to write to destination file
    public UserManagerJsonWriter(String destination) {
        this.destination = destination;
    }

    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter((destination));
    }

    // MODIFIES: this
    // EFFECTS: writes JSON representation of UserManager to file
    public void write(UserManager um) {
        JSONObject json = um.toJson();
        saveToFile(json.toString(TAB));
    }

    // MODIFIES: this
    // EFFECTS: closes writer
    public void close() {
        writer.close();
    }

    // MODIFIES: this
    // EFFECTS: writes string to file
    private void saveToFile(String json) {
        writer.print(json);
    }


}