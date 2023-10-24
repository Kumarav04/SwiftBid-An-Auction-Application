package model;


import org.json.JSONException;
import org.json.JSONObject;
import persistence.UserManagerReader;
import persistence.Writable;

import java.io.IOException;
//import persistence.Writable;

// Represents an Auction with a listing name, seller name, highest bid and highest bidder.
public class Auction implements Writable {
    private String listingName;
    private User seller;
    private double highestBid;
    private User highestBidder;
    private String description;


    // EFFECTS: Constructs an Auction with given listing name, and user
    public Auction(String listingName, User seller, String description) {
        this.listingName = listingName;
        this.seller = seller;
        this.highestBid = 0.0;
        this.highestBidder = null;
        this.description = description;
    }

    // EFFECTS: returns the listing name
    public String getListingName() {
        return listingName;
    }

    // EFFECTS: returns the seller's username
    public String getSeller() {
        return seller.getUserName();
    }

    public User getSellerObject() {
        return seller;
    }

    // EFFECTS: returns the highest bid on an auction
    public double getHighestBid() {
        return highestBid;
    }

    // EFFECTS: returns the highest bidder as a User object.
    public User getHighestBidder() {
        return highestBidder;
    }

    public String getDescription() {
        return description;
    }

    public void setHighestBidder(User bidder) {
        this.highestBidder = bidder;
    }

    public void setHighestBid(double bid) {
        this.highestBid = bid;
    }

    // REQUIRES: bid > 0
    // MODIFIES: this
    // EFFECTS: Increases highest bid if new bid is greater than existing highest bid and returns true, else false.
    public boolean placeBid(User bidder, double bid) {
        if (bid > highestBid) {
            setHighestBidder(bidder);
            setHighestBid(bid);
            return true;
        } else {
            return false;
        }

    }

    public void updateWishlists(UserManager um) {
        for (User user: um.getAllUsers()) {
            user.updateWishlistAuction(this);
        }
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("listingName", listingName);
        json.put("sellerName", seller.getUserName());
        json.put("sellerPass", seller.getPassWord());
        json.put("highestBid", highestBid);
        if (!(highestBidder == null)) {
            json.put("highestBidderName", highestBidder.getUserName());
            json.put("highestBidderPass", highestBidder.getPassWord());
        } else {
            json.put("highestBidderName", JSONObject.NULL);
            json.put("highestBidderPass", JSONObject.NULL);
        }
        json.put("description", description);
        return json;
    }

    public static Auction fromJson(JSONObject jsonObject) {
        String listingName = jsonObject.getString("listingName");
        String sellerName = jsonObject.getString("sellerName");
        String sellerPass = jsonObject.getString("sellerPass");
        User seller = new User(sellerName, sellerPass);
        String bidderName = null;
        String bidderPass = null;
        try {
            bidderName = jsonObject.getString("highestBidderName");
            bidderPass = jsonObject.getString("highestBidderPass");
        } catch (JSONException e) {
            ;
        }
        double highestBid = jsonObject.getDouble("highestBid");
        User highestBidder = new User(bidderName, bidderPass);
        String description = jsonObject.getString("description");
        Auction auction = new Auction(listingName, seller, description);
        auction.setHighestBid(highestBid);
        auction.setHighestBidder(highestBidder);
        return auction;
    }

}



