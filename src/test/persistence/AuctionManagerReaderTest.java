package persistence;

import model.Auction;
import model.AuctionManager;
import model.User;
import org.junit.jupiter.api.Test;
import java.io.IOException;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

public class AuctionManagerReaderTest {

    @Test
    void testReaderNonExistentFile() {
        AuctionManagerReader reader = new AuctionManagerReader("./data/noSuchFile.json");
        try {
            AuctionManager am = reader.read();
            assertNotNull(am);
            assertEquals(0, am.getAuctions().size());
        } catch (IOException e) {
            fail("IOException not expected");
        }
    }

    @Test
    void testReaderEmptyAuctionManager() {
        AuctionManagerReader reader = new AuctionManagerReader("./data/testReaderEmptyAuctionManager.json");
        try {
            AuctionManager am = reader.read();
            assertNotNull(am);
            assertEquals(0, am.getAuctions().size());
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    @Test
    void testReaderGeneralAuctionManager() {
        AuctionManagerReader reader = new AuctionManagerReader("./data/testReaderGeneralAuctionManager.json");
        try {
            AuctionManager am = reader.read();
            assertNotNull(am);
            List<Auction> auctions = am.getAuctions();
            assertEquals(2, auctions.size());
            checkAuction("Car Auction", "John", "pass123", 1000.0, "Jane"
                    , "pass456", "Fast car", auctions.get(0));
            checkAuction("Bike Auction", "Alice", "pass789", 500.0,
                    null, null, "Mountain bike", auctions.get(1));
        } catch (IOException e) {
            fail("Couldn't read from file");
        }
    }

    private void checkAuction(
            String listingName, String sellerName, String sellerPass,
            double highestBid, String bidderName, String bidderPass,
            String description, Auction auction) {
        assertEquals(listingName, auction.getListingName());
        assertEquals(description, auction.getDescription());
        User seller = auction.getSellerObject();
        assertEquals(sellerName, seller.getUserName());
        assertEquals(sellerPass, seller.getPassWord());
        assertEquals(highestBid, auction.getHighestBid());
        if (bidderName != null && bidderPass != null) {
            User bidder = auction.getHighestBidder();
            assertEquals(bidderName, bidder.getUserName());
            assertEquals(bidderPass, bidder.getPassWord());
        } else {
            assertNull(auction.getHighestBidder().getUserName());
        }
    }
}
