package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class AuctionManagerTest {
    private AuctionManager auctionManager;
    private Auction testAuction1;
    private Auction testAuction2;

    @BeforeEach
    void runBefore() {
        auctionManager = new AuctionManager();
        testAuction1 = new Auction("Auction1", null,"desc1");
        testAuction2 = new Auction("Auction2", null,"desc1");
        auctionManager.addAuction(testAuction1);
    }

    @Test
    void testAddAuction() {
        auctionManager.addAuction(testAuction2);
        assertEquals(2, auctionManager.getAuctions().size());
    }

    @Test
    void testRemoveAuction() {
        auctionManager.removeAuction(testAuction1);
        assertEquals(0, auctionManager.getAuctions().size());
        auctionManager.removeAuction(testAuction2);
        assertEquals(0, auctionManager.getAuctions().size());
    }

    @Test
    void testGetAuctions() {
        assertEquals(1, auctionManager.getAuctions().size());
        auctionManager.addAuction(testAuction2);
        assertEquals(testAuction1, auctionManager.getAuctions().get(0));
        assertEquals(testAuction2, auctionManager.getAuctions().get(1));
    }

    @Test
    void testRemoveAuctionNonexistent() {
        Auction nonExistentAuction = new Auction("NonExistentAuction", null, "NonExistentDesc");
        auctionManager.removeAuction(nonExistentAuction);
        assertEquals(1, auctionManager.getAuctions().size());
    }

    @Test
    void testGetAuctionsEmptyList() {
        AuctionManager emptyAuctionManager = new AuctionManager();
        assertEquals(0, emptyAuctionManager.getAuctions().size());
    }

    @Test
    void testGetAuctionsModifiedList() {
        auctionManager.addAuction(testAuction2);
        assertEquals(2, auctionManager.getAuctions().size());

        auctionManager.removeAuction(testAuction1);
        assertEquals(1, auctionManager.getAuctions().size());
        assertEquals(testAuction2, auctionManager.getAuctions().get(0));
    }
}
