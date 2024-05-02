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
import android.widget.Toast
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

        // Initialize views
        dateCalendarView = findViewById(R.id.shiftDate)
        employeeRecyclerView = findViewById(R.id.employeeList)
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set date change listener for the calendar view
        dateCalendarView.setOnDateChangeListener { view, year, month, dayOfMonth ->
            // Set the selectedDate property with the selected date
            selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            // Log the selected date for debugging
            Log.d("shiftAssignment", "Selected date: $selectedDate")
        }

        // Initialize the employee adapter for the recycler view
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

        // Set adapter for the recycler view
        employeeRecyclerView.adapter = employeeAdapter

        // Set search query listener for the search view
        val searchBar: SearchView = findViewById(R.id.searchEmployee)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrEmpty()) {
                    // If query is empty, show all employees
                    filteredEmployeesList.clear()
                    filteredEmployeesList.addAll(originalEmployeesList)
                    employeeAdapter.notifyDataSetChanged()
                    employeeRecyclerView.visibility = View.INVISIBLE
                } else {
                    // Filter employees based on the query
                    filterEmployees(newText)
                    employeeRecyclerView.visibility = View.VISIBLE
                }
                return true
            }
        })

        // Retrieve the adminId passed from the previous activity
        val addminId = intent.getStringExtra("addminId")
        Log.d("Admin id", "addy id: $addminId")

        // Set click listener for the return button
        val shiftReturnButton: Button = findViewById(R.id.shiftRetBut)
        shiftReturnButton.setOnClickListener {
            // Navigate back to AdminHome activity
            val intent = Intent(this, AdminHome::class.java).apply {
            }
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

        // Set click listener for the save shift button
        val saveShiftButton: Button = findViewById(R.id.saveShift)
        saveShiftButton.setOnClickListener {
            val startTimeText = findViewById<TextView>(R.id.startTime).text.toString()
            val endTimeText = findViewById<TextView>(R.id.endTime).text.toString()

            // Check if start time and end time are not empty
            if (startTimeText.isNotEmpty() && endTimeText.isNotEmpty()) {
                // Check if start time and end time have the correct format
                if (startTimeText.matches(Regex("\\d{2}:\\d{2}")) && endTimeText.matches(Regex("\\d{2}:\\d{2}"))) {
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
                        // Log error if end time is not after start time
                        Log.e("ShiftAssignment", "End time must be after start time")
                    }
                } else {
                    // Notify the user that both start and end times should be in the format HH:mm
                    Toast.makeText(this, "Please enter start and end times in HH:mm format", Toast.LENGTH_SHORT).show()
                }
            } else {
                // Notify the user that both start and end times are required
                Toast.makeText(this, "Please enter both start and end times", Toast.LENGTH_SHORT).show()
            }
        }


        // Fetch employees from the database
        fetchEmployees()
    }

    private fun fetchEmployees() {
        // Retrieve all employees from the database
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Clear the original list of employees
                originalEmployeesList.clear()
                // Iterate through the snapshot to retrieve employee details
                for (employeeSnapshot in snapshot.children) {
                    // Get employee object from the snapshot
                    val employee = employeeSnapshot.getValue(Employee::class.java)
                    employee?.let {
                        // Create a string containing employee information
                        val employeeInfo = "${it.employeeId}: ${it.firstName} ${it.lastName}, ${it.email}"
                        // Add employee information to the original list
                        originalEmployeesList.add(employeeInfo)
                    }
                }
                // Clear the filtered list and add all items from the original list
                filteredEmployeesList.clear()
                filteredEmployeesList.addAll(originalEmployeesList)
                // Notify the adapter that the dataset has changed
                employeeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error message if fetching employees is cancelled
                Log.e("ShiftAssignment", "Error fetching employees: ${error.message}")
            }
        })
    }

    private fun filterEmployees(query: String) {
        // Clear the filtered list
        filteredEmployeesList.clear()
        // Iterate through the original list to filter employees based on the query
        for (employeeInfo in originalEmployeesList) {
            if (employeeInfo.contains(query, ignoreCase = true)) {
                // Add the employee to the filtered list if it matches the query
                filteredEmployeesList.add(employeeInfo)
            }
        }
        // Notify the adapter that the dataset has changed
        employeeAdapter.notifyDataSetChanged()
    }


    private fun generateShiftId(startTime: LocalTime, endTime: LocalTime, selectedDate: LocalDate) {
        // Retrieve employee data from TextView
        val employeeData = findViewById<TextView>(R.id.employeeAssigned).text.toString()
        // Reference to the "Timetable" node in the Firebase Database
        reference = database.getReference("Timetable")
        // Retrieve the last shift ID from the database
        reference.orderByChild("shiftID").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            @RequiresApi(Build.VERSION_CODES.O)
            override fun onDataChange(snapshot: DataSnapshot) {
                var maxId: Long = 0L // Initialize maxId as Long
                for (shiftSnapshot in snapshot.children) {
                    // Retrieve shift ID as String from the database
                    val shiftIdString = shiftSnapshot.child("shiftID").getValue(String::class.java)
                    // Convert shift ID to Long
                    val shiftId = shiftIdString?.toLongOrNull()
                    shiftId?.let {
                        // Update maxId if the current shift ID is greater
                        if (it > maxId) {
                            maxId = it
                        }
                    }
                }
                // Create a new shift ID by incrementing the maxId
                val newId: String = (maxId + 1L).toString()
                // Calculate shift length in hours
                val shiftLength = Duration.between(startTime, endTime).toMinutes().toDouble() / 60.0
                // Format startTime as HH:mm
                val startTimeString = startTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                // Format endTime as HH:mm
                val endTimeString = endTime.format(DateTimeFormatter.ofPattern("HH:mm"))
                // Format selected date using custom function
                val selectedDateString = TimeTableDC.formatLocalDate(selectedDate)
                selectedEmployeeId?.let { employeeId ->
                    // Create TimeTableDC object for the new shift
                    val shift = TimeTableDC(
                        shiftID = newId,
                        shiftDate = selectedDateString,
                        startTime = startTimeString,
                        endTime = endTimeString,
                        shiftLength = shiftLength,
                        employeeData = employeeData
                    )
                    // Save the new shift to the database
                    saveShiftToDatabase(shift)
                    Log.d("ShiftAssignment", "Shift ID generated and saved to database")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Log error message if generating shift ID is canceled
                Log.e("ShiftAssignment", "Error generating shift ID: ${error.message}")
            }
        })
    }

    private fun saveShiftToDatabase(shift: TimeTableDC) {
        // Save the shift to the database under its shift ID
        reference.child(shift.shiftID).setValue(shift)
            .addOnSuccessListener {
                // Log success message if shift is saved successfully
                Log.d("ShiftAssignment", "Shift saved successfully")
                // Navigate to ShiftAssignment activity
                val addminId = intent.getStringExtra("addminId")
                val intent = Intent(this, ShiftAssignment::class.java).apply {
                    putExtra("addminId", addminId)
                }
                startActivity(intent)
            }
            .addOnFailureListener { e ->
                // Log error message if saving the shift fails and print the stack trace so you can identify the start of the error
                Log.e("ShiftAssignment", "Error saving shift: ${e.message}", e)
                e.printStackTrace()
            }
    }

}
