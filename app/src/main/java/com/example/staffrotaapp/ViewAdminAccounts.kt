package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
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

    // List to hold all admin accounts
    private lateinit var allAdmins: MutableList<String>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_admin_accounts)

        // Initialize Firebase authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins")

        // Initialize ListView
        accountListView = findViewById(R.id.ViewAdminListview)

        // Initialize adapter for ListView
        accountAdapter = ArrayAdapter(this, android.R.layout.simple_list_item_1)
        accountListView.adapter = accountAdapter

        // Initialize allAdmins list
        allAdmins = mutableListOf()

        val addminId = intent.getStringExtra("addminId")

        val accountViewReturn: Button = findViewById(R.id.viewAdminAccountsReturn)
        accountViewReturn.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

        val createAccount: Button = findViewById(R.id.addNewAdAccountBut)
        createAccount.setOnClickListener {
            val intent = Intent(this, AddNewAdmin::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

        // Set long click listener for the ListView items
        accountListView.setOnItemLongClickListener { _, _, position, _ ->
            val adminInfo = accountAdapter.getItem(position) // Get the item at the clicked position
            val adminId = adminInfo?.split(":")?.get(0) // Split the string to get the adminId
            val intent = Intent(this, EditAdminAccount::class.java)
            intent.putExtra("adminId", adminId)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
            true // finish the long click event
        }

        // Set up SearchView
        val adminSearch: SearchView = findViewById(R.id.adminSearch)
        adminSearch.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let { filterAdmins(it) }
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
                for (adminSnapshot in snapshot.children) {
                    val admin = adminSnapshot.getValue(Admin::class.java)
                    if (admin != null) {
                        val accountInfo = "${admin.adminId}: ${admin.firstName} ${admin.lastName}, ${admin.email}"
                        allAdmins.add(accountInfo)
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

    // Function to filter admins based on search query
    private fun filterAdmins(query: String) {
        accountAdapter.clear()
        allAdmins.filter { it.contains(query, ignoreCase = true) }
            .forEach { filteredAdmin ->
                accountAdapter.add(filteredAdmin)
            }
        accountAdapter.notifyDataSetChanged()
    }
}

