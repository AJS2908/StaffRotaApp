package com.example.staffrotaapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class AdminLogin : AppCompatActivity() {

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // Views
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button

    // Shared Preferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val returnbut: Button = findViewById(R.id.returnbut)
        returnbut.setOnClickListener {
            val intent = Intent(this, AdminTimetable::class.java)
            startActivity(intent)
        }

        val gotoOwner: Button = findViewById(R.id.gotoownerlogin)
        gotoOwner.setOnClickListener {
            val intent = Intent(this, Owner_Login::class.java)
            startActivity(intent)
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins") // Reference the "Admins" node

        // Initialize Shared Preferences
        sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.adminLoginBut)

        // Set click listener for loginButton
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                authenticateAdmin(username, password)
            }
        }
    }

    // Function to authenticate admin
    private fun authenticateAdmin(username: String, password: String) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isAdminAuthenticated = false
                var adminId = ""
                for (adminSnapshot in snapshot.children) {
                    val admin = adminSnapshot.getValue(Admin::class.java)
                    if (admin != null && admin.username == username && admin.password == password) {
                        isAdminAuthenticated = true
                        adminId = adminSnapshot.key ?: ""
                        break
                    }
                }
                if (isAdminAuthenticated) {
                    // Save admin ID to SharedPreferences
                    saveAdminId(adminId)
                    // Authentication successful, proceed to next activity
                    val intent = Intent(this@AdminLogin, AdminHome::class.java)
                    // Pass admin ID to the next activity
                    intent.putExtra("adminId", adminId)
                    startActivity(intent)
                    finish()
                } else {
                    // Authentication failed
                    Toast.makeText(this@AdminLogin, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@AdminLogin, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to save admin ID to SharedPreferences
    private fun saveAdminId(adminId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("adminId", adminId)
        editor.apply()
    }
}

