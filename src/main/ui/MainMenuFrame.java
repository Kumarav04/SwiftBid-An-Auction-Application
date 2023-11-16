package ui;

import javax.swing.*;

import ui.LoginFrame;

import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;


public class MainMenuFrame extends JFrame {
    private LoginFrame loginFrame;

    @SuppressWarnings({"checkstyle:MethodLength", "checkstyle:SuppressWarnings"})
    public MainMenuFrame(LoginFrame loginFrame) {
        this.loginFrame = loginFrame;
        JLabel label = new JLabel("Welcome, " + loginFrame.getCurrentUser().getUserName());
        label.setBounds(120, 50, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                new ExitFrame(loginFrame);
            }
        });

        JButton browseButton = new JButton("Browse Auctions");
        browseButton.setBounds(50, 100, 300, 20);
        browseButton.addActionListener(e -> new BrowseFrame(loginFrame));
        dispose();
        add(browseButton);

        JButton postButton = new JButton("Post a new Auction");
        postButton.setBounds(50, 150, 300, 20);
        postButton.addActionListener(e -> new NewAuctionFrame(loginFrame));
        add(postButton);

        JButton deleteButton = new JButton("Delete a Listing");
        deleteButton.setBounds(50, 200, 300, 20);
        deleteButton.addActionListener(e -> new DeleteFrame(loginFrame));
        add(deleteButton);

        JButton wishlistButton = new JButton("View your wishlist");
        wishlistButton.setBounds(50, 250, 300, 20);
        // button.addActionListener(e -> createLoginUser());
        add(wishlistButton);

        JButton exitButton = new JButton("Exit Application");
        exitButton.setBounds(50, 400, 300, 20);
        exitButton.addActionListener(e -> new ExitFrame(loginFrame));
        add(exitButton);

        getContentPane().setBackground(Color.black);
        setSize(400, 600);
        setLayout(null);
        setVisible(true);


    }

//    public static void main(String[] args) {
//        LoginFrame loginframe = new LoginFrame();
//        new MainMenuFrame(loginframe);
//    }


}
