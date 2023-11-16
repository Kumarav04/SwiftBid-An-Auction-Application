package ui;

import model.UserManager;
import persistence.UserManagerJsonWriter;
import persistence.UserManagerReader;

import javax.swing.*;
import java.io.FileNotFoundException;
import java.io.IOException;

public class ExitFrame extends JFrame {

    private static final String USER_JSON = "./data/usermanager.json";
    private UserManager manager;
    private UserManagerJsonWriter userWriter;

    public ExitFrame(LoginFrame loginFrame) {
        userWriter = new UserManagerJsonWriter(USER_JSON);

        manager = loginFrame.getManager();

        int option = JOptionPane.showOptionDialog(
                this,
                "Would you like to save the changes to your account before exiting?",
                "Exiting Application",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.QUESTION_MESSAGE,
                null,
                new Object[]{"Save","Don't Save"},
                "Save"
        );

        exitMechanism(option);
    }

    private void exitMechanism(int option) {
        if (option == JOptionPane.NO_OPTION) {
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
            System.exit(0);

        }
    }
}
