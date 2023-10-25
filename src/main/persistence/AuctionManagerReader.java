package persistence;

import model.User;
import model.Auction;
import model.AuctionManager;


import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;


import org.json.*;

// Represents a reader that reads AuctionManager from JSON data stored in file
public class AuctionManagerReader {
    private final String source;

    // EFFECTS: constructs reader to read from source file
    public AuctionManagerReader(String source) {
        this.source = source;
    }


    // EFFECTS: reads workroom from file and returns it;
    // throws IOException if an error occurs reading data from file
    public AuctionManager read() throws IOException {
        try {
            String jsonData = readFile(source);
            JSONObject jsonObject = new JSONObject(jsonData);
            return parseAuctionManager(jsonObject);
        } catch (IOException e) {
            return new AuctionManager();
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


    // EFFECTS: parses AuctionManager from JSON object and returns it
    private AuctionManager parseAuctionManager(JSONObject jsonObject) {
        AuctionManager am = new AuctionManager();
        addAuctions(am, jsonObject);
        return am;
    }


    // MODIFIES: am
    // EFFECTS: parses Auctions from JSON object and adds them to AuctionManager
    private void addAuctions(AuctionManager am, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("auctions");
        for (Object json : jsonArray) {
            JSONObject nextAuction = (JSONObject) json;
            addAuction(am, nextAuction);
        }
    }

    // MODIFIES: am
    // EFFECTS: parses Auction from JSON object and adds it to AuctionManager
    private void addAuction(AuctionManager am, JSONObject jsonObject) {
        String listingName = jsonObject.getString("listingName");
        String sellerName = jsonObject.getString("sellerName");
        String sellerPass = jsonObject.getString("sellerPass");
        User seller = new User(sellerName, sellerPass);
        double highestBid = jsonObject.getDouble("highestBid");
        String bidderName = null;
        String bidderPass = null;

        try {
            bidderName = jsonObject.getString("highestBidderName");
            bidderPass = jsonObject.getString("highestBidderPass");
        } catch (JSONException ignored) {
            ;
        }

        User highestBidder = new User(bidderName, bidderPass);
        String description = jsonObject.getString("description");
        Auction auction = new Auction(listingName, seller, description);
        auction.setHighestBid(highestBid);
        auction.setHighestBidder(highestBidder);
        am.addAuction(auction);
    }
}
