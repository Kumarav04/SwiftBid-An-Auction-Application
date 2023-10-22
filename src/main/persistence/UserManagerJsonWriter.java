package persistence;

import model.UserManager;
import org.json.JSONObject;


import java.io.*;


public class UserManagerJsonWriter {
    private static final int TAB = 4;
    private PrintWriter writer;
    private String destination;


    public UserManagerJsonWriter(String destination) {
        this.destination = destination;
    }


    public void open() throws FileNotFoundException {
        writer = new PrintWriter(new File(destination));
    }


    public void write(UserManager um) {
        JSONObject json = um.toJson();
        saveToFile(json.toString(TAB));
    }


    public void close() {
        writer.close();
    }


    private void saveToFile(String json) {
        writer.print(json);
    }


}