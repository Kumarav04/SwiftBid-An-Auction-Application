package persistence;

import model.AuctionManager;
import org.json.JSONObject;


import java.io.*;

public class AuctionManagerJsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;


    public AuctionManagerJsonWriter(String destination) {
        this.destination = destination;
    }


    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }


    public void write(AuctionManager am) {
        JSONObject json = am.toJson();
        saveToFile(json.toString(TAB));
    }

    public void close() {
        writer.close();
    }


    private void saveToFile(String json) {
        writer.print(json);
    }


}