package com.example.staffrotaapp

import android.app.AlertDialog
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AdminHoliday : AppCompatActivity() {

    private lateinit var conAdapter: ArrayAdapter<String>
    private lateinit var reqAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_holiday)

        // Initialize adapters for request and confirmed holiday lists
        reqAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        conAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())

        // Initialize UI elements
        val changeToBooked: Button = findViewById(R.id.upcomingHoliday)
        val changeToRequests: Button = findViewById(R.id.currentRequests)
        val showBookedHoliday: ListView = findViewById(R.id.conHolList)
        val showHolidayRequests: ListView = findViewById(R.id.holRequestsList)

        // Set adapters for the holiday request and confirmed holiday lists
        showHolidayRequests.adapter = reqAdapter
        showBookedHoliday.adapter = conAdapter

        // Set click listener for the button to switch to the confirmed holiday list
        changeToBooked.setOnClickListener {
            showHolidayRequests.visibility = View.INVISIBLE
            showBookedHoliday.visibility = View.VISIBLE
        }

        // Set click listener for the button to switch to the holiday request list
        changeToRequests.setOnClickListener {
            showHolidayRequests.visibility = View.VISIBLE
            showBookedHoliday.visibility = View.INVISIBLE
        }

        // Retrieve adminId passed from previous activity
        val addminId = intent.getStringExtra("addminId")
        val holidayRetBut: Button = findViewById(R.id.holidayRetBut)
        holidayRetBut.setOnClickListener {
            // Create intent to navigate back to AdminHome activity
            val intent = Intent(this@AdminHoliday, AdminHome::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Fetch holiday requests and confirmed holidays from the database
        fetchRequests()
        fetchConHol()
    }

    private fun fetchRequests() {
        // Reference to the "requests" node in the database
        val requestsReference = FirebaseDatabase.getInstance().getReference("Holiday/requests")

        // Listen for a single data change event on the "requests" node
        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // List to hold employee holiday request information
                val requestsList = mutableListOf<String>()
                // Iterate through each child node under "requests"
                for (requestsSnapshot in snapshot.children) {
                    // Retrieve holiday request data
                    val requests = requestsSnapshot.getValue(HolidayHelper::class.java)
                    if (requests != null) {
                        // Construct a string representing holiday request info
                        val requestsInfo = "${requests.holidayID}: ${requests.employeeID}, Start: ${requests.startDate}, End: ${requests.endDate}"
                        // Add the holiday request info to the list
                        requestsList.add(requestsInfo)
                    }
                }
                // Add all holiday request info to the request adapter
                reqAdapter.addAll(requestsList)
                // Set item click listener for long clicks on holiday requests
                setItemClickListener()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("fetchRequests", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    private fun fetchConHol() {
        // Reference to the "confirmed" node in the database
        val confirmedReference = FirebaseDatabase.getInstance().getReference("Holiday/confirmed")

        // Listen for a single data change event on the "confirmed" node
        confirmedReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // List to hold confirmed holiday information
                val confirmedList = mutableListOf<String>()
                // Iterate through each child node under "confirmed"
                for (confirmedSnapshot in snapshot.children) {
                    // Retrieve confirmed holiday data
                    val confirmed = confirmedSnapshot.getValue(HolidayHelper::class.java)
                    if (confirmed != null) {
                        // Construct a string representing confirmed holiday info
                        val confirmedInfo = "${confirmed.holidayID}: ${confirmed.employeeID}, Start: ${confirmed.startDate}, End: ${confirmed.endDate}"
                        // Add the confirmed holiday info to the list
                        confirmedList.add(confirmedInfo)
                    }
                }
                // Add all confirmed holiday info to the confirmed adapter
                conAdapter.addAll(confirmedList)
                // Set item click listener for long clicks on confirmed holidays
                setItemClickListener()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("fetchConfirmed", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    private fun setItemClickListener() {
        // Reference to the ListView for holiday requests
        val ShowHolidayRequests: ListView = findViewById(R.id.holRequestsList)
        // Set item long click listener for long clicks on holiday requests
        ShowHolidayRequests.setOnItemLongClickListener { _, _, position, _ ->
            if (position >= 0 && position < reqAdapter.count) {
                // Get the holiday info at the clicked position
                val holidayInfo = reqAdapter.getItem(position)
                // Extract the holiday ID from the holiday info string
                val holidayID = holidayInfo?.substringBefore(":")?.toIntOrNull()
                // Call function to confirm the holiday with the extracted ID
                confirmHoliday(holidayID)
            }
            true // Indicate that the long click event is consumed
        }
    }


    private fun confirmHoliday(holidayID: Int?) {
        holidayID ?: return // Check if holidayID is null, if so, return early

        AlertDialog.Builder(this)
            .setTitle("Confirm Holiday")
            .setMessage("Do you want to confirm this Holiday?")
            .setPositiveButton("Yes") { dialog, which ->
                val database = FirebaseDatabase.getInstance()
                val requestsReference = database.getReference("Holiday/requests")
                val confirmedReference = database.getReference("Holiday/confirmed")

                // Retrieve the holiday data from requests
                requestsReference.child(holidayID.toString()).get().addOnSuccessListener { dataSnapshot ->
                    val holidayData = dataSnapshot.getValue(HolidayHelper::class.java)

                    // Move the holiday data to the confirmed path
                    if (holidayData != null) {
                        confirmedReference.child(holidayID.toString()).setValue(holidayData)
                            .addOnSuccessListener {
                                // Remove the holiday data from the requests path
                                requestsReference.child(holidayID.toString()).removeValue()
                                    .addOnSuccessListener {
                                        finish()
                                    }
                                    .addOnFailureListener { exception ->
                                        // Handle error: unable to remove holiday data from requests
                                        Toast.makeText(this, "Failed to remove holiday data from requests: ${exception.message}", Toast.LENGTH_SHORT).show()
                                    }
                            }
                            .addOnFailureListener { exception ->
                                // Handle error: unable to move holiday data to confirmed
                                Toast.makeText(this, "Failed to move holiday data to confirmed: ${exception.message}", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Handle error: holiday data is null
                        Toast.makeText(this, "Holiday data is null", Toast.LENGTH_SHORT).show()
                    }
                }
                    .addOnFailureListener { exception ->
                        // Handle error: unable to retrieve holiday data from requests
                        Toast.makeText(this, "Failed to retrieve holiday data from requests: ${exception.message}", Toast.LENGTH_SHORT).show()
                    }
            }
            .setNegativeButton("No") { dialog, which ->
                // Do nothing if user declines
            }
            .show()
    }
}
