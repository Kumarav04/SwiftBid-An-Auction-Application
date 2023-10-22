package model;


// Represents an Auction with a listing name, seller name, highest bid and highest bidder.
public class Auction {
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

    // REQUIRES: bid > 0
    // MODIFIES: this
    // EFFECTS: Increases highest bid if new bid is greater than existing highest bid and returns true, else false.
    public boolean placeBid(User bidder, double bid) {
        if (bid > highestBid) {
            highestBid = bid;
            highestBidder = bidder;
            return true;
        } else {
            return false;
        }
    }
}

