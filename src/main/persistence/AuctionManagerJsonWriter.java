package persistence;

import model.AuctionManager;
import org.json.JSONObject;


import java.io.*;

// Implemented persistence by referring to JSONSerializationDemo
// (https://github.students.cs.ubc.ca/CPSC210/JsonSerializationDemo)

// Represents a writer that writes JSON representation of AuctionManager to file
public class AuctionManagerJsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private final String destination;


    // EFFECTS: constructs writer to write to destination file
    public AuctionManagerJsonWriter(String destination) {
        this.destination = destination;
    }


    // MODIFIES: this
    // EFFECTS: opens writer; throws FileNotFoundException if destination file cannot
    // be opened for writing
    public void open() throws FileNotFoundException {
        writer = new PrintWriter(destination);
    }


    // MODIFIES: this
    // EFFECTS: writes JSON representation of AuctionManager to file
    public void write(AuctionManager am) {
        JSONObject json = am.toJson();
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