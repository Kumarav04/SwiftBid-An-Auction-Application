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
    private User currentUser;
    private AuctionManager auctionManager;
    private JTextField auctionName;
    private JTextField auctionDescription;
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private AuctionManagerJsonWriter auctionWriter;
    private JTextArea auctionTextArea;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public DeleteFrame(LoginFrame loginFrame) {
        auctionWriter = new AuctionManagerJsonWriter(AUCTION_JSON);
        AuctionManagerReader auctionReader = new AuctionManagerReader(AUCTION_JSON);
        try {
            auctionManager = auctionReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + AUCTION_JSON);
        }

        currentUser = loginFrame.getCurrentUser();

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

        getContentPane().setBackground(Color.black);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);

        removeListing();
    }

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


    // EFFECTS: Displays all the listings currently posted
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

    private void removeListing() {
        List<Auction> auctions = auctionManager.getAuctions();
        if (auctions == null || auctions.isEmpty()) {
            System.out.println("No auctions available to remove.");
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

    private void removeMechanism(String name) {
        List<Auction> auctions = auctionManager.getAuctions();
        boolean auctionRemoved = false;
        for (Auction auction : auctions) {
            if (auction.getListingName().equalsIgnoreCase(name)) {
                if (auction.getSeller().equals(currentUser.getUserName())) {
                    auctionManager.removeAuction(auction);
                    JOptionPane.showMessageDialog(this, "Auction removed successfully");
                    auctionRemoved = true;
                    break;
                } else {
                    JOptionPane.showMessageDialog(this, "Only seller can remove an auction");
                }
            }
        }
        if (!auctionRemoved) {
            JOptionPane.showMessageDialog(this, "Auction not found");

        }

        rewriteJson();
    }

    private void rewriteJson() {
        try {
            auctionWriter.open();
            auctionWriter.write(auctionManager);
            auctionWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + AUCTION_JSON);
        }
    }

    public static void main(String[] args) {
        LoginFrame loginframe = new LoginFrame();
        new DeleteFrame(loginframe);
    }
}
