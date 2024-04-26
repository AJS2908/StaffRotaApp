package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewAccounts : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // ListView to display accounts
    private lateinit var accountListView: ListView

    // ArrayAdapter to populate the ListView
    private lateinit var accountAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_accounts)

        // Retrieve admin ID from intent extras
        val adminId = intent.getStringExtra("adminId")

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")

        // Initialize ListView
        accountListView = findViewById(R.id.ViewAccountView)

        // Initialize adapter for ListView
        accountAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        accountListView.adapter = accountAdapter

        // Set up click listeners for buttons
        val accountViewReturn: Button = findViewById(R.id.viewAccountsReturn)
        accountViewReturn.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        val createAccount: Button = findViewById(R.id.createAccount)
        createAccount.setOnClickListener {
            val intent = Intent(this, CreateNewAccount::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        // Set up item click listener for accountListView
        accountListView.setOnItemClickListener { parent, view, position, id ->
            val selectedItem = parent.getItemAtPosition(position) as? String
            val employeeId = selectedItem?.substringBefore(":")?.trim()
            if (employeeId != null) {
                val intent = Intent(this@ViewAccounts, EditEmployee::class.java).apply {
                    putExtra("employeeId", employeeId.toInt())
                }
                startActivity(intent)
            }
        }

        // Fetch and display accounts from Firebase Realtime Database
        fetchAccounts()
    }

    // Function to fetch accounts from Firebase Realtime Database
    private fun fetchAccounts() {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (employeeSnapshot in snapshot.children) {
                    val employee = employeeSnapshot.getValue(Employee::class.java)
                    if (employee != null) {
                        val accountInfo = "${employee.employeeId}: ${employee.firstName} ${employee.lastName}, ${employee.email}"
                        accountAdapter.add(accountInfo)
                    }
                }
                accountAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                println("Error: ${error.message}")
            }
        })
    }
}

