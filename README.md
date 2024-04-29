Time Smiths Readme

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









