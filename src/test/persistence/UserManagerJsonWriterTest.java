package persistence;

import model.Auction;
import model.AuctionManager;
import model.User;
import model.UserManager;
import org.json.JSONObject;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerJsonWriterTest {
    private UserManager userManager;
    private UserManagerJsonWriter userManagerJsonWriter;
    private UserManagerReader userManagerReader;

    @BeforeEach
    void runBefore() {
        userManager = new UserManager();
        User testUser1 = new User("user1", "password1");
        User testUser2 = new User("user2", "password2");
        userManager.addUser(testUser1);
        userManager.addUser(testUser2);
        userManagerJsonWriter = new UserManagerJsonWriter("./data/testWriterUserManager.json");
        userManagerReader = new UserManagerReader("./data/testWriterUserManager.json");
    }

    @Test
    void testConstructor() {
        try {
            userManagerJsonWriter.open();
            userManagerJsonWriter.close();

            File file = new File("./data/testWriterUserManager.json");
            assertTrue(file.exists());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriteUserManager() {
        try {
            userManagerJsonWriter.open();
            userManagerJsonWriter.write(userManager);
            userManagerJsonWriter.close();

            UserManagerReader userManagerJsonReader = new UserManagerReader("./data/testWriterUserManager.json");
            UserManager userManagerRead = userManagerJsonReader.read();
            assertEquals(userManager.getAllUsers().size(), userManagerRead.getAllUsers().size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testAddUserWithWishlist() {
        UserManager um = new UserManager();
        User testUser = new User("testName", "testPass");
        Auction testAuction = new Auction("testAuction", testUser, "");
        AuctionManager testAuctionManager = new AuctionManager();
        testAuctionManager.addAuction(testAuction);
        testUser.addToWishList(testAuction, testAuctionManager.getAuctions());
        JSONObject jsonObject = testUser.toJson();
        userManagerReader.addUser(um, jsonObject);

        assertEquals(1, um.getAllUsers().size());
        User user = um.getAllUsers().get(0);
        assertEquals("testName", user.getUserName());
        assertEquals("testPass", user.getPassWord());
        assertEquals(1, user.getWishlist().size());
    }
}
