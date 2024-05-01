package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditEmployee : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    // Views
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var nINumberEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var confirmPasswordEditText: EditText
    private lateinit var editButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_edit_employee)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Employees")

        // Retrieve employee ID passed from the intent extras
        val employeeId = intent.getIntExtra("employeeId", -1)

        // Initialize views
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        firstNameEditText = findViewById(R.id.fName)
        lastNameEditText = findViewById(R.id.lName)
        nINumberEditText = findViewById(R.id.nINumber)
        passwordEditText = findViewById(R.id.password)
        confirmPasswordEditText = findViewById(R.id.confirm)
        editButton = findViewById(R.id.editAccount)
        deleteButton = findViewById(R.id.deleteAccount)

        // Fetch employee details and populate EditText fields
        reference.child(employeeId.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val employee = snapshot.getValue(Employee::class.java)
                if (employee != null) {
                    usernameEditText.setText(employee.username)
                    emailEditText.setText(employee.email)
                    firstNameEditText.setText(employee.firstName)
                    lastNameEditText.setText(employee.lastName)
                    nINumberEditText.setText(employee.nINumber)
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(applicationContext, "Failed to fetch employee details", Toast.LENGTH_SHORT).show()
            }
        })

        // Set click listener for edit button
        editButton.setOnClickListener {
            val username = usernameEditText.text.toString()
            val email = emailEditText.text.toString()
            val firstName = firstNameEditText.text.toString()
            val lastName = lastNameEditText.text.toString()
            val nINumber = nINumberEditText.text.toString()
            val password = passwordEditText.text.toString()
            val confirmPassword = confirmPasswordEditText.text.toString()

            // Validate input fields
            if (username.isEmpty() || email.isEmpty() || firstName.isEmpty() || lastName.isEmpty() ||
                nINumber.isEmpty() || password.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(applicationContext, "All fields are required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (password != confirmPassword) {
                Toast.makeText(applicationContext, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Update employee details
            val updatedEmployee = Employee(employeeId, username, firstName, lastName, email, password, nINumber)
            reference.child(employeeId.toString()).setValue(updatedEmployee)
                .addOnSuccessListener {
                    Toast.makeText(applicationContext, "Employee details updated successfully", Toast.LENGTH_SHORT).show()
                    // Redirect to admin's home screen or any other suitable screen
                    startActivity(Intent(applicationContext, AdminHome::class.java))
                    finish()
                }
                .addOnFailureListener { exception ->
                    // Handle failure to update employee details
                    Toast.makeText(applicationContext, "Failed to update employee details: ${exception.message}", Toast.LENGTH_SHORT).show()
                }
        }

        // Set click listener for delete button
        deleteButton.setOnClickListener {
            // Perform delete operation
            deleteEmployeeAccount(employeeId)
        }
    }

    // Function to delete an employee account
    private fun deleteEmployeeAccount(employeeId: Int) {
        reference.child(employeeId.toString()).removeValue()
            .addOnSuccessListener {
                // Account deleted successfully
                Toast.makeText(applicationContext, "Employee account deleted successfully", Toast.LENGTH_SHORT).show()
                // Redirect to admin's home screen or any other suitable screen
                startActivity(Intent(applicationContext, AdminHome::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                // Handle failure to delete account
                Toast.makeText(applicationContext, "Failed to delete employee account: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }
}
