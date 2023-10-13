package model;

import java.util.ArrayList;
import java.util.List;


// Handles all User creation and management.
public class UserManager {
    private List<User> allUsers;

    // EFFECTS: Constructs a new user manager as an ArrayList.
    public UserManager() {
        this.allUsers = new ArrayList<>();
    }

    public boolean isUsernameTaken(String username) {
        for (User user : allUsers) {
            if (user.getUserName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    // EFFECTS: Authenticates and returns true if user already exists and passwords match, else false
    public boolean authenticate(String username, String password) {
        for (User user  : allUsers) {
            if (user.getUserName().equals(username)) {
                if (user.getPassWord().equals(password)) {
                    return true;
                }
            }
        }
        return false;
    }

    // EFFECTS: Returns User object if found in User Manager ArrayList, null otherwise
    public User getUser(String username) {
        for (User user : allUsers) {
            if (user.getUserName().equals(username)) {
                return user;
            }
        }
        return null;
    }

    // REQUIRES: !user = null
    // MODIFIES: this
    // EFFECTS: Adds user to the list of all users contained in UserManager.
    public void addUser(User user) {
        allUsers.add(user);
    }



}


