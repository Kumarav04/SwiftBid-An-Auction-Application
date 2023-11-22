package ui;

import model.Auction;
import model.AuctionManager;
import model.User;
import model.UserManager;
import persistence.AuctionManagerJsonWriter;
import persistence.AuctionManagerReader;
import persistence.UserManagerJsonWriter;
import persistence.UserManagerReader;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

// Auction platform application
public class SwiftBidApp {
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private static final String USER_JSON = "./data/usermanager.json";
    private AuctionManager auctionManager;
    private UserManager manager;
    private AuctionManagerJsonWriter auctionWriter;
    private AuctionManagerReader auctionReader;
    private UserManagerJsonWriter userWriter;
    private UserManagerReader userReader;

    Scanner scanner = new Scanner(System.in);
    User currentUser = null;

    // EFFECTS: Runs the SwiftBid Application
    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public SwiftBidApp() {
        auctionWriter = new AuctionManagerJsonWriter(AUCTION_JSON);
        auctionReader = new AuctionManagerReader(AUCTION_JSON);
        userWriter = new UserManagerJsonWriter(USER_JSON);
        userReader = new UserManagerReader(USER_JSON);
        try {
            auctionManager = auctionReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + AUCTION_JSON);
        }
        try {
            manager = userReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + USER_JSON);
        }

        Scanner scanner = new Scanner(System.in);
        while (true) {
            printMenu();
            String option = scanner.nextLine();
            switch (option) {
                case "1":
                    createLoginUser();
                    continuation();
                    break;
                case "2":
                    browse();
                    continuation();
                    break;
                case "3":
                    postListing();
                    break;
                case "4":
                    bid();
                    continuation();
                    break;
                case "5":
                    removeListing();
                    continuation();
                    break;
                case "6":
                    wishlists();
                    continuation();
                    break;
                case "X":
                    exitApp();
                default:
                    System.out.println("Invalid option. Please try again.");
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Creates a user if username does not exist, authenticates user if username exists.
    private void createLoginUser() {
        System.out.print("Enter Username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        currentUser = new User(userName, password);
        if (currentUser.createAccount(userName, password, manager)) {
            System.out.println("User Created successfully.");
        } else {
            if (manager.authenticate(userName, password)) {
                currentUser = manager.getUser(userName);
                System.out.println("User Logged in successfully.");
            } else {
                System.out.println("User Already exists. Authentication error. Try again.");
                System.out.println("Press X to exit application");
                String exit = scanner.nextLine();
                if (exit.equals("X")) {
                    exitApp();
                } else {
                    createLoginUser();
                }

            }
        }
    }

    private void auctionDisplayer(List<Auction> auctions) {
        for (Auction auction : auctions) {
            System.out.println("Listing: " + auction.getListingName()
                    + "\nSeller: " + auction.getSeller()
                    + "\nDescription: " + auction.getDescription()
                    + "\nCurrent Highest Bid: " + auction.getHighestBid()
                    + "\nHighest Bidder: " + ((auction.getHighestBidder())
                    != null ? auction.getHighestBidder().getUserName() : "None") + "\n");
        }
    }


    // EFFECTS: Displays all the listings currently posted
    private void browse() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            List<Auction> auctions = auctionManager.getAuctions();
            if (auctions.isEmpty()) {
                System.out.println("No listings available.");
            } else {
                auctionDisplayer(auctions);
                wishlisting(auctions);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: Adds an item to currentUser's wishlist
    private void wishlisting(List<Auction> auctions) {
        System.out.println("Enter \"Wishlist\" to save an auction to your"
                + " wish list, else press enter to continue");
        System.out.print("Enter input: ");
        String wishlist = scanner.nextLine();
        if (wishlist.equalsIgnoreCase("wishlist")) {
            System.out.println("Enter the name of the auction you would like to add to your wish list");
            String wishlistName = scanner.nextLine();
            boolean auctionFound = false;
            for (Auction auction : auctions) {
                if (auction.getListingName().equalsIgnoreCase(wishlistName)) {
                    if (currentUser.addToWishList(auction, auctions)) {
                        System.out.println("Item added to Wishlist successfully!");
                    } else {
                        System.out.println("Item already exists in wishlist");
                    }
                    auctionFound = true;
                    break;

                }
            }
            if (!auctionFound) {
                System.out.println("Auction not found");
            }
        }
    }


    // MODIFIES: this
    // EFFECTS: Allows user to view and delete items on their wishlist
    private void wishlists() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            List<Auction> wishlist = currentUser.getWishlist();
            auctionDisplayer(wishlist);
            System.out.println("Press D if you would like to delete an item from your wish list.");
            String delete = scanner.nextLine();
            if (delete.equalsIgnoreCase("D")) {
                System.out.println("Enter the name of the auction you would like to delete from your wish list");
                String wishlistName = scanner.nextLine();
                List<Auction> itemsToRemove = new ArrayList<>();
                wishlistsHelper(wishlist, wishlistName, itemsToRemove);
            }
        }
    }

    private void wishlistsHelper(List<Auction> wishlist, String wishlistName, List<Auction> itemsToRemove) {
        for (Auction auction : wishlist) {
            if (auction.getListingName().equalsIgnoreCase(wishlistName)) {
                itemsToRemove.add(auction);
            }
        }
        if (!itemsToRemove.isEmpty()) {
            for (Auction auction : itemsToRemove) {
                currentUser.removeFromWishList(auction);
                System.out.println("Item removed from Wishlist successfully!");
            }
        } else {
            System.out.println("Auction not found");
        }
    }


    // MODIFIES: this
    // EFFECTS: Creates a new listing from currentUser
    private void postListing() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            System.out.print("Enter the name of your auction: ");
            String auctionName = scanner.nextLine();
            System.out.print("Enter the description of your auction: ");
            String itemDesc = scanner.nextLine();
            if (currentUser.createListing(auctionName, itemDesc, auctionManager)) {
                System.out.println("Listing Successfully created!");
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: handles auction and bidding mechanism
    private void auctionMechanism(List<Auction> auctions) {
        System.out.print("Enter the name of the auction you want to bid on: ");
        String auctionToBidOn = scanner.nextLine();
        System.out.print("Enter your bid: ");
        double bidAmount = Double.parseDouble(scanner.nextLine());
        boolean foundAuction = false;
        for (Auction auction : auctions) {
            if (auction.getListingName().equalsIgnoreCase(auctionToBidOn)) {
                if (auction.getSeller().equals(currentUser.getUserName())) {
                    System.out.println("Seller cannot place bids on their own listings.");
                } else {
                    verifyBid(bidAmount, auction);
                    foundAuction = true;
                    break;
                }
            }
        }
        if (!foundAuction) {
            System.out.println("Listing not found");
        }
    }

    // MODIFIES: this
    // EFFECTS: verifies if bid is valid- if new bid is greater than current bid
    private void verifyBid(double bidAmount, Auction auction) {
        if (currentUser.placeBid(auction, bidAmount)) {
            auction.updateWishlists(manager);
            System.out.println("Successfully Placed Bid!");
        } else {
            System.out.println("Failed to Place Bid. Your bid must be higher "
                    + "than the current highest bid.");
        }
    }


    // MODIFIES: this
    // EFFECTS: places a bid from currentUser
    private void bid() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            List<Auction> auctions = auctionManager.getAuctions();
            if (auctions.isEmpty()) {
                System.out.println("No listings available.");
            } else {
                for (Auction auction : auctions) {
                    System.out.println("Auction: " + auction.getListingName()
                            + "\nDescription: " + auction.getDescription()
                            + "\nCurrent Highest Bid: " + auction.getHighestBid());
                }
                auctionMechanism(auctions);
            }
        }
    }

    // MODIFIES: this
    // EFFECTS: removes an auction from the current listings
    private void removeMechanism(String name) {
        List<Auction> auctions = auctionManager.getAuctions();
        boolean auctionRemoved = false;
        for (Auction auction : auctions) {
            if (auction.getListingName().equalsIgnoreCase(name)) {
                if (auction.getSeller().equals(currentUser.getUserName())) {
                    auctionManager.removeAuction(auction);
                    System.out.println("Auction removed successfully.");
                    auctionRemoved = true;
                    break;
                } else {
                    System.out.println("Only seller can remove an auction");
                }
            }
        }
        if (!auctionRemoved) {
            System.out.println("Listing not found");

        }
    }


    // MODIFIES: this
    // EFFECTS: removes a listing from list of all auctions only if the seller tries to remove it.
    private void removeListing() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            List<Auction> auctions = auctionManager.getAuctions();
            if (auctions == null || auctions.isEmpty()) {
                System.out.println("No auctions available to remove.");
            } else {

                System.out.print("Enter the name of the auction you want to remove: ");
                String auctionNameToRemove = scanner.nextLine();
                removeMechanism(auctionNameToRemove);
            }
        }
    }


    // EFFECTS: Quits application, gives user the choice to save user details
    private void exitApp() {
        try {
            auctionWriter.open();
            auctionWriter.write(auctionManager);
            auctionWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + AUCTION_JSON);
        }
        System.out.println("Would you like to save your Account and Wishlist before exiting?(Y/N)");
        String choice = scanner.nextLine();
        if (choice.equalsIgnoreCase("Y")) {
            try {
                userWriter.open();
                userWriter.write(manager);
                userWriter.close();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + USER_JSON);
            }
        }
        System.out.println("Exiting Application...");
        scanner.close();
        System.exit(0);
    }

    // EFFECTS: Prints menu of user choices
    private void printMenu() {
        System.out.println("Welcome to SwiftBid.");
        System.out.println("Choose the option you desire:");
        System.out.println("1. Create/Log in as a User");
        System.out.println("2. Browse Auctions");
        System.out.println("3. Post a new Auction");
        System.out.println("4. Place a Bid");
        System.out.println("5. Delete a Listing");
        System.out.println("6. View Wishlist");
        System.out.println("\nX. Exit Application");
        System.out.print("\nEnter your Option: ");
    }

    // EFFECTS: provides flow in application by prompting user to press enter.
    private void continuation() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }
}



