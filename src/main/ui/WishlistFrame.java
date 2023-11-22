package ui;

import model.Auction;
import model.AuctionManager;
import model.User;
import model.UserManager;
import persistence.AuctionManagerJsonWriter;
import persistence.AuctionManagerReader;
import persistence.UserManagerJsonWriter;
import persistence.UserManagerReader;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class WishlistFrame extends JFrame {
    private LoginFrame loginFrame;
    private UserManager manager;
    private User currentUser;
    private static final String USER_JSON = "./data/usermanager.json";
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private AuctionManager auctionManager;
    private UserManagerReader userReader;
    private UserManagerJsonWriter userWriter;
    private AuctionManagerReader auctionReader;
    private AuctionManagerJsonWriter auctionWriter;
    private JTextArea auctionTextArea;

    // EFFECTS: Creates a new frame for users' wish list
    public WishlistFrame(LoginFrame loginFrame) {
        currentUser = loginFrame.getCurrentUser();
        userReader = new UserManagerReader(USER_JSON);
        userWriter = new UserManagerJsonWriter(USER_JSON);
        auctionReader = new AuctionManagerReader(AUCTION_JSON);
        auctionWriter = new AuctionManagerJsonWriter(AUCTION_JSON);

        manager = loginFrame.getManager();

        try {
            auctionManager = auctionReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + AUCTION_JSON);
        }

        setFrameElements();

    }

    // MODIFIES: this
    // EFFECTS: sets all the frame elements in the frame
    private void setFrameElements() {
        auctionTextArea = new JTextArea();
        browseWishlist();
        JScrollPane scrollPane = new JScrollPane(auctionTextArea);
        scrollPane.setBounds(20, 100, 360, 400);
        auctionTextArea.setForeground(Color.WHITE);
        auctionTextArea.setBackground(Color.black);
        auctionTextArea.setFont(new Font("Verdana", Font.PLAIN, 16));
        add(scrollPane);

        JLabel label = new JLabel("Your Wishlist");
        label.setBounds(120, 50, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);

        JButton wishlistButton = new JButton("Delete item from Wishlist");
        wishlistButton.setBounds(150, 520, 210, 20);
        wishlistButton.addActionListener(e -> wishlistDeleting());
        add(wishlistButton);

        getContentPane().setBackground(Color.black);
        setResizable(false);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);
    }

    // EFFECTS: Displays a single auction with appropriate labels from a list of auctions
    private void auctionDisplayer(java.util.List<Auction> auctions) {
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


    // EFFECTS: Displays all the listings currently in the waitlists
    private void browseWishlist() {
        List<Auction> wishlist = currentUser.getWishlist();
        if (wishlist.isEmpty()) {
            auctionTextArea.setText("No listings available.");
        } else {
            auctionDisplayer(wishlist);
        }
    }

    // EFFECTS: Deletes a listing from the user's wish list
    private void wishlistDeleting() {
        List<Auction> wishlist = currentUser.getWishlist();
        String wishlistName = JOptionPane.showInputDialog(this, "Enter the name of the auction"
                + " you would like to delete from your wish list");

        List<Auction> itemsToRemove = new ArrayList<>();
        wishlistsHelper(wishlist, wishlistName, itemsToRemove);


    }

    // MODIFIES: currentUser, manager
    // EFFECTS: Handles the deleting mechanism and wites updated auctionManager into JSON file
    private void wishlistsHelper(List<Auction> wishlist, String wishlistName, List<Auction> itemsToRemove) {
        for (Auction auction : wishlist) {
            if (auction.getListingName().equalsIgnoreCase(wishlistName)) {
                itemsToRemove.add(auction);
            }
        }
        if (!itemsToRemove.isEmpty()) {
            for (Auction auction : itemsToRemove) {
                currentUser.removeFromWishList(auction);
                JOptionPane.showMessageDialog(this, "Item removed from Wishlist successfully!");
                try {
                    auctionWriter.open();
                    auctionWriter.write(auctionManager);
                    auctionWriter.close();
                } catch (FileNotFoundException e) {
                    System.out.println("Unable to write to file: " + AUCTION_JSON);
                }
            }
        } else {
            JOptionPane.showMessageDialog(this, "Auction not found");

        }
    }

}
