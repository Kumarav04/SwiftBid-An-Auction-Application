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
        testUserManager = new UserManager();
        testUser = new User("newUser", "newPassword", testAuctionManager);
    }

    @Test
    void testConstructor() {
        assertEquals("newUser", testUser.getUserName());
        assertEquals("newPassword", testUser.getPassWord());
    }

    @Test
    void testCreateListing() {
        assertTrue(testUser.createListing("New Auction","DemoDescription"));
        assertEquals(1, testAuctionManager.getAuctions().size());
    }

    @Test
    void testCreateAccount() {
        testUserManager = new UserManager();
        User testUser1 = new User("newUser2", "newPassword", testAuctionManager);
        assertTrue(testUser1.createAccount(testUser1.getUserName(), testUser1.getPassWord(), testUserManager));
    }

    @Test
    void testCreateAccountExists() {
        testUserManager = new UserManager();
        assertTrue(testUser.createAccount("newUser2", "password", testUserManager));
        assertFalse(testUser.createAccount("newUser2", "password", testUserManager));
        assertFalse(testUser.createAccount("newUser2", "newPassword", testUserManager));

    }

    @Test
    void testPlaceBid() {
        Auction auction = new Auction("Test Auction", testUser,"DemoDesc");
        testAuctionManager.addAuction(auction);
        assertTrue(testUser.placeBid(auction, 100.0));
        assertEquals(100.0, auction.getHighestBid());
        assertEquals("Test Auction", auction.getListingName());
    }


    @Test
    void testPlaceBidInvalidBid() {
        Auction auction = new Auction("Invalid Bid Auction", testUser, "demoDesc");
        testAuctionManager.addAuction(auction);
        assertFalse(testUser.placeBid(auction, -10.0));
        assertEquals(0.0, auction.getHighestBid());
    }


}
