package persistence;

import model.Auction;
import model.AuctionManager;
import model.User;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

public class AuctionManagerJsonWriterTest {
    @Test
    void testWriterInvalidFile() {
        try {
            AuctionManager am = new AuctionManager();
            AuctionManagerJsonWriter writer = new AuctionManagerJsonWriter("./data/my\0illegal:fileName.json");
            writer.open();
            writer.write(am);
            writer.close();
            fail("FileNotFoundException was expected");
        } catch (FileNotFoundException e) {
            // pass
        }
    }

    @Test
    void testWriterEmptyAuctionManager() {
        try {
            AuctionManager am = new AuctionManager();
            AuctionManagerJsonWriter writer = new AuctionManagerJsonWriter("./data/testWriterEmptyAuctionManager.json");
            writer.open();
            writer.write(am);
            writer.close();

            AuctionManagerReader reader = new AuctionManagerReader("./data/testWriterEmptyAuctionManager.json");
            am = reader.read();
            List<Auction> auctions = am.getAuctions();
            assertEquals(0, auctions.size());
        } catch (IOException e) {
            fail("Exception should not have been thrown");
        }
    }

    @Test
    void testWriterGeneralAuctionManager() {
        try {
            AuctionManager am = new AuctionManager();
            User seller = new User("John", "pass123");
            User bidder = new User("Jane", "pass456");
            am.addAuction(new Auction("Car Auction", seller,  "Fast car"));
            am.addAuction(new Auction("Bike Auction", seller,  "Mountain bike"));

            AuctionManagerJsonWriter writer = new AuctionManagerJsonWriter("./data/testWriterGeneralAuctionManager.json");
            writer.open();
            writer.write(am);
            writer.close();

            AuctionManagerReader reader = new AuctionManagerReader("./data/testWriterGeneralAuctionManager.json");
            am = reader.read();
            List<Auction> auctions = am.getAuctions();
            assertEquals(2, auctions.size());

            Auction carAuction = auctions.get(0);
            assertEquals("Car Auction", carAuction.getListingName());
            assertEquals("John", carAuction.getSeller());
            assertEquals(0, carAuction.getHighestBid());
            assertEquals(null, carAuction.getHighestBidder().getUserName());
            assertEquals("Fast car", carAuction.getDescription());

            Auction bikeAuction = auctions.get(1);
            assertEquals("Bike Auction", bikeAuction.getListingName());
            assertEquals("John", bikeAuction.getSeller());
            assertEquals(0, bikeAuction.getHighestBid());
            assertEquals(null, bikeAuction.getHighestBidder().getUserName());
            assertEquals("Mountain bike", bikeAuction.getDescription());

        } catch (IOException e) {
            fail("Exception should be thrown");
        }
    }
}
