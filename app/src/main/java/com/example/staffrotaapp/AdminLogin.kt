package com.example.staffrotaapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
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
    private lateinit var passwordMask: Switch


    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_admin_login)

        // Initialize UI elements
        val returnButton: Button = findViewById(R.id.returnbut)
        val gotoOwnerButton: Button = findViewById(R.id.gotoownerlogin)
        passwordMask = findViewById(R.id.PassMask)
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.adminLoginBut)

        // Set click listener for the return button
        returnButton.setOnClickListener {
            // Navigate to MainActivity when return button is clicked
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Set click listener for the button to navigate to Owner_Login activity
        gotoOwnerButton.setOnClickListener {
            val intent = Intent(this, Owner_Login::class.java)
            startActivity(intent)
        }

        // Set password visibility toggle listener
        passwordMask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                passwordEditText.transformationMethod = null
            } else {
                // Hide password (masking it)
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // Initialize Firebase Database reference
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins")

        // Initialize Shared Preferences
        sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)

        // Set click listener for loginButton
        loginButton.setOnClickListener {
            // Get username and password from input fields
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT)
                    .show()
            } else {
                // Authenticate admin
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
                // Iterate through admin nodes
                for (adminSnapshot in snapshot.children) {
                    val admin = adminSnapshot.getValue(Admin::class.java)
                    if (admin != null && admin.username == username && admin.password == password) {
                        // Admin authentication successful
                        isAdminAuthenticated = true
                        adminId = adminSnapshot.key ?: ""
                        break
                    }
                }
                if (isAdminAuthenticated) {
                    // Save admin ID to SharedPreferences
                    saveAdminId(adminId)
                    // Authentication successful, proceed to AdminHome activity
                    val intent = Intent(this@AdminLogin, AdminHome::class.java)
                    // Pass admin ID to AdminHome activity
                    intent.putExtra("addminId", adminId)
                    startActivity(intent)
                    finish()
                } else {
                    // Authentication failed
                    Toast.makeText(
                        this@AdminLogin,
                        "Invalid username or password",
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handles database error
                Toast.makeText(this@AdminLogin, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to save admin ID to SharedPreferences
    private fun saveAdminId(adminId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("addminId", adminId)
        editor.apply()
    }
}

