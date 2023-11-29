package ui;

import model.User;
import model.UserManager;
import persistence.UserManagerReader;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class LoginFrame extends JFrame {
    private JTextField userText;
    private JPasswordField passwordText;
    private static final String USER_JSON = "./data/usermanager.json";
    private UserManager manager;
    User currentUser = null;

    // EFFECTS: Opens a new instance of the app with a login page.
    LoginFrame() {
        UserManagerReader userReader = new UserManagerReader(USER_JSON);
        try {
            manager = userReader.read();
        } catch (IOException e) {
            System.out.println("Unable to read from file: " + USER_JSON);
        }

        loginPage();

    }

    // MODIFIES: this
    // EFFECTS: Sets the frame with title and text
    private void loginPage() {
        JLabel label = new JLabel("Welcome to SwiftBid");
        label.setBounds(90, 210, 300, 30);
        label.setFont(new Font("Verdana", Font.PLAIN, 18));
        label.setForeground(Color.WHITE);
        add(label);

        JLabel prompt = new JLabel("Login / Create new User Account");
        prompt.setBounds(65, 250, 300, 30);
        prompt.setFont(new Font("Verdana", Font.PLAIN, 15));
        prompt.setForeground(Color.WHITE);
        add(prompt);

        loginHandler();

        imageDisplayer();


        getContentPane().setBackground(Color.black);
        setSize(400, 600);
        setLocationRelativeTo(null);
        setLayout(null);
        setResizable(false);
        setVisible(true);
    }

    // EFFECTS: sets image label on LoginFrame
    private void imageDisplayer() {
        ImageIcon icon = createImageIcon();
        if (icon != null) {
            JLabel imageLabel = new JLabel(icon);
            imageLabel.setBounds(40, 10, 300, 197);
            add(imageLabel);
        }
    }

    // EFFECTS: Sets label and text field for user to enter username and password
    private void loginHandler() {
        JLabel nameLabel = new JLabel("Username: ", JLabel.RIGHT);
        JLabel passwordLabel = new JLabel("Password: ", JLabel.CENTER);
        userText = new JTextField(6);
        passwordText = new JPasswordField(6);
        nameLabel.setBounds(50, 300, 80, 25);
        passwordLabel.setBounds(55, 330, 80, 25);
        userText.setBounds(140, 300, 165, 25);
        passwordText.setBounds(140, 330, 165, 25);

        nameLabel.setForeground(Color.WHITE);
        passwordLabel.setForeground(Color.WHITE);

        JButton button = new JButton("Login");

        button.setBounds(140, 400, 100, 20);
        button.addActionListener(e -> createLoginUser());

        add(button);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        add(nameLabel);
        add(passwordLabel);
        add(userText);
        add(passwordText);
    }

    // EFFECTS: Displays SwiftBid image
    protected ImageIcon createImageIcon() {
        try {
            BufferedImage image = ImageIO.read(new File("./data/SwiftBid.jpg"));
            return new ImageIcon(image);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    // MODIFIES:: currentUser
    // EFFECTS: Creates a user if username does not exist, authenticates user if username exists.
    private void createLoginUser() {
        String userName = userText.getText().strip();
        String password = passwordText.getText().strip();
        if (userName.isBlank()) {
            JOptionPane.showMessageDialog(this, "Please enter a Username!");
        } else {
            currentUser = new User(userName, password);
            if (currentUser.createAccount(userName, password, manager)) {
                JOptionPane.showMessageDialog(this, "User created successfully.");
                new MainMenuFrame(this);
                this.setVisible(false);
            } else {
                if (manager.authenticate(userName, password)) {
                    currentUser = manager.getUser(userName);
                    new MainMenuFrame(this);
                    this.setVisible(false);
                } else {
                    failedAuthenticationHandling();
                }
            }
        }
    }

    // EFFECTS: handles the case when authentication fails
    private void failedAuthenticationHandling() {
        JOptionPane.showMessageDialog(this, "User already exists. Authentication error. Try again.");
        int option = JOptionPane.showOptionDialog(
                this,
                "Press X to exit application",
                "Exit",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Exit","Try Again"},
                "Exit"
        );

        if (option == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }

    public User getCurrentUser() {
        return currentUser;
    }

    public UserManager getManager() {
        return manager;
    }

}