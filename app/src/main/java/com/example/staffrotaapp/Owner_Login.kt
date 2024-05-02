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
class Owner_Login : AppCompatActivity() {

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // Views
    private lateinit var usernameEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var loginButton: Button
    private lateinit var passwordMask: Switch

    // Shared Preferences
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_login)

        // Button to return to the MainActivity
        val returnbutton: Button = findViewById(R.id.returnbutton)
        returnbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        // Button to initiate owner login
        val OwnerLoginbut: Button = findViewById(R.id.OwnerLogin)
        OwnerLoginbut.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
        }

        // Switch for toggling password visibility
        passwordMask = findViewById(R.id.PassMask)
        passwordMask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                passwordEditText.transformationMethod = null
            } else {
                // Hide password (mask)
                passwordEditText.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins") // Reference the "Admins" node

        // Initialize Shared Preferences
        sharedPreferences = getSharedPreferences("AdminPrefs", Context.MODE_PRIVATE)

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        passwordEditText = findViewById(R.id.password)
        loginButton = findViewById(R.id.OwnerLogin)

        // Set click listener for loginButton
        loginButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val password = passwordEditText.text.toString()

            // Validate input fields
            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter both username and password", Toast.LENGTH_SHORT).show()
            } else {
                authenticateOwner(username, password)
            }
        }
    }

    // Function to authenticate admin
    private fun authenticateOwner(username: String, password: String) {
        reference.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var isOwnerAuthenticated = false
                var adminId = ""
                for (adminSnapshot in snapshot.children) {
                    val admin = adminSnapshot.getValue(Admin::class.java)
                    if (admin != null && admin.username == username && admin.password == password && admin.ownerAcc) {
                        isOwnerAuthenticated = true
                        adminId = adminSnapshot.key ?: ""
                        break
                    }
                }
                if (isOwnerAuthenticated) {
                    saveOwnerId(adminId)
                    // Authentication successful, proceed to next activity
                    val intent = Intent(this@Owner_Login, OwnerHome::class.java)
                    // Pass admin ID to the next activity
                    intent.putExtra("addminId", adminId)
                    startActivity(intent)
                    finish()
                } else {
                    // Authentication failed
                    Toast.makeText(this@Owner_Login, "Invalid username or password", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@Owner_Login, "Database error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Function to save admin ID to SharedPreferences
    private fun saveOwnerId(adminId: String) {
        val editor = sharedPreferences.edit()
        editor.putString("adminId", adminId)
        editor.apply()
    }
}
