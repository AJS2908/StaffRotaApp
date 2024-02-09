package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException

class CreateNewAccount : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize views
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        createButton = findViewById(R.id.createButton)

        // Set click listener for createButton
        createButton.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            // Validate input fields
            if (user.isEmpty()) {
                username.setError("Username cannot be empty")
            } else if (pass.isEmpty()) {
                password.setError("Password cannot be empty")
            } else {
                // Call function to create new account
                createNewAccount(user, pass)
            }
        }
    }

    // Function to create a new account with Firebase Authentication
    private fun createNewAccount(username: String, password: String) {
        auth.createUserWithEmailAndPassword("$username", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Account creation successful, navigate to desired activity
                    startActivity(Intent(this, ViewAccounts::class.java))
                    finish()
                } else {
                    // Account creation failed, display error message
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Weak password. Password should be at least 6 characters long."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                        is FirebaseAuthUserCollisionException -> "This username is already taken. Please choose another one."
                        else -> "Account creation failed: ${task.exception?.message ?: "Unknown error occurred"}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}