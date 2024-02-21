package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class ViewAccounts : AppCompatActivity() {

    // Firebase authentication instance
    private lateinit var auth: FirebaseAuth

    // ListView to display accounts
    private lateinit var accountListView: ListView

    // ArrayAdapter to populate the ListView
    private lateinit var accountAdapter: ArrayAdapter<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_accounts)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

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

        // Fetch and display accounts from Firebase Authentication
        fetchAccounts()
    }

    // Function to fetch accounts from Firebase Authentication
    private fun fetchAccounts() {
        val currentUser: FirebaseUser? = auth.currentUser
        if (currentUser != null) {
            val userEmail: String? = currentUser.email
            if (userEmail != null) {
                accountAdapter.add(userEmail)
                accountAdapter.notifyDataSetChanged()
            }
        }
    }
}

