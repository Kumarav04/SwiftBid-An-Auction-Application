package persistence;

import model.Auction;
import model.User;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class JsonTest {
    protected void checkAuction(String listingName, User seller, double highestBid, User highestBidder, String description, Auction auction) {
        assertEquals(listingName, auction.getListingName());
        assertEquals(seller, auction.getSeller());
        assertEquals(highestBid, auction.getHighestBid(), 0.001); // Use an appropriate delta
        assertEquals(highestBidder, auction.getHighestBidder());
        assertEquals(description, auction.getDescription());
    }
}
