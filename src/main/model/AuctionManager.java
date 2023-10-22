package model;

import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;
//import persistence.Writable;

import java.util.ArrayList;
import java.util.List;


// Handles a list of all Auctions currently listed in the platform
public class AuctionManager implements Writable {
    private List<Auction> auctions;

    //EFFECTS: Constructs a new auction manager as an ArrayList.
    public AuctionManager() {
        this.auctions = new ArrayList<>();
    }

    //MODIFIES: this
    //EFFECTS: adds Auction into list of auctions handled by Auction Manager
    public void addAuction(Auction auction) {
        auctions.add(auction);
    }

    // MODIFIES: this
    // EFFECTS: removes an Auction from the list of existing auctions
    public void removeAuction(Auction auction) {
        auctions.remove(auction);
    }


    //EFFECTS: returns a list of all auctions currently posted.
    public List<Auction> getAuctions() {
        return auctions;
    }

    public AuctionManager getAuctionManager() {
        return this;
    }

    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("auctions", auctionsToJson());
        return json;
    }

    // EFFECTS: returns things in this workroom as a JSON array
    private JSONArray auctionsToJson() {
        JSONArray jsonArray = new JSONArray();

        for (Auction t : auctions) {
            jsonArray.put(t.toJson());
        }

        return jsonArray;
    }

}