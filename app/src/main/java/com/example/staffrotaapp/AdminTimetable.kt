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

class AdminTimetable : AppCompatActivity() {

    private lateinit var calendarView: CalendarView
    private lateinit var shiftsListView: ListView
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var shiftsAdapter: ArrayAdapter<String>
    private val shiftsList = mutableListOf<String>()

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_admin_timetable)

        // Retrieve admin ID from intent extras
        val addminId = intent.getStringExtra("addminId")

        // Initialize UI elements
        calendarView = findViewById(R.id.DateShift)
        shiftsListView = findViewById(R.id.Shifts)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Timetable")

        // Initialize the shifts adapter and set it to the shiftsListView
        shiftsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shiftsList)
        shiftsListView.adapter = shiftsAdapter

        // Set click listener for shiftsListView
        shiftsListView.setOnItemClickListener { parent, view, position, id ->
            // Get the selected item which contains both shift ID and shift information
            val selectedItem = parent.getItemAtPosition(position) as? String
            // Extract shift ID from the selected item
            val shiftId = selectedItem?.substringBefore(":")
            // Create intent to navigate to TimeTableEdit activity and pass shift ID
            val intent = Intent(this@AdminTimetable, TimeTableEdit::class.java).apply {
                putExtra("shiftId", shiftId)
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Set listener for calendarView to fetch shifts for the selected date
        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create a LocalDate object with the selected date
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Format the date with zero-padded month and day
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            // Call the method to fetch shifts using the formatted date
            fetchShiftsForDate(formattedDate)
        }

        // Set click listener for the button to navigate to AdminHome activity
        val timetableHomeButton: Button = findViewById(R.id.timetablehomebut)
        timetableHomeButton.setOnClickListener {
            // Create intent to navigate to AdminHome activity and pass admin ID
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }
    }

    // Function to fetch shifts for a specific date
    private fun fetchShiftsForDate(date: String) {
        reference.orderByChild("shiftDate").equalTo(date).addListenerForSingleValueEvent(object :
            ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shiftsList.clear() // Clears existing shifts list
                // Iterate through each shift data
                for (shiftSnapshot in snapshot.children) {
                    val shiftId = shiftSnapshot.key // Get the shift ID
                    val shiftData = shiftSnapshot.getValue(TimeTableDC::class.java)
                    shiftData?.let {
                        // Construct shift information string
                        val shiftInfo = "Employee: ${it.employeeData}, Start Time: ${it.startTime}, End Time: ${it.endTime}"
                        // Combines shift ID and shift information
                        shiftsList.add("$shiftId: $shiftInfo")
                    }
                }
                // Notify adapter of data changes
                shiftsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Log.e("Timetable", "Error fetching shifts: ${error.message}")
            }
        })
    }


}

