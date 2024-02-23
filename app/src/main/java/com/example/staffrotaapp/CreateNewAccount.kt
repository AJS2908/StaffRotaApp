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
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class CreateNewAccount : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // Views
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var confirm: EditText
    private lateinit var createButton: Button


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")

        // Initialize views
        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        firstName = findViewById(R.id.fName)
        lastName = findViewById(R.id.lName)
        confirm = findViewById(R.id.confirm)
        createButton = findViewById(R.id.createButton)

        // Set click listener for createButton
        createButton.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()
            val fName = firstName.text.toString()
            val lName = lastName.text.toString()
            val confirmPass = confirm.text.toString()

            // Validate input fields
            var hasError = false
            if (user.isEmpty()) {
                username.error = "Username cannot be empty"
                hasError = true
            }
            if (pass.isEmpty()) {
                password.error = "Password cannot be empty"
                hasError = true
            }
            if (fName.isEmpty()) {
                firstName.error = "First name cannot be empty"
                hasError = true
            }
            if (lName.isEmpty()) {
                lastName.error = "Last name cannot be empty"
                hasError = true
            }
            if (confirmPass != pass) {
                confirm.error = "Passwords do not match"
                hasError = true
            }

            if (!hasError) {
                // Call function to create new account
                createNewAccount(user, pass, fName, lName)
            }
        }
    }

    /**
     * Function to create a new account with Firebase Authentication.
     * @param username The username entered by the user.
     * @param password The password entered by the user.
     * @param firstName The first name entered by the user.
     * @param lastName The last name entered by the user.
     */
    private fun createNewAccount(username: String, password: String, firstName: String, lastName: String) {
        auth.createUserWithEmailAndPassword("$username", password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Account creation successful, save user details to database
                    val encodedUsername = encodeUsername(username)
                    val employee = Employee(firstName, lastName, "", password)
                    reference.child(encodedUsername).setValue(employee)
                    // Navigate to desired activity
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

    private fun encodeUsername(username: String): String {
        return username.replace(".", "_dot_").replace("@", "_at_")
    }
}