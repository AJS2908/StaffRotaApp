package com.example.staffrotaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.text.SimpleDateFormat
import java.util.*

class TimeTableEdit : AppCompatActivity() {

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var dateCalendarView: CalendarView
    private lateinit var saveButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var shiftId: String // Assuming you have the shift ID

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_edit)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Timetable")

        // Retrieve shift ID passed from the previous activity
        shiftId = intent.getStringExtra("shiftId") ?: ""

        // Debugging statement to check the received shift ID
        Log.d("TimeTableEdit", "Shift ID received: $shiftId")

        // Initialize UI components
        startTimeEditText = findViewById(R.id.startTime)
        endTimeEditText = findViewById(R.id.endTime)
        dateCalendarView = findViewById(R.id.shiftDate)
        saveButton = findViewById(R.id.ConfirmEdit)

        // Set up click listener for the save button
        saveButton.setOnClickListener {
            // Get the edited shift data from the UI components
            val startTime = startTimeEditText.text.toString()
            val endTime = endTimeEditText.text.toString()
            val dateMillis = dateCalendarView.date
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val date = sdf.format(Date(dateMillis))

            // Calculate the shift length
            val duration = calculateDuration(startTime, endTime)

            // Update the shift data in the Firebase Realtime Database
            updateShiftData(startTime, endTime, date, duration)

            // Display a toast message indicating that the data has been updated
            Toast.makeText(this, "Shift data updated successfully", Toast.LENGTH_SHORT).show()
        }
    }


    private fun calculateDuration(startTime: String, endTime: String): String {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val startDate = sdf.parse(startTime)
        val endDate = sdf.parse(endTime)
        val diff = endDate.time - startDate.time
        val hours = diff / (60 * 60 * 1000)
        val minutes = (diff % (60 * 60 * 1000)) / (60 * 1000)
        return String.format("%02d:%02d", hours, minutes)
    }

    private fun updateShiftData(startTime: String, endTime: String, date: String, duration: String) {
        // Reference the specific shift node using shiftId
        val shiftRef = reference.child(shiftId)

        // Update only the specified fields under the shiftID node
        val updates = mapOf(
            "startTime" to startTime,
            "endTime" to endTime,
            "shiftDate" to date,
            "shiftLength" to duration
        )
        shiftRef.updateChildren(updates)
            .addOnSuccessListener {
                // Handle success, if needed
                Toast.makeText(this, "Shift data updated successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                // Handle failure
                Toast.makeText(this, "Failed to update shift data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }




}

