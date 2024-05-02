package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.staffrotaapp.LoginScreen
import com.example.staffrotaapp.R
import com.google.firebase.auth.FirebaseAuth

class PasswordReset : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var emailEditText: EditText
    private lateinit var resetButton: Button
    private lateinit var resetPasswordBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_password_reset)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize views
        emailEditText = findViewById(R.id.Email)
        resetButton = findViewById(R.id.Send)
        resetPasswordBack = findViewById(R.id.ResetBack)

        resetButton.setOnClickListener {
            // Retrieve email input from EditText and trim any leading or trailing whitespace
            val email = emailEditText.text.toString().trim()

            // Check if email is empty
            if (email.isEmpty()) {
                // Display a toast message indicating that the user should enter their email address
                Toast.makeText(this, "Please enter your email address", Toast.LENGTH_SHORT).show()
            } else {
                // If email is provided, proceed with sending password reset email
                sendPasswordResetEmail(email)
            }
        }

        // Set click listener for resetPasswordBack button and redirect user back to Login Screen
        resetPasswordBack.setOnClickListener {
            // Create an intent to navigate back to the LoginScreen activity
            val intent = Intent(this, LoginScreen::class.java)
            // Start the activity to go back to the login screen
            startActivity(intent)
        }
    }

        // Function to send password reset email to the provided email address
        private fun sendPasswordResetEmail(email: String) {
            // Send password reset email using FirebaseAuth
            auth.sendPasswordResetEmail(email)
                .addOnCompleteListener { task ->
                    if (task.isSuccessful) {
                        // If sending the password reset email is successful, display a success message to the user
                        Toast.makeText(
                            this,
                            "Password reset email sent to $email",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        // If sending the password reset email fails, display an error message to the user
                        Toast.makeText(
                            this,
                            "Failed to send password reset email: ${task.exception?.message}",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
        }
}