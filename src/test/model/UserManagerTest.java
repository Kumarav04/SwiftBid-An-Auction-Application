package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class UserManagerTest {
    private UserManager userManager;
    private User testUser1;
    private User testUser2;

    @BeforeEach
    void runBefore() {
        userManager = new UserManager();
        testUser1 = new User("username1", "password1");
        testUser2 = new User("username2", "password2");
        userManager.addUser(testUser1);
    }

    @Test
    void testIsUsernameTaken() {
        assertTrue(userManager.isUsernameTaken("username1"));
        assertFalse(userManager.isUsernameTaken("nonexistent"));
    }

    @Test
    void testAuthenticate() {
        assertTrue(userManager.authenticate("username1", "password1"));
        assertFalse(userManager.authenticate("username1", "wrongpassword"));
        assertFalse(userManager.authenticate("nonexistent", "password"));
    }

    @Test
    void testGetUser() {
        assertEquals(testUser1, userManager.getUser("username1"));
        assertNull(userManager.getUser("nonexistent"));
    }

    @Test
    void testAddUser() {
        userManager.addUser(testUser2);
        assertTrue(userManager.isUsernameTaken("username2"));
        assertEquals(testUser2, userManager.getUser("username2"));
    }

    @Test
    void testGetAllUsers() {
        ArrayList temp = new ArrayList();
        temp.add(testUser1);
        assertEquals(userManager.getAllUsers(),temp);
    }
}
