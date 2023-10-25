package persistence;

import model.User;
import model.Auction;
import model.UserManager;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.stream.Stream;


import org.json.*;

// Represents a reader that reads UserManager from JSON data stored in file
public class UserManagerReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public UserManagerReader(String source) {
        this.source = source;
    }

    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public UserManager read() throws IOException {
        try {
            String jsonData = readFile(source);
            JSONObject jsonObject = new JSONObject(jsonData);
            return parseUserManager(jsonObject);
        } catch (IOException e) {
            return new UserManager();
        }
    }

    // EFFECTS: reads source file as string and returns it
    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }

    // EFFECTS: parses UserManager from JSON object and returns it
    private UserManager parseUserManager(JSONObject jsonObject) {
        UserManager um = new UserManager();
        addUsers(um, jsonObject);
        return um;
    }


    // MODIFIES: um
    // EFFECTS: parses Users from JSON object and adds them to UserManager
    private void addUsers(UserManager um, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("allUsers");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addUser(um, nextThingy);
        }
    }

    // MODIFIES: um
    // EFFECTS: parses User from JSON object and adds it to UserManager
    protected void addUser(UserManager um, JSONObject jsonObject) {
        String userName = jsonObject.getString("userName");
        String passWord = jsonObject.getString("passWord");
        User user = new User(userName, passWord);
        JSONArray wishlistArray = jsonObject.getJSONArray("wishList");
        ArrayList<Auction> wishList = new ArrayList<>();
        for (int i = 0; i < wishlistArray.length(); i++) {
            JSONObject auctionJson = wishlistArray.getJSONObject(i);
            Auction auction = Auction.fromJson(auctionJson);
            wishList.add(auction);
        }
        user.setWishList(wishList);
        um.addUser(user);
    }
}
