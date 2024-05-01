package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Profile : AppCompatActivity() {
    private lateinit var emailView: TextView
    private lateinit var usernameView: TextView
    private lateinit var fNameView: TextView
    private lateinit var iDnumber: TextView
    private lateinit var niNumberView: TextView
    private lateinit var database: FirebaseDatabase
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
        database = FirebaseDatabase.getInstance()

        emailView = findViewById(R.id.viewEmail)
        usernameView = findViewById(R.id.viewUsername)
        fNameView = findViewById(R.id.viewfName)
        niNumberView = findViewById(R.id.viewNinumber)
        iDnumber = findViewById(R.id.viewIdnumber)

        getEmployeeDetailsForCurrentUser()
    }

    fun ButtonReturnHome (View: View){
        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
    }

    private fun getEmployeeDetailsForCurrentUser() {
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser

        currentUser?.let { user ->
            val userEmail = user.email
            userEmail?.let { email ->
                val reference = database.getReference("Employees")

                // Query database for the employee with the current user's email
                reference.orderByChild("email").equalTo(email).addListenerForSingleValueEvent(object :
                    ValueEventListener {
                    override fun onDataChange(dataSnapshot: DataSnapshot) {
                        for (employeeSnapshot in dataSnapshot.children) {
                            val employee = employeeSnapshot.getValue(Employee::class.java)
                            // If employee with given email is found
                            if (employee != null) {
                                Log.i("Employee", "Employee found - ID: ${employee.employeeId}")
                                // Fill TextViews with employee data
                                emailView.text = employee.email
                                usernameView.text = employee.username
                                fNameView.text = "${employee.firstName} ${employee.lastName}"
                                niNumberView.text = employee.ninumber
                                iDnumber.text = employee.employeeId.toString()
                                Log.i("Employee", "NINumber: ${employee.ninumber}") // Check the retrieved nINumber
                            } else {
                                Log.i("Employee", "Employee with email $email not found")
                            }
                        }
                    }

                    override fun onCancelled(databaseError: DatabaseError) {
                        Log.e("Employee", "Error fetching employee details: ${databaseError.message}")
                    }
                })
            }
        }
    }
}
