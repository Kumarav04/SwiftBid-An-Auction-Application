package model;


// Unit tests for User class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User testUser;
    private AuctionManager testAuctionManager;
    private UserManager testUserManager;


    @BeforeEach
    void runBefore() {
        testAuctionManager = new AuctionManager();
        testUser = new User("newUser", "newPassword", testAuctionManager);
    }

    @Test
    void testConstructor() {
        assertEquals("newUser", testUser.getUserName());
        assertEquals("newPassword", testUser.getPassWord());
    }

    @Test
     void testCreateListing() {
        assertTrue(testUser.createListing("New Auction"));
        assertEquals(1, testAuctionManager.getAuctions().size());
    }

    @Test
    void testCreateAccount() {
        testUserManager = new UserManager();
        User testUser1 = new User("newUser2","newPassword",testAuctionManager);
        assertTrue(testUser1.createAccount(testUser1.getUserName(), testUser1.getPassWord(), testUserManager));
    }

    @Test
    void testCreateAccountExists() {
        testUserManager = new UserManager();
        testUser.createAccount("newUser2","password", testUserManager);
        assertFalse(testUser.createAccount("newUser2","password", testUserManager));

    }

    @Test
    void testPlaceBid() {
        Auction auction = new Auction("Test Auction", testUser);
        testAuctionManager.addAuction(auction);
        assertTrue(testUser.placeBid(auction, 100.0));
        assertEquals(100.0, auction.getHighestBid(), 0.0);
        assertEquals("Test Auction",auction.getListingName());
    }


}
