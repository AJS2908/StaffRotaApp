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

        calendarView = findViewById(R.id.DateShift)
        shiftsListView = findViewById(R.id.Shifts)
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Timetable")

        shiftsAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, shiftsList)
        shiftsListView.adapter = shiftsAdapter

        calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create a LocalDate object with the selected date
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Format the date with zero-padded month and day
            val formattedDate = selectedDate.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"))
            // Call the method to fetch shifts using the formatted date
            fetchShiftsForDate(formattedDate)
        }

        val TimetableretButton: Button = findViewById(R.id.button4)
        TimetableretButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }
    private fun fetchShiftsForDate(date: String) {
        reference.orderByChild("shiftDate").equalTo(date).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                shiftsList.clear()
                for (shiftSnapshot in snapshot.children) {
                    val shiftData = shiftSnapshot.getValue(TimeTableDC::class.java)
                    shiftData?.let {
                        val shiftInfo = "Employee: ${it.employeeData}, Start Time: ${it.startTime}, End Time: ${it.endTime}"
                        shiftsList.add(shiftInfo)
                    }
                }
                shiftsAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("Timetable", "Error fetching shifts: ${error.message}")
            }
        })
    }


}
