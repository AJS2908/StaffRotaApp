package com.example.staffrotaapp

import android.content.Intent
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

class AddNewAdmin : AppCompatActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var reference: DatabaseReference

    private lateinit var username: EditText
    private lateinit var email: EditText
    private lateinit var password: EditText
    private lateinit var firstName: EditText
    private lateinit var lastName: EditText
    private lateinit var nINumber: EditText
    private lateinit var confirm: EditText
    private lateinit var createButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_admin)

        database = FirebaseDatabase.getInstance()
        reference = database.getReference("Admins")

        username = findViewById(R.id.username)
        email = findViewById(R.id.email)
        password = findViewById(R.id.password)
        firstName = findViewById(R.id.fName)
        lastName = findViewById(R.id.lName)
        nINumber = findViewById(R.id.nINumber)
        confirm = findViewById(R.id.confirm)
        createButton = findViewById(R.id.addNewAddAccount)

        createButton.setOnClickListener {
            val user = username.text.toString()
            val emailInput = email.text.toString()
            val pass = password.text.toString()
            val fName = firstName.text.toString()
            val lName = lastName.text.toString()
            val nINum = nINumber.text.toString()
            val confirmPass = confirm.text.toString()

            var hasError = false
            if (user.isEmpty()) {
                username.error = "Username cannot be empty"
                hasError = true
            }
            if (emailInput.isEmpty()) {
                email.error = "Email cannot be empty"
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
            if (nINum.isEmpty()) {
                nINumber.error = "National Insurance number cannot be empty"
                hasError = true
            }
            if (confirmPass != pass) {
                confirm.error = "Passwords do not match"
                hasError = true
            }

            if (!hasError) {
                generateAdminId()
            }
        }
    }

    private fun createNewAdmin(
        username: String,
        firstName: String,
        lastName: String,
        email: String,
        password: String,
        nINumber: String,
        adminId: Int
    ) {
        val admin = Admin(
            adminId,
            username,
            email,
            password,
            firstName,
            lastName,
            nINumber
        )

        reference.child(adminId.toString())
            .setValue(admin)
            .addOnSuccessListener {
                startActivity(Intent(this, ViewAdminAccounts::class.java))
                finish()
            }
            .addOnFailureListener { exception ->
                Toast.makeText(
                    this,
                    "Failed to create admin account: ${exception.message}",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }

    private fun generateAdminId() {
        reference.orderByChild("adminId").limitToLast(1).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                var maxId = 0
                for (adminSnapshot in snapshot.children) {
                    val admin = adminSnapshot.getValue(Admin::class.java)
                    val adminId = admin?.adminId ?: 0
                    if (adminId > maxId) {
                        maxId = adminId
                    }
                }
                val newId = maxId + 1
                createNewAdmin(
                    username.text.toString(), email.text.toString(), password.text.toString(),
                    firstName.text.toString(), lastName.text.toString(), nINumber.text.toString(), newId
                )
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
            }
        })
    }
}