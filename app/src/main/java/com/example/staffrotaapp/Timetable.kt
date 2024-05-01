package com.example.staffrotaapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.CalendarView
import android.widget.ListView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class Timetable : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var shiftsListView: ListView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var shiftsAdapter: ArrayAdapter<String>
    private val shiftsList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        // Initialize views
        calendarView = findViewById(R.id.DateShift)
        shiftsListView = findViewById(R.id.Shifts)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Timetable")

        // Initialize shifts adapter for the list view
        shiftsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shiftsList)
        shiftsListView.adapter = shiftsAdapter

        // Set date change listener for the calendar view
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create a LocalDate object with the selected date
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Format the date with zero-padded month and day
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            // Call the method to fetch shifts using the formatted date
            fetchShiftsForDate(formattedDate)
        }

        // Set click listener for the return button
        val TimetableretButton: Button = findViewById(R.id.button4)
        TimetableretButton.setOnClickListener {
            // Navigate back to HomeScreen activity
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }
    private fun fetchShiftsForDate(date: String) {
        // Query the database to retrieve shifts for the specified date
        reference.orderByChild("shiftDate").equalTo(date).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the existing shifts list
                shiftsList.clear()
                // Iterate through the retrieved shifts
                for (shiftSnapshot in snapshot.children) {
                    // Get shift data from the snapshot
                    val shiftData = shiftSnapshot.getValue(TimeTableDC::class.java)
                    shiftData?.let {
                        // Create shift information string
                        val shiftInfo = "Employee: ${it.employeeData}, Start Time: ${it.startTime}, End Time: ${it.endTime}"
                        // Add shift information to the list
                        shiftsList.add(shiftInfo)
                    }
                }
                // Notify the adapter that the data set has changed
                shiftsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Log an error message if fetching shifts is unsuccessful
                Log.e("Timetable", "Error fetching shifts: ${error.message}")
            }
        })
    }
}
