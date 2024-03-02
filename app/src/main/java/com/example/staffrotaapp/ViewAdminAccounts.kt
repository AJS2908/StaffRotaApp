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

class ViewAdminAccounts : AppCompatActivity() {

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
        setContentView(R.layout.activity_view_admin_accounts)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins") // Reference the "Admins" node

        // Initialize ListView
        accountListView = findViewById(R.id.ViewAdminListview)

        // Initialize adapter for ListView
        accountAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        accountListView.adapter = accountAdapter

        val accountViewReturn: Button = findViewById(R.id.viewAdminAccountsReturn)
        accountViewReturn.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
        }

        val createAccount: Button = findViewById(R.id.addNewAdAccountBut)
        createAccount.setOnClickListener {
            val intent = Intent(this, AddNewAdmin::class.java)
            startActivity(intent)
        }

        // Fetch and display accounts from Firebase Realtime Database
        fetchAccounts()
    }

    // Function to fetch accounts from Firebase Realtime Database
    private fun fetchAccounts() {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for (adminSnapshot in snapshot.children) {
                    val admin = adminSnapshot.getValue(Admin::class.java)
                    if (admin != null) {
                        val accountInfo = "${admin.adminId}: ${admin.firstName} ${admin.lastName}, ${admin.email}"
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