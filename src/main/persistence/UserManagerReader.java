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

public class UserManagerReader {
    private String source;


    public UserManagerReader(String source) {
        this.source = source;
    }


    public UserManager read() throws IOException {
        try {
            String jsonData = readFile(source);
            JSONObject jsonObject = new JSONObject(jsonData);
            return parseUserManager(jsonObject);
        } catch (IOException e) {
            return new UserManager();
        }
    }


    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }


    private UserManager parseUserManager(JSONObject jsonObject) {
        UserManager um = new UserManager();
        addUsers(um, jsonObject);
        return um;
    }


    private void addUsers(UserManager um, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("allUsers");
        for (Object json : jsonArray) {
            JSONObject nextThingy = (JSONObject) json;
            addUser(um, nextThingy);
        }
    }


    private void addUser(UserManager um, JSONObject jsonObject) {
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
