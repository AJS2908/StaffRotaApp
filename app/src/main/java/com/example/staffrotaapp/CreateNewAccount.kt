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
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class CreateNewAccount : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // Views
    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var nINumber: EditText
    private lateinit var confirm: EditText
    private lateinit var holiday: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_create_new_account)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Fetch admin id from intent extras
        val adminId = intent.getStringExtra("adminId")

        // Initialize UI elements
        val createAccountsRet: Button = findViewById(R.id.createAccountsReturn)
        createAccountsRet.setOnClickListener {
            // Return to AdminHome activity
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")

        // Initialize views
        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        firstName = findViewById(R.id.fName)
        lastName = findViewById(R.id.lName)
        nINumber = findViewById(R.id.nINumber)
        holiday = findViewById(R.id.holidays)
        confirm = findViewById(R.id.confirm)
        createButton = findViewById(R.id.createButton)

        // Set click listener for createButton
        createButton.setOnClickListener {
            // Retrieve input values
            val user = username.text.toString()
            val emailInput = email.text.toString()
            val pass = password.text.toString()
            val fName = firstName.text.toString()
            val lName = lastName.text.toString()
            val nINum = nINumber.text.toString()
            val holidayInput = holiday.text.toString()
            val confirmPass = confirm.text.toString()

            // Validate input fields
            var hasError = false
            if (user.isEmpty()) {
                username.error = "Username cannot be empty"
                hasError = true
            }
            if (emailInput.isEmpty()) {
                email.error = "Email cannot be empty"
                hasError = true
            }
            if (!android.util.Patterns.EMAIL_ADDRESS.matcher(emailInput).matches()) {
                email.error = "Invalid email format"
                hasError = true
            }
            if (pass.isEmpty()) {
                password.error = "Password cannot be empty"
                hasError = true
            }
            if (pass.length < 6) {
                password.error = "Password must be at least 6 characters long"
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
            if (nINum.isEmpty()) {
                nINumber.error = "National Insurance number cannot be empty"
                hasError = true
            }
            if (holidayInput.isEmpty()) {
                holiday.error = "Holiday number cannot be empty"
                hasError = true
            }
            if (confirmPass != pass) {
                confirm.error = "Passwords do not match"
                hasError = true
            }

            if (!hasError) {
                // If no errors, generate employee ID
                generateEmployeeId()
            }
        }
    }
    // Function to generate a unique employee ID
    private fun generateEmployeeId() {
        reference.orderByChild("employeeId").limitToLast(1)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    var maxId = 0
                    // Find the maximum employee ID
                    for (employeeSnapshot in snapshot.children) {
                        val employee = employeeSnapshot.getValue(Employee::class.java)
                        val employeeId = employee?.employeeId ?: 0
                        if (employeeId > maxId) {
                            maxId = employeeId
                        }
                    }
                    // Increment the maximum ID by 1 to get a new unique ID
                    val newId = maxId + 1
                    // Convert holiday input to Int
                    val holidayInput = holiday.text.toString().toIntOrNull() ?: 0
                    // Call function to create a new account with the generated ID
                    createNewAccount(
                        username.text.toString(),
                        email.text.toString(),
                        password.text.toString(),
                        firstName.text.toString(),
                        lastName.text.toString(),
                        nINumber.text.toString(),
                        newId,
                        holidayInput
                    )
                }

                override fun onCancelled(error: DatabaseError) {
                    println("Error: ${error.message}")
                }
            })
    }

    // Function to create a new account
    private fun createNewAccount(
        username: String,
        email: String,
        password: String,
        firstName: String,
        lastName: String,
        nINumber: String,
        employeeId: Int,
        holidays: Int
    ) {
        // Use Firebase Authentication to create a new user account
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // If account creation is successful, save employee data to the database
                    val employee = Employee(
                        employeeId,
                        username,
                        firstName,
                        lastName,
                        email,
                        password,
                        nINumber,
                        holidays
                    )
                    reference.child(employeeId.toString())
                        .setValue(employee)
                        .addOnSuccessListener {
                            // If data saving is successful, navigate to ViewAccounts activity
                            startActivity(Intent(this, ViewAccounts::class.java))
                            finish()
                        }
                        .addOnFailureListener { exception ->
                            // If data saving fails, display error message
                            Toast.makeText(
                                this,
                                "Failed to create account: ${exception.message}",
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                } else {
                    // If account creation fails, display appropriate error message
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthWeakPasswordException -> "Weak password. Password should be at least 6 characters long."
                        is FirebaseAuthInvalidCredentialsException -> "Invalid email format."
                        is FirebaseAuthUserCollisionException -> "This email is already taken. Please choose another one."
                        else -> "Account creation failed: ${task.exception?.message ?: "Unknown error occurred"}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }

}
