package persistence;

import model.User;
import model.UserManager;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerJsonReaderTest {
    private final String testFilePath = "./data/testUserManagerReader.json";

    @Test
    void testReadNonExistentFile() {
        UserManagerReader reader = new UserManagerReader("nonexistent\\file.json");
        try {
            UserManager userManager = reader.read();
            assertEquals(0, userManager.getAllUsers().size());
        } catch (IOException e) {
            fail("IOException was not expected");
        }
    }

    @Test
    void testReadEmptyUserManager() {
        UserManagerJsonWriter writer = new UserManagerJsonWriter(testFilePath);
        try {
            writer.open();
            writer.write(new UserManager());
            writer.close();

            UserManagerReader reader = new UserManagerReader(testFilePath);
            UserManager userManager = reader.read();

            assertEquals(0, userManager.getAllUsers().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testReadUserManagerWithUsers() {
        UserManager userManager = new UserManager();
        userManager.addUser(new User("user1", "password1"));
        userManager.addUser(new User("user2", "password2"));
        UserManagerJsonWriter writer = new UserManagerJsonWriter(testFilePath);

        try {
            writer.open();
            writer.write(userManager);
            writer.close();

            UserManagerReader reader = new UserManagerReader(testFilePath);
            userManager = reader.read();

            assertEquals(2, userManager.getAllUsers().size());
            assertEquals("user1", userManager.getUser("user1").getUserName());
            assertEquals("user2", userManager.getUser("user2").getUserName());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }
}
