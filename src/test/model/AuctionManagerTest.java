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
        testAuction1 = new Auction("Auction1", null);
        testAuction2 = new Auction("Auction2", null);
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
    }

    @Test
    void testGetAuctions() {
        assertEquals(1, auctionManager.getAuctions().size());
        assertEquals(testAuction1, auctionManager.getAuctions().get(0));
    }
}
