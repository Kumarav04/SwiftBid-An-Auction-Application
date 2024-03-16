# SwiftBid
## *Auctions Simplified*.

**CPSC 210 2023W1**
___
**SwiftBid** is an application that enables 
users to easily list products for auction, place bids,
and buy the items they are interested in. With the help of this 
adaptable platform, users can post classifieds and bid on listings. 
This guarantees a customised auction experience. 
With SwiftBid, users may directly participate 
in the auction process, making bids to compete and 
eventually make purchases, making the auctions effective
and achieving successful sales.

The concept behind this initiative is to give all types 
of buyers and sellers a platform where they can connect 
and find the right customer/seller  for the right price. Whether someone wants to declutter their space 
by auctioning off items they no longer need or desires
to find unique bargains through auctions, this app 
will cater to their needs. By fostering a straightforward 
auction environment, this application aims 
to make the process of auctioning and bidding 
accessible and convenient for a broad audience.
Some examples include of uses include:
- Selling automobiles
- Selling antique articles such as furniture
- Selling Real Estate
- Trading rare collectibles
- Selling goods in bulk to establishments

This project is of particular interest to me because
it can make auctioning accessible to a wider audience
without complicated regulations. People often prefer to
sell and buy for fixed prices to make the process easier,
but auctioning allows the seller to get the price they deserve 
for the item they are selling. In auctioning, the market, i.e. 
the consumers decide a fair price for the item sold, rather
than the sellers themselves. This provides  fair space for both
the buyers and the sellers.


## User stories
 
-  As a user, I want to be able to post a new classified  
-  As a user, I want to be able to view all the classifieds
- As a user, I want to be able to delete a classified
- As a user, I want to be able to open and view the details of each classified
- As a user, I want to be able to place a bid on a classified
- As a user, I want to be able to create a wish list of selective auctions currently active.
- As a user, I want to be reminded to save my wish list before I quit the application
- As a user, I want to be able to access my saved wish list at any time
- As a user, I want to be able to see currently active listings even after restarting the application
- As a user, I want to be able to be authenticated as a registered user even after restarting the application.

## **Instructions for grader**

- You can generate the first required action related to the user story "post a new classified"
by clicking on the "Post a new Auction" button in the main menu after logging into an existing account
or creating a new account. This will post a new auction by the user onto a list of all currently active listings.
####
- You can generate the second required action related to the user story "view all the classifieds"
by clicking on the "Browse Auctions" button in the main menu after logging into an existing account
or creating a new account. This will display all currently active listings. The user can also save auctions
to their wish list and click on "View your wishlist" button in the main menu to display the auctions in their
wish list.
####
- The visual component, SwiftBid logo in this case, can be found in the login page that 
is first displayed when the program is run.
####
- You can save the state of my application by clicking the appropriate option on the pop-up window that appears
when the user clicks on the "X" button in the top right corner of the main menu window, or the dedicated
"Exit application" button in the bottom of the main menu.
####
- You can reload the state of my application by logging into a saved account using the correct credentials which were 
used while creating the account. This will allow the user to load their saved wish list by clicking on 
"View your wish list".

## **Phase 4: Task 2**

Sample of Logged Events:

``Wed Nov 29 00:04:08 PST 2023``<br>
``User Authenticated! Username: Kumar``<br>
``Wed Nov 29 00:04:16 PST 2023``<br>
``New Bid placed on Auction Boat``<br>
``Wed Nov 29 00:04:24 PST 2023``<br>
``Auction Boat added to user's wishlist!``<br>
``Wed Nov 29 00:04:41 PST 2023``<br>
``New Auction Posted! Auction name: Car``<br>
``Wed Nov 29 00:05:03 PST 2023``<br>
``New Auction Posted! Auction name: Headphones``<br>
``Wed Nov 29 00:05:09 PST 2023``<br>
``Auction Headphones removed!``<br>
``Wed Nov 29 00:05:16 PST 2023``<br>
``Changes to user account saved!``

``Process finished with exit code 0``

## **Phase 4: Task 3**

- I would reduce repetitive use of code across different classes in my UI package
by creating an abstract superclass than handles the repetitive functions and call 
the methods in the abstract class whenever necessary. This would improve the
cohesion of my code. Additionally, I would add one or more interfaces/abstract classes in my model and persistence packages to reduce coupling and the code easier to read.

- The next thing I would do is implementing the Singleton Design Pattern for my LoginFrame
class in UI Package. This is the frame that initializes the application and acts as
a foundation for all the other functions, and it would make sense for it to be initialized
only once in every instance the application is started.

