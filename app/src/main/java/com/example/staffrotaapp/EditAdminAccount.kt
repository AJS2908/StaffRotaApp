package com.example.staffrotaapp

import android.app.AlertDialog
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class EditAdminAccount : AppCompatActivity() {

    // Firebase Database instance
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var adminId: String // Received adminId to edit

    // UI elements
    private lateinit var usernameEditText: EditText
    private lateinit var emailEditText: EditText
    private lateinit var passwordEditText: EditText
    private lateinit var firstNameEditText: EditText
    private lateinit var lastNameEditText: EditText
    private lateinit var nINumberEditText: EditText
    private lateinit var ownerAccSwitch: Switch
    private lateinit var saveButton: Button
    private lateinit var deleteButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_admin_account)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins")

        // Get adminId passed from previous activity
        adminId = intent.getStringExtra("adminId") ?: ""

        // Initialize UI elements
        usernameEditText = findViewById(R.id.username)
        emailEditText = findViewById(R.id.email)
        passwordEditText = findViewById(R.id.password)
        firstNameEditText = findViewById(R.id.fName)
        lastNameEditText = findViewById(R.id.lName)
        nINumberEditText = findViewById(R.id.nINumber)
        ownerAccSwitch = findViewById(R.id.ownerAccSwitch)
        saveButton = findViewById(R.id.edit)
        deleteButton = findViewById(R.id.delete)

        // Retrieve admin data from database and autofill fields
        fetchAdminData()

        // Set up save button click listener
        saveButton.setOnClickListener {
            updateAdminAccount()
        }

        // Set up delete button click listener
        deleteButton.setOnClickListener {
            showDeleteConfirmationDialog()
        }
    }

    private fun fetchAdminData() {
        reference.child(adminId).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val admin = snapshot.getValue(Admin::class.java)
                admin?.let {
                    // Autofill UI fields with admin data
                    usernameEditText.setText(it.username)
                    emailEditText.setText(it.email)
                    firstNameEditText.setText(it.firstName)
                    lastNameEditText.setText(it.lastName)
                    nINumberEditText.setText(it.nINumber)
                    ownerAccSwitch.isChecked = it.ownerAcc
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                println("Error: ${error.message}")
            }
        })
    }

    private fun updateAdminAccount() {
        val updatedUsername = usernameEditText.text.toString()
        val updatedEmail = emailEditText.text.toString()
        val updatedPassword = passwordEditText.text.toString()
        val updatedFirstName = firstNameEditText.text.toString()
        val updatedLastName = lastNameEditText.text.toString()
        val updatedNINumber = nINumberEditText.text.toString()
        val updatedOwnerAcc = ownerAccSwitch.isChecked

        // Update only if fields are not empty
        if (updatedUsername.isNotEmpty()) {
            reference.child(adminId).child("username").setValue(updatedUsername)
        }
        if (updatedEmail.isNotEmpty()) {
            reference.child(adminId).child("email").setValue(updatedEmail)
        }
        if (updatedPassword.isNotEmpty()) {
            reference.child(adminId).child("password").setValue(updatedPassword)
        }
        if (updatedFirstName.isNotEmpty()) {
            reference.child(adminId).child("firstName").setValue(updatedFirstName)
        }
        if (updatedLastName.isNotEmpty()) {
            reference.child(adminId).child("lastName").setValue(updatedLastName)
        }
        if (updatedNINumber.isNotEmpty()) {
            reference.child(adminId).child("ninumber").setValue(updatedNINumber)
        }
        reference.child(adminId).child("ownerAcc").setValue(updatedOwnerAcc)

        // Finish activity after updating
        finish()
    }

    private fun showDeleteConfirmationDialog() {
        AlertDialog.Builder(this)
            .setTitle("Delete Admin Account")
            .setMessage("Are you sure you want to delete this admin account?")
            .setPositiveButton("Yes") { dialog, which ->
                // Delete the admin account from the database
                reference.child(adminId).removeValue()
                finish() // Finish activity after deleting
            }
            .setNegativeButton("No", null)
            .show()
    }
}
