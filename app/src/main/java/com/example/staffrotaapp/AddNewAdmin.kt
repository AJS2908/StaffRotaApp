package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
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
    private lateinit var ownerAccSwitch: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_admin)

        val returnbut: Button = findViewById(R.id.crtAdmProfileRetBut)
        returnbut.setOnClickListener {
            val intent = Intent(this, AdminTimetable::class.java)
            startActivity(intent)
        }

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
        ownerAccSwitch = findViewById(R.id.ownerAccSwitch)

        createButton.setOnClickListener {
            val user = username.text.toString()
            val emailInput = email.text.toString()
            val pass = password.text.toString()
            val fName = firstName.text.toString()
            val lName = lastName.text.toString()
            val nINum = nINumber.text.toString()
            val confirmPass = confirm.text.toString()
            val ownerAcc = ownerAccSwitch.isChecked

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
                generateAdminId(ownerAcc)
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
        ownerAcc: Boolean,
        adminId: Int
    ) {
        val admin = Admin(
            adminId,
            username,
            email,
            password,
            firstName,
            lastName,
            nINumber,
            ownerAcc
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

    private fun generateAdminId(ownerAcc: Boolean) {
        val usernameInput = username.text.toString()
        val emailInput = email.text.toString()

        // Check for existing username
        reference.orderByChild("username").equalTo(usernameInput).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(usernameSnapshot: DataSnapshot) {
                if (usernameSnapshot.exists()) {
                    // Username already exists
                    username.error = "Username already taken"
                } else {
                    // Username is unique, check for email
                    reference.orderByChild("email").equalTo(emailInput).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(emailSnapshot: DataSnapshot) {
                            if (emailSnapshot.exists()) {
                                // Email already exists
                                email.error = "Email already taken"
                            } else {
                                // Email is also unique, proceed to create admin
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
                                            usernameInput,
                                            emailInput,
                                            password.text.toString(),
                                            firstName.text.toString(),
                                            lastName.text.toString(),
                                            nINumber.text.toString(),
                                            ownerAcc,
                                            newId
                                        )
                                    }

                                    override fun onCancelled(error: DatabaseError) {
                                        println("Error: ${error.message}")
                                    }
                                })
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            println("Error: ${error.message}")
                        }
                    })
                }
            }

            override fun onCancelled(error: DatabaseError) {
                println("Error: ${error.message}")
            }
        })
    }
}