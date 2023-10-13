package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest {
    private Auction testAuction;
    private User testUser;
    private AuctionManager testAuctionManager;

    @BeforeEach
    void runBefore() {
        testAuctionManager = new AuctionManager();
        testUser = new User("newUser", "newPassword", testAuctionManager);
        testAuction = new Auction("newListing", testUser);
    }

    @Test
    void testConstructor() {
        assertEquals("newListing", testAuction.getListingName());
        assertEquals("newUser",testAuction.getSeller());
        assertEquals(0,testAuction.getHighestBid());
        assertNull(testAuction.getHighestBidder());
        testAuction.placeBid(testUser,500.0);
        assertEquals(testUser,testAuction.getHighestBidder());
    }

    @Test
    void testPlaceBid() {
        assertTrue(testAuction.placeBid(testUser,100.0));
        assertFalse(testAuction.placeBid(testUser,50.0));
        User testUser1 = new User("newUser2","newPassword", testAuctionManager);
        assertTrue(testAuction.placeBid(testUser1,150.0));
        assertEquals("newUser2",testAuction.getHighestBidder().getUserName());
        assertEquals(150.0,testAuction.getHighestBid());
    }


    @Test
    void testPlaceBidBelowCurrentHighestBid() {
        testAuction.placeBid(testUser, 100.0);
        assertFalse(testAuction.placeBid(testUser, 50.0));
    }

    @Test
    void testPlaceBidEqualCurrentHighestBid() {
        assertTrue(testAuction.placeBid(testUser, 100.0));
        assertFalse(testAuction.placeBid(testUser, 100.0));
        assertEquals(testUser, testAuction.getHighestBidder());
    }

    @Test
    void testPlaceBidWithDifferentUsers() {
        User testUser1 = new User("newUser2", "newPassword", testAuctionManager);
        assertTrue(testAuction.placeBid(testUser, 100.0));
        assertTrue(testAuction.placeBid(testUser1, 150.0));
        assertEquals("newUser2", testAuction.getHighestBidder().getUserName());
        assertEquals(150.0, testAuction.getHighestBid());
    }

}
