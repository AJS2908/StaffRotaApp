package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthUserCollisionException
import com.google.firebase.auth.FirebaseAuthWeakPasswordException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

class AddNewAdmin : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    // Views
    private lateinit var phoneNumber: EditText
    private lateinit var confirm: EditText
    private lateinit var password: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_admin)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        val ownerLoginBut: Button = findViewById(R.id.crtAdmProfileRetBut)
        ownerLoginBut.setOnClickListener {
            val intent = Intent(this, ViewAdminAccounts::class.java)
            startActivity(intent)
        }

        // Initialize views
        phoneNumber = findViewById(R.id.PhoneNumber)
        password = findViewById(R.id.password)
        confirm = findViewById(R.id.confirm)
        createButton = findViewById(R.id.addNewAddAccount)

        // Set click listener for createButton
        createButton.setOnClickListener {
            val phone = phoneNumber.text.toString()
            val pass = password.text.toString()
            val confirmPass = confirm.text.toString()

            // Validate input fields
            var hasError = false
            if (phone.isEmpty()) {
                phoneNumber.error = "Phone number cannot be empty"
                hasError = true
            }
            if (pass.isEmpty()) {
                password.error = "Password cannot be empty"
                hasError = true
            }
            if (confirmPass != pass) {
                confirm.error = "Passwords do not match"
                hasError = true
            }

            if (!hasError) {
                // Call function to create new account
                createNewAccount(phone, pass)
            }
        }
    }

    /**
     * Function to create a new account with Firebase Authentication using phone number.
     * @param phoneNumber The phone number entered by the user.
     * @param password The password entered by the user.
     */
    private fun createNewAccount(phoneNumber: String, password: String) {
        // Request OTP to verify the phone number
        val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
            override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                // Sign in with the verified credential
                signInWithPhoneAuthCredential(credential)
            }

            override fun onVerificationFailed(e: FirebaseException) {
                // Handle verification failure
                Toast.makeText(this@AddNewAdmin, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
            }
        }
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
            phoneNumber,
            60,
            TimeUnit.SECONDS,
            this,
            callbacks
        )
    }

    /**
     * Function to sign in with phone authentication credential.
     */
    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Account creation successful, navigate to desired activity
                    startActivity(Intent(this, ViewAccounts::class.java))
                    finish()
                } else {
                    // Account creation failed, display error message
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Weak password. Password should be at least 6 characters long."
                        is FirebaseAuthUserCollisionException -> "This phone number is already taken. Please choose another one."
                        else -> "Account creation failed: ${task.exception?.message ?: "Unknown error occurred"}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}