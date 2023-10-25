package model;


// Unit tests for User class

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserTest {
    private User testUser;
    private User testUser2;
    private AuctionManager testAuctionManager;
    private UserManager testUserManager;
    private Auction sampleAuction1;
    private Auction sampleAuction2;
    private Auction sampleAuction3;


    @BeforeEach
    void runBefore() {
        testAuctionManager = new AuctionManager();
        testUserManager = new UserManager();
        testUser = new User("newUser", "newPassword");
        testUser2 = new User("newUser2", "newPassword2");
        sampleAuction1 = new Auction("newListing1", testUser, "newDesc1");
        sampleAuction2 = new Auction("newListing2", testUser, "newDesc2");
        sampleAuction3 = new Auction("newListing3", testUser, "newDesc3");
        testAuctionManager.addAuction(sampleAuction1);
        testAuctionManager.addAuction(sampleAuction2);
    }

    @Test
    void testConstructor() {
        assertEquals("newUser", testUser.getUserName());
        assertEquals("newPassword", testUser.getPassWord());
    }

    @Test
    void testCreateListing() {
        assertTrue(testUser.createListing("New Auction","DemoDescription", testAuctionManager));
        assertEquals(3, testAuctionManager.getAuctions().size());
    }

    @Test
    void testCreateAccount() {
        testUserManager = new UserManager();
        User testUser1 = new User("newUser2", "newPassword");
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


    @Test
    public void testAddToWishList() {
        assertTrue(testUser.addToWishList(sampleAuction1, testAuctionManager.getAuctions()));
        assertTrue(testUser.addToWishList(sampleAuction2, testAuctionManager.getAuctions()));

        assertFalse(testUser.addToWishList(sampleAuction1, testAuctionManager.getAuctions()));

        assertFalse(testUser.addToWishList(sampleAuction3, testAuctionManager.getAuctions()));


    }

    @Test
    public void testRemoveFromWishList() {
        testUser.addToWishList(sampleAuction1,testAuctionManager.getAuctions());
        testUser.addToWishList(sampleAuction2,testAuctionManager.getAuctions());

        assertTrue(testUser.removeFromWishList(sampleAuction1));

        assertTrue(testUser.removeFromWishList(sampleAuction2));

        assertFalse(testUser.removeFromWishList(sampleAuction3));



    }

    @Test
    public void testUpdateWishlistAuction() {

        testUser.addToWishList(sampleAuction1, new ArrayList<>());
        testUser.addToWishList(sampleAuction3, new ArrayList<>());

        assertEquals(sampleAuction2.getHighestBid(), sampleAuction1.getHighestBid());
        assertEquals(sampleAuction2.getHighestBidder(), sampleAuction1.getHighestBidder());

        Auction newAuction = new Auction("NewAuction", testUser, "NewDescription");
        newAuction.placeBid(testUser, 200.0);
        assertFalse(testUser.updateWishlistAuction(newAuction));

        assertEquals(0, sampleAuction1.getHighestBid());
        assertNull(sampleAuction1.getHighestBidder());

        sampleAuction2.placeBid(testUser, 300.0);
        assertFalse(testUser.updateWishlistAuction(sampleAuction2));

        assertEquals(sampleAuction2.getHighestBid(), 300.0);
        assertEquals(sampleAuction2.getHighestBidder(), testUser);

        assertFalse(testUser.updateWishlistAuction(sampleAuction3));
    }

    @Test
    void testUpdateWishlistAuctionMatchingNames() {
        Auction updatedAuction = new Auction("MatchingName", testUser, "");
        Auction a = new Auction("MatchingName", testUser2, "");
        testAuctionManager.addAuction(updatedAuction);
        testAuctionManager.addAuction(a);
        testUser2.addToWishList(a,testAuctionManager.getAuctions());

        assertTrue(testUser2.updateWishlistAuction(updatedAuction));
    }

    // Covering the case where listing names do not match
    @Test
    void testUpdateWishlistAuctionNonMatchingNames() {
        Auction updatedAuction = new Auction("UpdatedName", testUser, "");
        Auction a = new Auction("OriginalName", testUser2, "");
        testAuctionManager.addAuction(updatedAuction);
        testAuctionManager.addAuction(a);
        testUser2.addToWishList(a,testAuctionManager.getAuctions());

        assertFalse(testUser2.updateWishlistAuction(updatedAuction));
    }

    @Test
    public void testToJson() {
        // Test converting a user with a wish list to a JSON object
        assertEquals(testUser.fromUserJson(testUser.toJson()).getUserName(), testUser.getUserName());
        assertEquals(testUser.fromUserJson(testUser.toJson()).getPassWord(), testUser.getPassWord());
        assertEquals(testUser.fromUserJson(testUser.toJson()).getWishlist(), testUser.getWishlist());
        testUser2.addToWishList(sampleAuction1, testAuctionManager.getAuctions());
        assertEquals(testUser2.fromUserJson(testUser2.toJson()).getUserName(), testUser2.getUserName());
        assertEquals(testUser2.fromUserJson(testUser2.toJson()).getPassWord(), testUser2.getPassWord());
        assertEquals(testUser2.fromUserJson(testUser2.toJson()).getWishlist().size(), testUser2.getWishlist().size());


    }


}
