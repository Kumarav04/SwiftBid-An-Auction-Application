package ui;

import model.Auction;
import model.AuctionManager;
import model.UserManager;
import model.User;
import persistence.AuctionManagerJsonWriter;
import persistence.AuctionManagerReader;


import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

public class BrowseFrame extends JFrame {
    private LoginFrame loginFrame;
    private UserManager manager;
    private User currentUser;
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private AuctionManager auctionManager;
    private AuctionManagerJsonWriter auctionWriter;
    private JTextArea auctionTextArea;

    // EFFECTS: Creates a new frame with a scroll pane for the user to browse through currently active listings
    public BrowseFrame(LoginFrame loginFrame) {
        currentUser = loginFrame.getCurrentUser();
        AuctionManagerReader auctionReader = new AuctionManagerReader(AUCTION_JSON);
        auctionWriter = new AuctionManagerJsonWriter(AUCTION_JSON);
        try {
            auctionManager = auctionReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + AUCTION_JSON);
        }

        auctionTextArea = new JTextArea();
        browse();
        JScrollPane scrollPane = new JScrollPane(auctionTextArea);
        scrollPane.setBounds(20, 100, 360, 400);
        auctionTextArea.setForeground(Color.WHITE);
        auctionTextArea.setBackground(Color.black);
        auctionTextArea.setFont(new Font("Verdana", Font.PLAIN, 16));
        add(scrollPane);

        manager = loginFrame.getManager();

        setFrameElements();

        getContentPane().setBackground(Color.black);
        setResizable(false);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: Sets title and buttons on the frame
    private void setFrameElements() {

        JLabel label = new JLabel("Current Listings");
        label.setBounds(120, 50, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);

        JButton bidButton = new JButton("Bid on a listing");
        bidButton.setBounds(220, 520, 130, 20);
        bidButton.addActionListener(e -> auctionMechanism(auctionManager.getAuctions()));
        add(bidButton);

        JButton wishButton = new JButton("Add to wishlist");
        wishButton.setBounds(30, 520, 130, 20);
        wishButton.addActionListener(e -> wishlisting(auctionManager.getAuctions()));
        add(wishButton);
    }

    // EFFECTS: Displays a single auction with appropriate labels from a list of auctions
    private void auctionDisplayer(List<Auction> auctions) {
        StringBuilder auctionInfo = new StringBuilder();
        for (Auction auction : auctions) {
            auctionInfo.append("Listing: ").append(auction.getListingName())
                    .append("\nSeller: ").append(auction.getSeller())
                    .append("\nDescription: ").append(auction.getDescription())
                    .append("\nCurrent Highest Bid: $").append(auction.getHighestBid())
                    .append("\nHighest Bidder: ").append((auction.getHighestBidder() != null
                            ? auction.getHighestBidder().getUserName() : "None"))
                    .append("\n\n");
        }
        auctionTextArea.setText(auctionInfo.toString());
    }


    // EFFECTS: Displays all the listings currently posted
    private void browse() {
        List<Auction> auctions = auctionManager.getAuctions();
        if (auctions.isEmpty()) {
            auctionTextArea.setText("No listings available.");
        } else {
            auctionDisplayer(auctions);
        }
    }

    // MODIFIES: currentUser
    // EFFECTS: Adds an item to currentUser's wishlist if not already added.
    private void wishlisting(List<Auction> auctions) {

        String wishlistName = JOptionPane.showInputDialog(this, "Enter the name of the "
                + " auction you would like to add to your wish list");

        if (wishlistName != null) {
            if (!wishlistName.trim().isEmpty()) {
                boolean auctionFound = false;

                for (Auction auction : auctions) {
                    if (auction.getListingName().equalsIgnoreCase(wishlistName.trim())) {
                        if (currentUser.addToWishList(auction, auctions)) {
                            JOptionPane.showMessageDialog(this, "Item added to Wishlist successfully!");
                        } else {
                            JOptionPane.showMessageDialog(this, "Item already exists in wishlist");
                        }
                        auctionFound = true;
                        break;
                    }
                }

                if (!auctionFound) {
                    JOptionPane.showMessageDialog(this, "Auction not found");
                }
            } else {
                JOptionPane.showMessageDialog(this, "Invalid input. Auction name cannot be empty.");
            }
        }

    }

    // EFFECTS: handles auction and bidding mechanism
    private void auctionMechanism(List<Auction> auctions) {
        String auctionToBidOn = JOptionPane.showInputDialog(this, "Enter the name of the auction"
                + " you would like to bid on");
        boolean auctionFound = false;

        for (Auction a : auctions) {
            if (a.getListingName().equalsIgnoreCase(auctionToBidOn)) {
                String bidAmountString = JOptionPane.showInputDialog(this, "Enter your bid");
                double bidAmount = Double.parseDouble(bidAmountString);

                if (a.getSeller().equals(currentUser.getUserName())) {
                    JOptionPane.showMessageDialog(this, "Seller cannot place bids on their own listings");
                } else {
                    verifyBid(bidAmount, a);
                }

                auctionFound = true;
                break;
            }
        }

        if (!auctionFound) {
            JOptionPane.showMessageDialog(this, "Listing not found");
        }
    }


    // MODIFIES: currentUser, manager, auctionManager
    // EFFECTS: verifies if bid is valid- if new bid is greater than current bid
    private void verifyBid(double bidAmount, Auction auction) {
        if (currentUser.placeBid(auction, bidAmount)) {
            auction.updateWishlists(manager);
            JOptionPane.showMessageDialog(this, "Successfully placed bid!");

            try {
                auctionWriter.open();
                auctionWriter.write(auctionManager);
                auctionWriter.close();
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + AUCTION_JSON);
            }

        } else {
            JOptionPane.showMessageDialog(this, "Failed to Place Bid. Your bid must be higher "
                    + "than the current highest bid.");
        }
    }


}
