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

        reqAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())
        conAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, mutableListOf())

        val changeToBooked: Button = findViewById(R.id.upcomingHoliday)
        val changeToRequests: Button = findViewById(R.id.currentRequests)
        val ShowBookedHoliday: ListView = findViewById(R.id.conHolList)
        val ShowHolidayRequests: ListView = findViewById(R.id.holRequestsList)

        // Set adapter for ShowHolidayRequests after initializing holAdapter
        ShowHolidayRequests.adapter = reqAdapter
        ShowBookedHoliday.adapter = conAdapter

        changeToBooked.setOnClickListener {
            ShowHolidayRequests.visibility = View.INVISIBLE
            ShowBookedHoliday.visibility = View.VISIBLE
        }

        changeToRequests.setOnClickListener {
            ShowHolidayRequests.visibility = View.VISIBLE
            ShowBookedHoliday.visibility = View.INVISIBLE
        }

        val adminId = intent.getStringExtra("adminId")
        val holidayRetBut: Button = findViewById(R.id.holidayRetBut)
        holidayRetBut.setOnClickListener {
            val intent = Intent(this@AdminHoliday, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        fetchRequests()
        fetchConHol()
    }

    private fun fetchRequests() {
        val requestsReference = FirebaseDatabase.getInstance().getReference("Holiday/requests")

        requestsReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val requestsList = mutableListOf<String>() // List to hold employee information
                for (requestsSnapshot in snapshot.children) {
                    val requests = requestsSnapshot.getValue(HolidayHelper::class.java)
                    if (requests != null) {
                        val requestsInfo = "${requests.holidayID}: ${requests.employeeID}, Start: ${requests.startDate}, End: ${requests.endDate}"
                        requestsList.add(requestsInfo)
                    }
                }
                reqAdapter.addAll(requestsList)
                setItemClickListener()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("fetchRequests", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }


    private fun fetchConHol() {
        val confirmedReference = FirebaseDatabase.getInstance().getReference("Holiday/confirmed")

        confirmedReference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val confirmedList = mutableListOf<String>() // List to hold employee information
                for (confirmedSnapshot in snapshot.children) {
                    val confirmed = confirmedSnapshot.getValue(HolidayHelper::class.java)
                    if (confirmed != null) {
                        val confirmedInfo = "${confirmed.holidayID}: ${confirmed.employeeID}, Start: ${confirmed.startDate}, End: ${confirmed.endDate}"
                        confirmedList.add(confirmedInfo)
                    }
                }
                conAdapter.addAll(confirmedList)
                setItemClickListener()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("fetchConfirmed", "Error fetching Requests: ${error.message}", error.toException())
            }
        })
    }

    private fun setItemClickListener() {
        val ShowHolidayRequests: ListView = findViewById(R.id.holRequestsList)
        ShowHolidayRequests.setOnItemLongClickListener { _, _, position, _ ->
            if (position >= 0 && position < reqAdapter.count) {
                val holidayInfo = reqAdapter.getItem(position)
                val holidayID = holidayInfo?.substringBefore(":")?.toIntOrNull()
                confirmHoliday(holidayID)
            }
            true
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
                                        finish() // Finish activity after confirming
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
