package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Holiday : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)

        // Buttons for navigation
        val Holidayretbutton: Button = findViewById(R.id.holidayRetBut)
        Holidayretbutton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

        val BoHoliday: Button = findViewById(R.id.bookHoliday)
        BoHoliday.setOnClickListener {
            val intent = Intent(this, BookHoliday::class.java)
            startActivity(intent)
        }

        // Buttons for switching between holiday requests and bookings
        val changetoBooked: Button = findViewById(R.id.yourHoliday)
        val changetorequests: Button = findViewById(R.id.currentRequests)
        val ShowBookedHoliday: ListView = findViewById(R.id.yourHolidayList)
        val ShowHolidayRequests: ListView = findViewById(R.id.yourRequestsList)

        // Switch to show booked holidays
        changetoBooked.setOnClickListener {
            ShowHolidayRequests.visibility = View.INVISIBLE
            ShowBookedHoliday.visibility = View.VISIBLE
        }

        // Switch to show holiday requests
        changetorequests.setOnClickListener {
            ShowHolidayRequests.visibility = View.VISIBLE
            ShowBookedHoliday.visibility = View.INVISIBLE
        }

        // Initialize Firebase components
        auth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Fetch holiday requests for the current user
        getEmployeeDetailsForCurrentUser()
    }

    // Fetch holiday requests for the current user
    private fun fetchRequests(employeeId: Int) {
        // Reference to holiday requests in the database
        val requestsReference = database.getReference("Holiday/requests")

        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requestsList = mutableListOf<String>() // List to hold holiday requests
                for (requestsSnapshot in snapshot.children) {
                    val requests = requestsSnapshot.getValue(HolidayHelper::class.java)
                    // Check if the request is for the current employee
                    if (requests != null && requests.employeeID == employeeId) {
                        val requestsInfo = "${requests.holidayID}, Start: ${requests.startDate}, End: ${requests.endDate}"
                        requestsList.add(requestsInfo)
                        fetchConHol(employeeId)
                    }
                }
                // Update ListView with holiday requests
                val adapter = ArrayAdapter<String>(this@Holiday, android.R.layout.simple_list_item_1, requestsList)
                findViewById<ListView>(R.id.yourRequestsList).adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e("fetchRequests", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    // Fetch confirmed holidays for the current user
    private fun fetchConHol(employeeId: Int) {
        // Reference to confirmed holidays in the database
        val requestsReference = database.getReference("Holiday/confirmed")

        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val confirmedList = mutableListOf<String>() // List to hold confirmed holidays
                for (confirmedSnapshot in snapshot.children) {
                    val confirmed = confirmedSnapshot.getValue(HolidayHelper::class.java)
                    // Check if the confirmed holiday is for the current employee
                    if (confirmed != null && confirmed.employeeID == employeeId) {
                        val requestsInfo = "${confirmed.holidayID}, Start: ${confirmed.startDate}, End: ${confirmed.endDate}"
                        confirmedList.add(requestsInfo)
                    }
                }
                // Update ListView with confirmed holidays
                val adapter = ArrayAdapter<String>(this@Holiday, android.R.layout.simple_list_item_1, confirmedList)
                findViewById<ListView>(R.id.yourHolidayList).adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e("fetchConfirmed", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    // Fetch employee details for the current user
    fun getEmployeeDetailsForCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let { email ->
                val reference = database.getReference("Employees")

                // Query database for the employee with the current user's email
                reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var employeeId: Int? = null // Initialize to null to avoid unnecessary default value
                        for (employeeSnapshot in dataSnapshot.children) {
                            val employee = employeeSnapshot.getValue(Employee::class.java)
                            // If employee with given email is found
                            if (employee != null) {
                                Log.i("Employee", "Employee found - ID: ${employee.employeeId}")
                                employeeId = employee.employeeId
                                fetchRequests(employeeId!!)
                            } else {
                                Log.i("Employee", "Employee with email $email not found")
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Employee", "Error fetching employee details: ${databaseError.message}")
                    }
                })
            }
        }
    }
}
