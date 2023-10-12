package ui;

import model.AuctionManager;
import model.User;
import model.Auction;
import model.UserManager;

import java.util.List;
import java.util.Scanner;

public class Main {
    private static final AuctionManager auctionManager = new AuctionManager();
    private static final UserManager manager = new UserManager();

    Scanner scanner = new Scanner(System.in);
    User currentUser = null;


    private void createLoginUser() {
        System.out.print("Enter Username: ");
        String userName = scanner.nextLine();
        System.out.print("Enter Password: ");
        String password = scanner.nextLine();
        currentUser = new User(userName, password, auctionManager);
        if (currentUser.createAccount(userName, password, manager)) {
            System.out.println("User Created in successfully.");
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



    private void browse() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            List<Auction> auctions = auctionManager.getAuctions();  // Retrieve auctions from auctionManager
            if (auctions.isEmpty()) {
                System.out.println("No listings available.");
            } else {
                for (Auction auction : auctions) {
                    System.out.println("Listing: " + auction.getListingName()
                            + " Seller: " + auction.getSeller()
                            + ", Current Highest Bid: " + auction.getHighestBid()
                            + ", Highest Bidder: " + (auction.getHighestBidder()
                            != null ? auction.getHighestBidder().getUserName() : "None"));
                }
            }
        }
    }

    private void postListing() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            System.out.print("Enter the name of your auction: ");
            String auctionName = scanner.nextLine();
            if (currentUser.createListing(auctionName)) {
                System.out.println("Listing Successfully created!");
            }
        }
    }

    private void auctionMechanism() {
        System.out.print("Enter the name of the auction you want to bid on: ");
        String auctionToBidOn = scanner.nextLine();
        System.out.print("Enter your bid: ");
        double bidAmount = Double.parseDouble(scanner.nextLine());
        for (Auction auction : auctionManager.getAuctions()) {
            if (auction.getListingName().equalsIgnoreCase(auctionToBidOn)) {
                if (currentUser.placeBid(auction, bidAmount)) {
                    System.out.println("Successfully Placed Bid!");
                } else {
                    System.out.println("Failed to Place Bid. Your bid must be higher "
                            + "than the current highest bid.");
                }
            } else {
                System.out.println("Listing not found");
            }
        }
    }


    private void bid() {
        if (currentUser == null) {
            System.out.println("Please create/login as a user first.");
        } else {
            List<Auction> auctions = auctionManager.getAuctions();
            if (auctions.isEmpty()) {
                System.out.println("No listings available.");
            } else {
                for (Auction auction : auctionManager.getAuctions()) {
                    System.out.println("Auction: " + auction.getListingName()
                            + ", Current Highest Bid: " + auction.getHighestBid());
                }
                auctionMechanism();
            }
        }
    }

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

                boolean auctionRemoved = false;

                for (Auction auction : auctions) {
                    if (auction.getListingName().equalsIgnoreCase(auctionNameToRemove)) {
                        auctionManager.removeAuction(auction);
                        System.out.println("Auction removed successfully.");
                        auctionRemoved = true;
                        break;
                    }
                }

                if (!auctionRemoved) {
                    System.out.println("Auction not found.");
                }
            }
        }
    }

    private void exitApp() {
        System.out.println("Exiting Application...");
        scanner.close();
        System.exit(0);
    }

    private void printMenu() {
        System.out.println("Welcome to SwiftBid.");
        System.out.println("Choose the option you desire:");
        System.out.println("1. Create/Log in as a User");
        System.out.println("2. Browse Auctions");
        System.out.println("3. Post a new Auction");
        System.out.println("4. Place a Bid");
        System.out.println("5. Delete a Listing");
        System.out.println("\nX. Exit Application");
        System.out.print("\nEnter your Option: ");
    }

    private void continuation() {
        System.out.println("Press Enter to continue...");
        scanner.nextLine();
    }

    public static void main(String[] args) {
        Main app = new Main();  // Create an instance of the Main class
        app.startApplication();  // Call a non-static method using the instance
    }


    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void startApplication() {
        Scanner scanner = new Scanner(System.in);
        try {
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
                    case "X":
                        exitApp();
                    default:
                        System.out.println("Invalid option. Please try again.");
                }
            }
        } catch (Exception ex) {
            System.out.println("An Error occurred. Please try again");
            startApplication();
        }
    }
}
