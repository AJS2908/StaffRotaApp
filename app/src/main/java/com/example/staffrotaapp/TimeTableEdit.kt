package com.example.staffrotaapp

import android.app.AlertDialog
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import android.widget.EditText
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

class TimeTableEdit : AppCompatActivity() {

    private lateinit var startTimeEditText: EditText
    private lateinit var endTimeEditText: EditText
    private lateinit var dateCalendarView: CalendarView
    private lateinit var saveButton: Button

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private var formattedDate: String = ""

    private lateinit var shiftId: String // Assuming you have the shift ID

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_time_table_edit)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Timetable")

        // Retrieve shift ID passed from the previous activity
        shiftId = intent.getStringExtra("shiftId") ?: ""
        val addminId = intent.getStringExtra("addminId")

        // Debugging statement to check the received shift ID
        Log.d("TimeTableEdit", "Shift ID received: $shiftId")

        // Initialize UI components
        startTimeEditText = findViewById(R.id.startTime)
        endTimeEditText = findViewById(R.id.endTime)
        dateCalendarView = findViewById(R.id.shiftDate)
        saveButton = findViewById(R.id.ConfirmEdit)
        dateCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Create a LocalDate object with the selected date
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Format the date with zero-padded month and day
            formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            // Log the selected date for debugging
            Log.d("TimeTableEdit", "Selected date: $formattedDate")
        }

        val editShifRetButton: Button = findViewById(R.id.editShiftRetBut)
        editShifRetButton.setOnClickListener {
            val intent = Intent(this, AdminTimetable::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

        // Set up click listener for the save button
        saveButton.setOnClickListener {
            // Ensure formattedDate is not empty
            if (formattedDate.isNotEmpty()) {
                // Get the edited shift data from the UI components
                val startTime = startTimeEditText.text.toString()
                val endTime = endTimeEditText.text.toString()

                // Calculate the shift length
                val duration = calculateDuration(startTime, endTime)

                // Check if the shift ID is not empty
                if (shiftId.isNotEmpty()) {
                    // Retrieve employee data for the given shift ID
                    retrieveEmployeeData(shiftId) { employeeData ->
                        if (employeeData.isNotEmpty()) {
                            // Update the shift data in the Firebase Realtime Database
                            updateShiftData(startTime, endTime, formattedDate, duration, employeeData, shiftId)

                            // Display a toast message indicating that the data has been updated
                            Toast.makeText(this, "Shift data updated successfully", Toast.LENGTH_SHORT).show()
                        } else {
                            Toast.makeText(this, "Employee data is empty", Toast.LENGTH_SHORT).show()
                        }
                    }
                } else {
                    Toast.makeText(this, "Shift ID is empty", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "No date selected", Toast.LENGTH_SHORT).show()
            }
        }
        val deleteShiftButton: Button = findViewById(R.id.deleteShift)
        deleteShiftButton.setOnClickListener {
            // Call a function to delete the shift
            deleteShift(shiftId)
        }
    }

    private fun retrieveEmployeeData(shiftId: String, callback: (String) -> Unit) {
        val shiftRef = reference.child(shiftId)
        shiftRef.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val employeeData = snapshot.child("employeeData").value.toString()
                callback(employeeData)
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("TimeTableEdit", "Failed to retrieve employee data: ${error.message}")
                callback("")
            }
        })
    }

    private fun calculateDuration(startTime: String, endTime: String): Double {
        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val startTimeDate = sdf.parse(startTime)
        val endTimeDate = sdf.parse(endTime)
        val diff = endTimeDate.time - startTimeDate.time
        val hours = diff / (60 * 60 * 1000).toDouble() // Convert to double
        val minutes = (diff % (60 * 60 * 1000)) / (60 * 1000).toDouble() // Convert to double
        return hours + (minutes / 60.0) // Combine hours and minutes as double
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun updateShiftData(
        startTime: String,
        endTime: String,
        selectedDate: String,
        duration: Double,
        employeeData: String,
        shiftIdParam: String
    ) {
        val shiftRef = reference.child(shiftIdParam)

        // Update the specified fields along with employeeData and shiftID
        val updates = mapOf<String, Any>(
            "startTime" to startTime,
            "endTime" to endTime,
            "shiftDate" to selectedDate,
            "shiftLength" to duration,
            "employeeData" to employeeData,
            "shiftID" to shiftIdParam
        )

        shiftRef.updateChildren(updates)
            .addOnSuccessListener {
                Toast.makeText(this, "Shift data updated successfully", Toast.LENGTH_SHORT).show()
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Failed to update shift data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun deleteShift(shiftId: String) {
        AlertDialog.Builder(this)
            .setTitle("Delete Admin Account")
            .setMessage("Are you sure you want to delete this admin account?")
            .setPositiveButton("Yes") { dialog, which ->
                // Delete the admin account from the database
                reference.child(shiftId).removeValue()
                finish() // Finish activity after deleting
            }
            .setNegativeButton("No", null)
            .show()
    }
}