package ui;

import model.Auction;
import model.AuctionManager;
import model.User;
import persistence.AuctionManagerJsonWriter;
import persistence.AuctionManagerReader;

import javax.swing.*;
import java.awt.*;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DeleteFrame extends JFrame {
    private LoginFrame loginFrame;
    private User currentUser;
    private AuctionManager auctionManager;
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private AuctionManagerJsonWriter auctionWriter;
    private JTextArea auctionTextArea;

    // EFFECTS: Creates a new frame allowing users to delete listings posted by them.
    public DeleteFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        auctionWriter = new AuctionManagerJsonWriter(AUCTION_JSON);
        AuctionManagerReader auctionReader = new AuctionManagerReader(AUCTION_JSON);
        try {
            auctionManager = auctionReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + AUCTION_JSON);
        }

        currentUser = loginFrame.getCurrentUser();

        setFrameElements();

        getContentPane().setBackground(Color.black);
        setResizable(false);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);

        removeListing();
    }

    // MODIFIES: this
    // EFFECTS: sets scroll plane and title for the frame.
    private void setFrameElements() {
        auctionTextArea = new JTextArea();
        browseDelete();
        JScrollPane scrollPane = new JScrollPane(auctionTextArea);
        scrollPane.setBounds(20, 100, 360, 400);
        auctionTextArea.setForeground(Color.WHITE);
        auctionTextArea.setBackground(Color.black);
        auctionTextArea.setFont(new Font("Verdana", Font.PLAIN, 16));
        add(scrollPane);

        JLabel label = new JLabel("Your listings");
        label.setBounds(120, 50, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);
    }

    // EFFECTS: Displays a single auction with appropriate labels from a list of auctions
    private void auctionDisplayer(List<Auction> delAuctions) {
        StringBuilder auctionInfo = new StringBuilder();
        for (Auction auction : delAuctions) {
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


    // EFFECTS: Displays all the listings currently posted by the current user.
    private void browseDelete() {
        List<Auction> auctions = auctionManager.getAuctions();
        List<Auction> delAuction = new ArrayList<>();
        for (Auction auction : auctions) {
            if (auction.getSellerObject().getUserName().equals(currentUser.getUserName())) {
                delAuction.add(auction);
            }
        }
        if (delAuction.isEmpty() || auctions.isEmpty()) {
            auctionTextArea.setText("No listings available.");
        } else {
            auctionDisplayer(delAuction);
        }
    }

    // MODIFIES: auctionManager
    // EFFECTS: removes a listing from list of all auctions only if the seller tries to remove it.
    private void removeListing() {
        List<Auction> auctions = auctionManager.getAuctions();
        if (auctions == null || auctions.isEmpty()) {
            JOptionPane.showMessageDialog(this, "No Auction available to remove");
            dispose();
        } else {

            String auctionNameToRemove = JOptionPane.showInputDialog(this, "Enter the name of the "
                    + " auction you would like to remove");
            if (auctionNameToRemove != null) {
                removeMechanism(auctionNameToRemove);
            } else {
                dispose();
            }
        }

    }

    // MODIFIES: auctionManager
    // EFFECTS: removes an auction from the current listings
    private void removeMechanism(String name) {
        List<Auction> auctions = auctionManager.getAuctions();
        Auction auctionToRemove = null;

        for (Auction auction : auctions) {
            if (auction.getListingName().equalsIgnoreCase(name)) {
                if (auction.getSeller().equals(currentUser.getUserName())) {
                    auctionToRemove = auction;
                    break;  // Found the auction to remove, exit the loop
                } else {
                    JOptionPane.showMessageDialog(this, "Only seller can remove an auction");
                    return;  // Exit the method, as we don't want to continue checking other auctions
                }
            }
        }

        if (auctionToRemove != null) {
            auctionManager.removeAuction(auctionToRemove);
            JOptionPane.showMessageDialog(this, "Auction removed successfully");
            rewriteJson();
            dispose();
            new DeleteFrame(loginFrame);
        } else {
            JOptionPane.showMessageDialog(this, "Auction not found");
        }
    }


    // EFFECTS: Writes updated auctionManager into JSON file
    private void rewriteJson() {
        try {
            auctionWriter.open();
            auctionWriter.write(auctionManager);
            auctionWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + AUCTION_JSON);
        }
    }

}
