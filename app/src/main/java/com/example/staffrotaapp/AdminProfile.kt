package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminProfile : AppCompatActivity() {

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // Views
    private lateinit var adminIdTextView: TextView
    private lateinit var usernameTextView: TextView
    private lateinit var firstNameTextView: TextView
    private lateinit var lastNameTextView: TextView
    private lateinit var emailTextView: TextView
    private lateinit var nINumberTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_profile)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins")

        // Initialize views
        adminIdTextView = findViewById(R.id.adminIdTextView)
        usernameTextView = findViewById(R.id.usernameTextView)
        firstNameTextView = findViewById(R.id.firstNameTextView)
        lastNameTextView = findViewById(R.id.lastNameTextView)
        emailTextView = findViewById(R.id.emailTextView)
        nINumberTextView = findViewById(R.id.nINumberTextView)

        // Fetch and display admin profile
        val adminId = intent.getStringExtra("adminId")
        adminId?.let { fetchAdminProfile(it) }

        val AdProfile: Button = findViewById(R.id.AdmProfilehomebut)
        AdProfile.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }
    }

    private fun fetchAdminProfile(adminId: String) {
        reference.child(adminId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val admin = snapshot.getValue(Admin::class.java)
                    admin?.let {
                        adminIdTextView.text = "Admin ID: ${snapshot.key}"
                        usernameTextView.text = "Username: ${admin.username}"
                        firstNameTextView.text = "First Name: ${admin.firstName}"
                        lastNameTextView.text = "Last Name: ${admin.lastName}"
                        emailTextView.text = "Email: ${admin.email}"
                        nINumberTextView.text = "NIN: ${admin.nINumber}"
                    }
                } else {
                    Log.d("AdminProfile", "Snapshot does not exist")
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                println("Error: ${error.message}")
            }
        })
    }
}