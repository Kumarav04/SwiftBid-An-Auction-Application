package ui;

import model.Event;
import model.EventLog;
import model.UserManager;
import persistence.UserManagerJsonWriter;

import javax.swing.*;
import java.io.FileNotFoundException;

public class ExitFrame extends JFrame {

    private static final String USER_JSON = "./data/usermanager.json";
    private UserManager manager;
    private UserManagerJsonWriter userWriter;

    // EFFECTS: Opens an option dialogue asking the user to save before exiting.
    public ExitFrame(LoginFrame loginFrame) {
        userWriter = new UserManagerJsonWriter(USER_JSON);

        manager = loginFrame.getManager();

        int option = JOptionPane.showOptionDialog(
                this,
                "Would you like to save your account and related changes before exiting?"
                + "\nThis includes changes to your wishlist, and latest updates to the auctions in your wishlist.",
                "Exiting Application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Save","Don't Save"},
                "Save"
        );

        exitMechanism(option);
    }


    // MODIFIES: manager
    // EFFECTS: Saves the changes by writing manager into JSON file if the user chooses to do so
    private void exitMechanism(int option) {
        if (option == JOptionPane.NO_OPTION) {
            for (Event e : EventLog.getInstance()) {
                System.out.println(e);
            }
            System.exit(0);
        } else {
            try {
                userWriter.open();
                userWriter.write(manager);
                userWriter.close();
                JOptionPane.showMessageDialog(this, "Changes saved successfully.");
            } catch (FileNotFoundException e) {
                System.out.println("Unable to write to file: " + USER_JSON);
            }
            for (Event e : EventLog.getInstance()) {
                System.out.println(e);
            }
            System.exit(0);

        }
    }
}
