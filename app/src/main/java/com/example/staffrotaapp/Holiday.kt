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
import java.time.LocalDate

class Holiday : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)

        val Holidayretbutton: Button = findViewById(R.id.holidayRetBut)
        Holidayretbutton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

        val BookHoliday: Button = findViewById(R.id.bookHoliday)
        BookHoliday.setOnClickListener {
            val intent = Intent(this, BookHoliday::class.java)
            startActivity(intent)
        }

        val changetoBooked: Button = findViewById(R.id.yourHoliday)
        val changetorequests: Button = findViewById(R.id.currentRequests)
        val ShowBookedHoliday: ListView = findViewById(R.id.yourHolidayList)
        val ShowHolidayRequests: ListView = findViewById(R.id.yourRequestsList)

        changetoBooked.setOnClickListener {
            ShowHolidayRequests.visibility = View.INVISIBLE
            ShowBookedHoliday.visibility = View.VISIBLE
        }

        changetorequests.setOnClickListener {
            ShowHolidayRequests.visibility = View.VISIBLE
            ShowBookedHoliday.visibility = View.INVISIBLE
        }

        auth = FirebaseAuth.getInstance()

        getEmployeeDetailsForCurrentUser()
    }

    private fun fetchRequests(employeeId: Int) {
        val requestsReference = FirebaseDatabase.getInstance().getReference("Holiday/requests")

        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requestsList = mutableListOf<String>() // List to hold employee information
                for (requestsSnapshot in snapshot.children) {
                    val requests = requestsSnapshot.getValue(HolidayHelper::class.java)
                    if (requests != null && requests.employeeID == employeeId) { // Check if the request is for the handled employee
                        val requestsInfo = "${requests.holidayID}, Start: ${requests.startDate}, End: ${requests.endDate}"
                        requestsList.add(requestsInfo)
                        fetchConHol(employeeId)
                    }
                }
                val adapter = ArrayAdapter<String>(this@Holiday, android.R.layout.simple_list_item_1, requestsList)
                findViewById<ListView>(R.id.yourRequestsList).adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("fetchRequests", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    private fun fetchConHol(employeeId: Int) {
        val requestsReference = FirebaseDatabase.getInstance().getReference("Holiday/confirmed")

        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val confirmedList = mutableListOf<String>() // List to hold employee information
                for (confirmedSnapshot in snapshot.children) {
                    val confirmed = confirmedSnapshot.getValue(HolidayHelper::class.java)
                    if (confirmed != null && confirmed.employeeID == employeeId) {
                        val requestsInfo = "${confirmed.holidayID}, Start: ${confirmed.startDate}, End: ${confirmed.endDate}"
                        confirmedList.add(requestsInfo)
                    }
                }
                val adapter = ArrayAdapter<String>(this@Holiday, android.R.layout.simple_list_item_1, confirmedList)
                findViewById<ListView>(R.id.yourHolidayList).adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("fetchConfirmed", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    fun getEmployeeDetailsForCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let { email ->
                val database = FirebaseDatabase.getInstance()
                val reference = database.getReference("Employees")

                reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        var employeeId: Int? = 0 // Changed initialization to null to avoid unnecessary default value
                        for (employeeSnapshot in dataSnapshot.children) {
                            val employee = employeeSnapshot.getValue(Employee::class.java)
                            // If employee with given email is found
                            if (employee != null) {
                                println("Employee found - ID: ${employee.employeeId}")
                                employeeId = employee.employeeId
                                fetchRequests(employeeId)
                            } else {
                                println("Employee with email $email not found")
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        println("Error fetching employee details: ${databaseError.message}")
                    }
                })
            }
        }
    }
}