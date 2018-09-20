#Tamedia mobila challenge solution


## Functionality and how it works

- I have created an android app project in Android studio with empty activity for a start

- Inside layout file i have added an horizontal layout
  and in it 2 ui components we needed (EditText and Button vith valid ids)

- While creating activity I initialized button listener
  to check if this email is valid (by using Android email pattern matching)
  Validating is encapsulated into ControlBusinessLogc class to keep it separated from UI part
  In case of wrong email format user will get Toast with appropriate message.
  All strings are extracted in strings.xml in case of need for localization.
  (All network requests for getting data and handling those date are encapsulated in that ControlBusinessLogc class)

- I have included compile Fast-Android-Networking for networking part and configured it properly.
  Paths and params for RESt API was separated with some other constants in ConstBusinessLogic

- I have created some Java domain classes for expected JSON responses via http://pojo.sodhanalibrary.com/

- By using this AndroidNetworking I get users for email that have been entered.
  If there is no user for that email I will toast User with message about that.
  If user is found I will save that users email in Android's shared preferences for later use
  (added in activity creation code to check for last valid email in shared prefs
  and fill email field with that address for start)

- After getting valid user from server I load all posts for data user and show it via ListView
  (using it in package with ListAdapterPosts with l_post_item.xml layout and ViewHolder pattern)
  and enabling new post subform (2 EditText for title and body and button for posting, separate xml layout for land to optimize screen use )

- On sending new posts code will check if title/text fields are populated. If that's ok posts are stored in pending posts list and they are removed from there after post is send successfully.

- Currently code will save those pending posts in shared prefs and try to resend it only on acrivty next start or orientation change (no network detection implemented)

- App uses onSaveInstanceState / onRestoreInstanceState mechanism to manage data on screen orientation change

- After adding new posts via network api that post will be removed from pending posts and will be added to the main list (and show it on top of the ListView)

- Note: I have some basic knowledge on Android integration tests but I didn't use it in my practice so far.


# How to build and run app
Import this as a project in Android Studio to build and run app.
