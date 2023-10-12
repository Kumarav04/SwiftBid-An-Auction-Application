package model;


// Represents a user of the application
public class User {
    private String userName;
    private String passWord;
    private AuctionManager listingManager;

    // EFFECTS: Constructs a user with given username, password and initiates a listing manager
    public User(String userName, String passWord, AuctionManager listingManager) {
        this.userName = userName;
        this.passWord = passWord;
        this.listingManager = listingManager;
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
    public boolean createListing(String listingName) {
        Auction newAuction = new Auction(listingName, this);
        listingManager.addAuction(newAuction);
        return true;
    }

    // MODIFIES: Auction
    // EFFECTS: places bid as requested by user on a specific listing.
    public boolean placeBid(Auction auction, double bid) {
        return auction.placeBid(this, bid);
    }
}