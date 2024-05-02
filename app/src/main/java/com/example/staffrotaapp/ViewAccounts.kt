package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.SearchView
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

    // List to hold all accounts
    private lateinit var allAccounts: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_accounts)


        // Retrieve admin ID from intent extras
        val addminId = intent.getStringExtra("addminId")

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

        // Initialize allAccounts list
        allAccounts = mutableListOf()

        // Set up click listeners for buttons
        val accountViewReturn: Button = findViewById(R.id.viewAccountsReturn)
        accountViewReturn.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
            }
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

        val createAccount: Button = findViewById(R.id.createAccount)
        createAccount.setOnClickListener {
            val intent = Intent(this, CreateNewAccount::class.java).apply {
            }
            intent.putExtra("addminId", addminId)
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
                intent.putExtra("addminId", addminId)
                startActivity(intent)
            }
        }

        // Set up SearchView
        val searchAccounts: SearchView = findViewById(R.id.searchAccounts)
        searchAccounts.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterAccounts(it) }
                return true
            }
        })

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
                        allAccounts.add(accountInfo)
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

    // Function to filter accounts based on search query
    private fun filterAccounts(query: String) {
        accountAdapter.clear()
        allAccounts.filter { it.contains(query, ignoreCase = true) }
            .forEach { filteredAccount ->
                accountAdapter.add(filteredAccount)
            }
        accountAdapter.notifyDataSetChanged()
    }
}


