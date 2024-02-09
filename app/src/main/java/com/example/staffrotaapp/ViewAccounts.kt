package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.*

class ViewAccounts : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // Reference to the Firebase Realtime Database
    private lateinit var database: DatabaseReference

    // ListView to display accounts
    private lateinit var accountListView: ListView

    // ArrayAdapter to populate the ListView
    private lateinit var accountAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_accounts)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // Initialize database reference to the "users" node
        database = FirebaseDatabase.getInstance().reference.child("users")

        // Initialize ListView
        accountListView = findViewById(R.id.ViewAccountView)

        // Initialize adapter for ListView
        accountAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        accountListView.adapter = accountAdapter

        // Set up click listeners for buttons
        val accountViewReturn: Button = findViewById(R.id.viewAccountsReturn)
        accountViewReturn.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }

        val createAccount: Button = findViewById(R.id.createAccount)
        createAccount.setOnClickListener {
            val intent = Intent(this, CreateNewAccount::class.java)
            startActivity(intent)
        }

        // Fetch and display accounts from database
        fetchAccounts()
    }

    // Function to fetch accounts from the database
    private fun fetchAccounts() {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                // Clear the existing data in the adapter
                accountAdapter.clear()
                // Iterate through each child node
                for (snapshot in dataSnapshot.children) {
                    // Deserialize user data from the snapshot
                    val user = snapshot.getValue(User::class.java)
                    user?.let {
                        // Add username and email to the adapter
                        accountAdapter.add("${it.username} (${it.email})")
                    }
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {
                // Handle database error
            }
        })
    }

    // Data class to represent user data
    data class User(val username: String = "", val email: String = "")
}