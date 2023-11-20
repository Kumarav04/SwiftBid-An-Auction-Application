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

public class NewAuctionFrame extends JFrame {
    private User currentUser;
    private AuctionManager auctionManager;
    private JTextField auctionName;
    private JTextField auctionDescription;
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private AuctionManagerJsonWriter auctionWriter;

    public NewAuctionFrame(LoginFrame loginFrame) {
        auctionWriter = new AuctionManagerJsonWriter(AUCTION_JSON);
        AuctionManagerReader auctionReader = new AuctionManagerReader(AUCTION_JSON);
        try {
            auctionManager = auctionReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + AUCTION_JSON);
        }

        currentUser = loginFrame.getCurrentUser();

        JLabel label = new JLabel("Post New Auction");
        label.setBounds(120, 50, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);

        postAuction();

        getContentPane().setBackground(Color.black);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);

    }

    private void postAuction() {
        JLabel nameLabel = new JLabel("Name of Auction: ", JLabel.RIGHT);
        JLabel passwordLabel = new JLabel("Description: ", JLabel.CENTER);
        auctionName = new JTextField(6);
        auctionDescription = new JTextField(6);
        nameLabel.setBounds(20, 300, 120, 25);
        passwordLabel.setBounds(55, 330, 80, 25);
        auctionName.setBounds(140, 300, 165, 25);
        auctionDescription.setBounds(140, 330, 165, 25);

        // Set foreground color for login components
        nameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

        //  create button
        JButton button = new JButton("Post Auction");

        button.setBounds(100, 400, 200, 20);
        button.addActionListener(e -> postListing());

        // adding button on frame
        add(nameLabel);
        add(passwordLabel);
        add(auctionName);
        add(auctionDescription);
        add(button);
    }

    private void postListing() {
        boolean postStatus = true;
        String nameofAuction = auctionName.getText();
        String itemDesc = auctionDescription.getText();
        for (Auction a : auctionManager.getAuctions()) {
            if (a.getListingName().equalsIgnoreCase(nameofAuction)) {
                JOptionPane.showMessageDialog(this, "An auction with the same name already exists."
                        + "Please use a different name");
                postStatus = false;
                dispose();
            }
        }
        if (postStatus) {
            if (currentUser.createListing(nameofAuction, itemDesc, auctionManager)) {
                JOptionPane.showMessageDialog(this, "Auction Posted Successfully!");
                dispose();
            }
        }

        saveChanges();

    }

    private void saveChanges() {
        try {
            auctionWriter.open();
            auctionWriter.write(auctionManager);
            auctionWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + AUCTION_JSON);
        }
    }


}
