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
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class BrowseFrame extends JFrame {
    private LoginFrame loginFrame;
    private User currentUser;
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private static final String USER_JSON = "./data/usermanager.json";
    private AuctionManager auctionManager;
    private UserManager manager;
    private AuctionManagerJsonWriter auctionWriter;
    private AuctionManagerReader auctionReader;
    private UserManagerJsonWriter userWriter;
    private UserManagerReader userReader;
    private JTextArea auctionTextArea;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public BrowseFrame(LoginFrame loginFrame) {

        currentUser = loginFrame.getCurrentUser();
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


//        JScrollBar s = new JScrollBar();
//        s.setBounds(365,0, 20,560);
//        add(s);

        auctionTextArea = new JTextArea();
        browse();
        JScrollPane scrollPane = new JScrollPane(auctionTextArea);
        scrollPane.setBounds(20, 100, 360, 400);
        auctionTextArea.setForeground(Color.WHITE);
        auctionTextArea.setBackground(Color.black);
        auctionTextArea.setFont(new Font("Verdana", Font.PLAIN, 16));
        add(scrollPane);

        JLabel label = new JLabel("Current Listings");
        label.setBounds(120, 50, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);

        JButton bidButton = new JButton("Bid on a listing");
        bidButton.setBounds(220, 520, 130, 20);
        // button.addActionListener(e -> createLoginUser());
        add(bidButton);

        JButton wishButton = new JButton("Add to wishlist");
        wishButton.setBounds(30, 520, 130, 20);
        wishButton.addActionListener(e -> wishlisting(auctionManager.getAuctions()));
        add(wishButton);

        getContentPane().setBackground(Color.black);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);
    }

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

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    private void wishlisting(List<Auction> auctions) {

        String wishlistName = JOptionPane.showInputDialog(this, "Enter the name of the "
                + " auction you would like to add to your wish list");

        if (wishlistName != null && !wishlistName.trim().isEmpty()) {
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

//    public static void main(String[] args) {
//        LoginFrame loginframe = new LoginFrame();
//        new BrowseFrame(loginframe);
//    }
}
