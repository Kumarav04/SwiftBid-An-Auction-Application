package ui;

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

public class NewAuctionFrame extends JFrame {
    private LoginFrame loginFrame;
    private User currentUser;
    private AuctionManager auctionManager;
    private JTextField auctionName;
    private JTextField auctionDescription;
    private static final String AUCTION_JSON = "./data/auctionmanager.json";
    private static final String USER_JSON = "./data/usermanager.json";
    private UserManager manager;
    private AuctionManagerJsonWriter auctionWriter;
    private AuctionManagerReader auctionReader;
    private UserManagerJsonWriter userWriter;
    private UserManagerReader userReader;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public NewAuctionFrame(LoginFrame loginFrame) {
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

        this.loginFrame = loginFrame;
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
        nameLabel.setBounds(50, 300, 80, 25);
        passwordLabel.setBounds(55, 330, 80, 25);
        auctionName.setBounds(140, 300, 165, 25);
        auctionDescription.setBounds(140, 330, 165, 25);

        // Set foreground color for login components
        nameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

        //  create button
        JButton button = new JButton("Post Auction");

        button.setBounds(140, 400, 100, 20);
        button.addActionListener(e -> postListing());

        // adding button on frame
        add(nameLabel);
        add(passwordLabel);
        add(auctionName);
        add(auctionDescription);
        add(button);
    }

    private void postListing() {
        String nameofAuction = new String(auctionName.getText());
        String itemDesc = new String(auctionDescription.getText());
        if (currentUser.createListing(nameofAuction, itemDesc, auctionManager)) {
            JOptionPane.showMessageDialog(this, "Auction Posted Successfully!");
        }

        try {
            auctionWriter.open();
            auctionWriter.write(auctionManager);
            auctionWriter.close();
        } catch (FileNotFoundException e) {
            System.out.println("Unable to write to file: " + AUCTION_JSON);
        }

    }




}
