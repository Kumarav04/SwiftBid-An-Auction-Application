package model;


import java.util.ArrayList;
import java.util.List;

// Represents a user of the application with a username, password and an AuctionManager object.
public class User {
    private String userName;
    private String passWord;
    private AuctionManager listingManager;
    private ArrayList<Auction> wishList;


    // EFFECTS: Constructs a user with given username, password and initiates a listing manager
    public User(String userName, String passWord, AuctionManager listingManager) {
        this.userName = userName;
        this.passWord = passWord;
        this.listingManager = listingManager;
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
    public boolean createListing(String listingName, String description) {
        Auction newAuction = new Auction(listingName, this, description);
        listingManager.addAuction(newAuction);
        return true;
    }

    // MODIFIES: Auction
    // EFFECTS: places bid as requested by user on a specific listing.
    public boolean placeBid(Auction auction, double bid) {
        return auction.placeBid(this, bid);
    }

    public boolean addToWishList(Auction auction, List<Auction> auctions) {
        boolean wishlisted = false;
        for (Auction a : auctions) {
            if (a.equals(auction)) {
                wishList.add(auction);
                wishlisted = true;

            } else {
                wishlisted = false;
            }
        }
        return wishlisted;
    }

    public boolean removeFromWishList(Auction auction) {
        boolean removed = false;
        for (Auction a : wishList) {
            if (a.equals(auction)) {
                removed = false;
            } else {
                wishList.remove(auction);
                removed = true;
            }
        }
        return removed;
    }



    public ArrayList<Auction> getWishlist() {
        return wishList;
    }
}
