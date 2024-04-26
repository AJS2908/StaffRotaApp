package com.example.staffrotaapp

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.CalendarView
import android.widget.SearchView
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.time.Duration
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter


class ShiftAssignment : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var employeeRecyclerView: RecyclerView
    private lateinit var employeeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private lateinit var dateCalendarView: CalendarView
    private val originalEmployeesList = mutableListOf<String>()
    private val filteredEmployeesList = mutableListOf<String>()
    private var selectedEmployeeId: String? = ""
    private var selectedDate: LocalDate? = null

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_assignment)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")
        dateCalendarView = findViewById(R.id.shiftDate)
        employeeRecyclerView = findViewById(R.id.employeeList)
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)

        dateCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Set the selectedDate property with the selected date
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Log the selected date for debugging
            Log.d("shiftAssignment", "Selected date: $selectedDate")
        }

        employeeAdapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val textView = holder.itemView as TextView
                textView.text = filteredEmployeesList[position]
                textView.setOnClickListener {
                    selectedEmployeeId = filteredEmployeesList[position].split(":")[0].trim()
                    findViewById<TextView>(R.id.employeeAssigned).text = textView.text.toString() // Update TextView here
                    employeeRecyclerView.visibility = View.GONE
                }
            }

            override fun getItemCount(): Int = filteredEmployeesList.size
        }

        employeeRecyclerView.adapter = employeeAdapter

        val searchBar: SearchView = findViewById(R.id.searchEmployee)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    filteredEmployeesList.clear()
                    filteredEmployeesList.addAll(originalEmployeesList)
                    employeeAdapter.notifyDataSetChanged()
                    employeeRecyclerView.visibility = View.INVISIBLE
                } else {
                    filterEmployees(newText)
                    employeeRecyclerView.visibility = View.VISIBLE
                }
                return true
            }
        })

        //carries the current user to the new window
        val adminId = intent.getStringExtra("adminId")

        val shiftReturnButton: Button = findViewById(R.id.shiftRetBut)
        shiftReturnButton.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        // Save shift button click listener
        val saveShiftButton: Button = findViewById(R.id.saveShift)
        saveShiftButton.setOnClickListener {
            val startTimeText = findViewById<TextView>(R.id.startTime).text.toString()
            val endTimeText = findViewById<TextView>(R.id.endTime).text.toString()

            // Parse start time and end time into LocalTime objects
            val startTime = TimeTableDC.parseLocalTime(startTimeText)
            val endTime = TimeTableDC.parseLocalTime(endTimeText)

            // Check if end time is after start time
            if (endTime.isAfter(startTime)) {
                // Proceed to save the shift using the captured values
                selectedEmployeeId?.let { employeeId ->
                    selectedDate?.let { date ->
                        generateShiftId(startTime, endTime, date)
                    }
                }
            } else {
                Log.e("ShiftAssignment", "End time must be after start time")
            }
        }

        fetchEmployees()
    }

    private fun fetchEmployees() {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                originalEmployeesList.clear()
                for (employeeSnapshot in snapshot.children) {
                    val employee = employeeSnapshot.getValue(Employee::class.java)
                    employee?.let {
                        val employeeInfo = "${it.employeeId}: ${it.firstName} ${it.lastName}, ${it.email}"
                        originalEmployeesList.add(employeeInfo)
                    }
                }
                filteredEmployeesList.clear()
                filteredEmployeesList.addAll(originalEmployeesList)
                employeeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ShiftAssignment", "Error fetching employees: ${error.message}")
            }
        })
    }

    private fun filterEmployees(query: String) {
        filteredEmployeesList.clear()
        for (employeeInfo in originalEmployeesList) {
            if (employeeInfo.contains(query, ignoreCase = true)) {
                filteredEmployeesList.add(employeeInfo)
            }
        }
        employeeAdapter.notifyDataSetChanged()
    }

    private fun generateShiftId(startTime: LocalTime, endTime: LocalTime, selectedDate: LocalDate) {
        val employeeData = findViewById<TextView>(R.id.employeeAssigned).text.toString()
        reference = database.getReference("Timetable")
        reference.orderByChild("shiftID").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                var maxId: Long = 0L // Initialize maxId as Long
                for (shiftSnapshot in snapshot.children) {
                    val shiftIdString = shiftSnapshot.child("shiftID").getValue(String::class.java)
                    val shiftId = shiftIdString?.toLongOrNull()
                    shiftId?.let {
                        if (it > maxId) {
                            maxId = it
                        }
                    }
                }
                reference = database.getReference("Timetable")
                val newId: String = (maxId + 1L).toString()
                val shiftLength = Duration.between(startTime, endTime).toMinutes().toDouble() / 60.0
                val startTimeString = startTime.format(DateTimeFormatter.ofPattern("HH:mm")) // Format startTime
                val endTimeString = endTime.format(DateTimeFormatter.ofPattern("HH:mm")) // Format endTime
                val selectedDateString = TimeTableDC.formatLocalDate(selectedDate) // Format selected date
                selectedEmployeeId?.let { employeeId ->
                    val shift = TimeTableDC(
                        shiftID = newId,
                        shiftDate = selectedDateString,
                        startTime = startTimeString,
                        endTime = endTimeString,
                        shiftLength = shiftLength,
                        employeeData = employeeData
                    )
                    saveShiftToDatabase(shift)
                    Log.d("ShiftAssignment", "Shift ID generated and saved to database")

                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("ShiftAssignment", "Error generating shift ID: ${error.message}")
            }
        })
    }
    private fun saveShiftToDatabase(shift: TimeTableDC) {
        reference.child(shift.shiftID).setValue(shift)
            .addOnSuccessListener {
                Log.d("ShiftAssignment", "Shift saved successfully")
                // Navigate to AdminHome activity
                val adminId = intent.getStringExtra("adminId")
                val intent = Intent(this, ShiftAssignment::class.java).apply {
                    putExtra("adminId", adminId)
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                Log.e("ShiftAssignment", "Error saving shift: ${e.message}", e)
                // Optionally, you can print the stack trace for more detailed error information
                e.printStackTrace()
            }
    }
}
