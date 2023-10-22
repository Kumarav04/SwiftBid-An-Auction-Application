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


public class AuctionManagerReader {
    private String source;


    public AuctionManagerReader(String source) {
        this.source = source;
    }


    public AuctionManager read() throws IOException {
        try {
            String jsonData = readFile(source);
            JSONObject jsonObject = new JSONObject(jsonData);
            return parseAuctionManager(jsonObject);
        } catch (IOException e) {
            return new AuctionManager();
        }
    }


    private String readFile(String source) throws IOException {
        StringBuilder contentBuilder = new StringBuilder();

        try (Stream<String> stream = Files.lines(Paths.get(source), StandardCharsets.UTF_8)) {
            stream.forEach(s -> contentBuilder.append(s));
        }

        return contentBuilder.toString();
    }


    private AuctionManager parseAuctionManager(JSONObject jsonObject) {
        AuctionManager am = new AuctionManager();
        addAuctions(am, jsonObject);
        return am;
    }


    private void addAuctions(AuctionManager am, JSONObject jsonObject) {
        JSONArray jsonArray = jsonObject.getJSONArray("auctions");
        for (Object json : jsonArray) {
            JSONObject nextAuction = (JSONObject) json;
            addAuction(am, nextAuction);
        }
    }


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
        } catch (JSONException e) {
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
