package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.SearchView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class shiftAssignment : AppCompatActivity() {

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // RecyclerView to display employees
    private lateinit var employeeRecyclerView: RecyclerView

    // Adapter for RecyclerView
    private lateinit var employeeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>

    // List to hold employee data
    private val employeesList = mutableListOf<String>() // Declare employeesList here

    // ID of the selected employee
    private var selectedEmployeeId: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_assignment)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")

        // Initialize RecyclerView
        employeeRecyclerView = findViewById(R.id.employeeList)
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up RecyclerView Adapter
        employeeAdapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                val textView = holder.itemView as TextView
                textView.text = employeesList[position]
                textView.setOnClickListener {
                    // Capture the ID of the selected employee
                    selectedEmployeeId = employeesList[position].split(":")[0].trim()
                    // Hide the RecyclerView
                    employeeRecyclerView.visibility = View.GONE
                }
            }

            override fun getItemCount(): Int = employeesList.size
        }

        employeeRecyclerView.adapter = employeeAdapter

        // Set up click listener for the search bar
        val searchBar: SearchView = findViewById(R.id.searchEmployee)
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Toggle RecyclerView visibility based on search query
                if (newText.isNullOrEmpty()) {
                    employeeRecyclerView.visibility = View.INVISIBLE
                } else {
                    // Filter employees based on search query
                    filterEmployees(newText)
                    employeeRecyclerView.visibility = View.VISIBLE
                }
                return true
            }
        })

        // Set up return button click listener
        val shiftReturnButton: Button = findViewById(R.id.shiftRetBut)
        shiftReturnButton.setOnClickListener {
            // Navigate back to AdminHome
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }

        // Fetch and display employees from Firebase Realtime Database
        fetchEmployees()
    }

    // Function to fetch employees from Firebase Realtime Database
    private fun fetchEmployees() {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val employees = mutableListOf<String>()
                for (employeeSnapshot in snapshot.children) {
                    val employee = employeeSnapshot.getValue(Employee::class.java)
                    employee?.let {
                        val employeeInfo = "${it.employeeId}: ${it.firstName} ${it.lastName}, ${it.email}"
                        employees.add(employeeInfo)
                    }
                }

                // Update the adapter's data source with fetched employee data
                employeesList.clear() // Clear the previous data
                employeesList.addAll(employees)

                // Notify the adapter of the data change
                employeeAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Log.e("ShiftAssignment", "Error fetching employees: ${error.message}")
            }
        })
    }

    // Function to filter employees based on search query
    private fun filterEmployees(query: String) {
        val filteredList = employeesList.filter { it.contains(query, ignoreCase = true) }
        employeesList.clear()
        employeesList.addAll(filteredList)
        employeeAdapter.notifyDataSetChanged()
    }
}
