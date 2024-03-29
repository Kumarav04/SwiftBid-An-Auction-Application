package model;


import org.json.JSONArray;
import org.json.JSONObject;
import persistence.Writable;

import java.util.ArrayList;
import java.util.List;

// Represents a user of the application with a username, password and an AuctionManager object.
public class User implements Writable {
    private String userName;
    private String passWord;
    private ArrayList<Auction> wishList;


    // EFFECTS: Constructs a user with given username, password and initiates a listing manager
    public User(String userName, String passWord) {
        this.userName = userName;
        this.passWord = passWord;
        this.wishList = new ArrayList<>();

    }

    // MODIFIES: this
    // EFFECTS: Creates an account for the user by  checking if username already exists
    public boolean createAccount(String name, String pass, UserManager userManager) {
        if (userManager.isUsernameTaken(name)) {
            return false;
        } else {
            this.userName = name;
            this.passWord = pass;
            userManager.addUser(this);
            EventLog.getInstance().logEvent(new Event("New user created! Username: " + this.userName));
            return true;
        }
    }


    // EFFECTS: returns the username of a user.
    public String getUserName() {
        return userName;
    }

    public String getPassWord() {
        return passWord;

    }


    // MODIFIES: this, AuctionManager
    // EFFECTS: Creates a new listing by a user and adds it to AuctionManager
    public boolean createListing(String listingName, String description, AuctionManager listingManager) {
        Auction newAuction = new Auction(listingName, this, description);
        listingManager.addAuction(newAuction);
        EventLog.getInstance().logEvent(new Event("New Auction Posted! Auction name: " + listingName));
        return true;
    }

    // MODIFIES: Auction
    // EFFECTS: places bid as requested by user on a specific listing.
    public boolean placeBid(Auction auction, double bid) {
        return auction.placeBid(this, bid);

    }

    // MODIFIES: this
    // EFFECTS: adds auction to wishlist
    public boolean addToWishList(Auction auction, List<Auction> auctions) {
        boolean wishlisted = false;
        for (Auction a : auctions) {
            if (a.equals(auction)) {
                boolean alreadyWishlisted = false;
                for (Auction aw : wishList) {
                    if (auction.getListingName().equalsIgnoreCase(aw.getListingName())) {
                        alreadyWishlisted = true;
                        break;
                    }
                }
                if (!alreadyWishlisted) {
                    wishList.add(auction);
                    EventLog.getInstance().logEvent(new Event("Auction " + auction.getListingName() + " added "
                            + "to user's wishlist!"));
                    wishlisted = true;
                }
            }
        }
        return wishlisted;
    }


    // MODIFIES: this
    // EFFECTS: removes auction from wishlist
    public boolean removeFromWishList(Auction auction) {
        List<Auction> itemsToRemove = new ArrayList<>();

        for (Auction a : wishList) {
            if (a.equals(auction)) {
                itemsToRemove.add(auction);
            }
        }

        boolean removed = !itemsToRemove.isEmpty();

        if (removed) {
            wishList.removeAll(itemsToRemove);
            EventLog.getInstance().logEvent(new Event("Auction " + auction.getListingName() + " removed "
                    + "from user's wishlist!"));
        }

        return removed;
    }

    // MODIFIES: this
    // EFFECTS: updates Auction with new details in the wishlist
    public boolean updateWishlistAuction(Auction updatedAuction) {
        for (Auction a : wishList) {
            if (a.getListingName().equals(updatedAuction.getListingName())) {
                a.setHighestBid(updatedAuction.getHighestBid());
                a.setHighestBidder(updatedAuction.getHighestBidder());
                return true;
            }
        }
        return false;
    }

    public void setWishList(ArrayList a) {
        wishList = a;
    }

    public ArrayList getWishlist() {
        return wishList;
    }


    // EFFECTS: converts User object to JSON format
    @Override
    public JSONObject toJson() {
        JSONObject json = new JSONObject();
        json.put("userName", userName);
        json.put("passWord", passWord);
        JSONArray wishlistArray = new JSONArray();
        for (Auction a : wishList) {
            wishlistArray.put(a.toJson());
        }
        json.put("wishList", wishlistArray);
        return json;
    }

    // EFFECTS: Retrieves User object from JSON format
    public User fromUserJson(JSONObject jsonObject) {
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
        return user;
    }
}
