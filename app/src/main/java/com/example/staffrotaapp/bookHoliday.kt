package com.example.staffrotaapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.CalendarView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class bookHoliday : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var startDateCalendarView: CalendarView
    private lateinit var endDateCalendarView: CalendarView
    private lateinit var confirmBooking: Button
    private var startDate: LocalDate? = null
    private var endDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_holiday)
        startDateCalendarView = findViewById(R.id.hStart)
        endDateCalendarView = findViewById(R.id.hEnd)
        confirmBooking = findViewById(R.id.conBook)

        startDateCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Set the startDate property with the selected date
            startDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Log the selected start date for debugging
            Log.d("shiftAssignment", "Selected date: $startDate")
        }
        endDateCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Set the endDate property with the selected date
            endDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Log the selected end date for debugging
            Log.d("shiftAssignment", "Selected date: $endDate")
        }


        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Holiday/requests") // Reference to the 'requests' node under 'Holiday'

        confirmBooking.setOnClickListener {
            getEmployeeDetailsForCurrentUser(startDate, endDate)
        }

    }

    fun generateHolidayId(startDate: LocalDate, endDate: LocalDate, employeeId: Int, employeeData: String) {
        val reference = this.reference // Use the class-level reference
        reference.orderByChild("holidayID").limitToLast(1).addListenerForSingleValueEvent(object :
            ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                var maxId: Long = 0L // Initialize maxId as Long
                for (holidaySnapshot in snapshot.children) {
                    val holidayIdString = holidaySnapshot.child("holidayID").getValue(Long::class.java)
                    holidayIdString?.let {
                        if (it > maxId) {
                            maxId = it
                        }
                    }
                }
                val newId: String = (maxId + 1L).toString()
                val startDateString = startDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val endDateString = endDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
                val holiday = HolidayHelper(
                    holidayID = newId.toInt(),
                    startDate = startDateString,
                    endDate = endDateString,
                    employeeData = employeeData,
                    employeeID = employeeId
                )
                saveHolidayToDatabase(holiday)
                Log.d("Holidays", "Holiday ID generated and saved to database")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Holidays", "Error generating holiday ID: ${error.message}")
            }
        })
    }

    fun getEmployeeDetailsForCurrentUser(startDate: LocalDate?, endDate: LocalDate?) {
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
                        var employeeData: String? = null
                        var employeeId: Int? = 0 // Changed initialization to null to avoid unnecessary default value
                        for (employeeSnapshot in dataSnapshot.children) {
                            val employee = employeeSnapshot.getValue(Employee::class.java)
                            // If employee with given email is found
                            if (employee != null) {
                                println("Employee found - ID: ${employee.employeeId}, Name: ${employee.firstName} ${employee.lastName}")
                                // Extract employee's full name and ID
                                employeeData = "${employee.firstName} ${employee.lastName}"
                                employeeId = employee.employeeId
                                // Check if startDate and endDate are not null before calling generateHolidayId
                                startDate?.let { startDate ->
                                    endDate?.let { endDate ->
                                        generateHolidayId(startDate, endDate, employeeId, employeeData)
                                    }
                                }
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

    private fun saveHolidayToDatabase(holiday: HolidayHelper) {
        reference.child(holiday.holidayID.toString()).setValue(holiday)
            .addOnSuccessListener {
                Log.d("Holiday booking", "request saved successfully")
                // Navigate to AdminHome activity
                val intent = Intent(this@bookHoliday, Holiday::class.java)
                startActivity(intent)
                finish() // Finish the current activity to prevent navigating back to it from the next activity
            }
            .addOnFailureListener { e ->
                Log.e("Holiday booking", "Error saving Holiday: ${e.message}", e)
                // Optionally, you can print the stack trace for more detailed error information
                e.printStackTrace()
            }
    }

}
