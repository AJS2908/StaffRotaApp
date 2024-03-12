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

    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference
    private lateinit var employeeRecyclerView: RecyclerView
    private lateinit var employeeAdapter: RecyclerView.Adapter<RecyclerView.ViewHolder>
    private val originalEmployeesList = mutableListOf<String>()
    private val filteredEmployeesList = mutableListOf<String>()
    private var selectedEmployeeId: String? = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_assignment)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")

        employeeRecyclerView = findViewById(R.id.employeeList)
        employeeRecyclerView.layoutManager = LinearLayoutManager(this)

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

        val adminId = intent.getStringExtra("adminId")

        val shiftReturnButton: Button = findViewById(R.id.shiftRetBut)
        shiftReturnButton.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
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
}
