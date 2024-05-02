# Time Smiths Readme

## AddNewAdmin
The AddNewAdmin class allows administrators to input essential details such as username, email, password, first name, last name, and National Insurance (NI) number for creating a new admin account. Additionally, administrators can choose whether the new account is an owner account using a switch toggle.

## Admin
The Admin data class sets the attributes of an admin account, including the admin ID, username, first name, last name, email, password, National Insurance (NI) number, and owner account designation.

## AdminHoliday
The AdminHoliday class allows admins to view holiday requests and confirmed holidays as well as confirm pending holiday requests, and navigate between the two views. It interacts with Firebase Realtime Database to fetch and update lists.

## AdminHome
The AdminHome class is the navigation hub for all admins allowing them to manouver between differnt pages.

## AdminLogin
The AdminLogin Class sets up the functionality for the app to match up the inputed username and password.

## AdminProfile
The AdminProfile Class fills out text boxes with the logged in admins data suchas their emails, National Insurance number and name.

## AdminTimetable
The AdminTimetable class consists of a Listview and a Calendar view that fills the listview with the shifts for that date. when an admin holds on one of the shifts they have the ability to edit these or delete these on the Timetable Edit Page.

## BookHoliday
The BookHoliday Class allows an employee to select 2 dates that they want to have as the holiday and then book the request adding it to the firebase database under holiday/requests.

## CreateNewAccount
The Create new account class allows admins to create new accounts when all text fields are filled in.

## EditAdminAccount
The edit admin account class allows a select admin account that can then have the data changed. any edit texts that are left empty will not change.

## EditEmployee
The edit Employee class allows a select employee account that can then have the data changed. any edit texts that are left empty will not change.

## Employee
This is the employee data class that sets the layout for the data added to the employee table.

## Feedback
This is the class that saves the feedback to the feedback table and then exits the page.

## FeedbackDC
This is the feedback data class that sets the format of the feedback

## Holiday
This holiday fills the list views that allow the user to see their holiday requests and confirmed holiday and allows them to switch between.

## HolidayHelper
This is the data class for the holiday that sets the layout for the holiday data.

## LoginScreen
this uses firebase auth to test the inputs for a login page that takes you to the home page for employees on succession.

## LoginHelper
Does nothing, broke when it was deleted so i had to roll back.

## MainActivity
Just navigates to login this is the page you open on

## Owner_Login
Uses a read and match for username and password and owner access to allow the user to access the owner home page

## OwnerHome
This is just a navigation page for owners that have logged in it passes arounf the currently logged in user

## OwnerProfile
Retrieved the data for the currently logged in owner on the device

## PasswordReset
Allows employee that has forgotten password to use firebase auth method to reset email through password.

## Profile
Shows current users information in text boxes.

## ShiftAssignment
this is the class that allows admins to assign shifts to differnt employees.

## Timetable
This is the timetable that fills a listview with shifts that can be accessed by employees

## TimetableDC
This is the data class for the format of the shifts .

## TimeTableEdit
this is the class that allows admins to edits shifts of differnt employees.

## ViewAccounts
this fills a list view with the employees and is searchable by a admin

## ViewAdminAccount
this fills a list view with the admins and is searchable by a owner

## ViewFeedback
This is a listview that is the feedback left by employees.








