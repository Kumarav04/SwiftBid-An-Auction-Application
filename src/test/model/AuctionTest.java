package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuctionTest {
    private Auction testAuction;
    private User testUser;
    private User testUser2;
    private UserManager testUserManager;

    @BeforeEach
    void runBefore() {
        AuctionManager testAuctionManager = new AuctionManager();
        testUserManager = new UserManager();
        testUser = new User("newUser", "newPassword");
        testUser2 = new User("newUser2", "newPassword2");
        testUserManager.addUser(testUser);
        testUserManager.addUser(testUser2);
        testAuction = new Auction("newListing", testUser, "newDesc");
        Auction testAuction2 = new Auction("newListing2", testUser2, "newDesc2");
        testAuctionManager.addAuction(testAuction);
        testAuctionManager.addAuction(testAuction2);
        testUser2.addToWishList(testAuction, testAuctionManager.getAuctions());
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
        User testUser1 = new User("newUser2","newPassword");
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
        User testUser1 = new User("newUser2", "newPassword");
        assertTrue(testAuction.placeBid(testUser, 100.0));
        assertTrue(testAuction.placeBid(testUser1, 150.0));
        assertEquals("newUser2", testAuction.getHighestBidder().getUserName());
        assertEquals(150.0, testAuction.getHighestBid());
    }

    @Test
    void testUpdateWishlists() {
        // Call updateWishlists on user1
        testAuction.placeBid(testUser2, 400);
        testAuction.updateWishlists(testUserManager);

        // Check if user1's wishlist has been updated
        assertFalse(testUser.updateWishlistAuction(testAuction));
        assertTrue(testUser2.updateWishlistAuction(testAuction));
    }

    @Test
    void testAuctionToJson() {
        assertEquals(Auction.fromJson(testAuction.toJson()).getSeller(), testAuction.getSeller());
        assertEquals(Auction.fromJson(testAuction.toJson()).getListingName(), testAuction.getListingName());
        assertEquals(Auction.fromJson(testAuction.toJson()).getDescription(), testAuction.getDescription());
        assertEquals(Auction.fromJson(testAuction.toJson()).getHighestBid(), testAuction.getHighestBid());
        assertNull(Auction.fromJson(testAuction.toJson()).getHighestBidder().getUserName());
        testAuction.placeBid(testUser2, 400);
        assertEquals(Auction.fromJson(testAuction.toJson()).getHighestBidder().getUserName(),
                testAuction.getHighestBidder().getUserName());
    }

}
