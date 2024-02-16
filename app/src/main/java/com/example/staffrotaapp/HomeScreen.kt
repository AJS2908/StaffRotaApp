package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class HomeScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)

        val TimetableButton: Button = findViewById(R.id.TimetableBut)
        TimetableButton.setOnClickListener {
            val intent = Intent(this, Timetable::class.java)
            startActivity(intent)
        }


        val Holidaybutton: Button = findViewById(R.id.holidayBut)
        Holidaybutton.setOnClickListener {
            val intent = Intent(this, Holiday::class.java)
            startActivity(intent)
        }

        val Feebackbutton: Button = findViewById(R.id.feedbackBut)
        Feebackbutton.setOnClickListener {
            val intent = Intent(this, Feedback::class.java)
            startActivity(intent)
        }

        val logoutButton: Button = findViewById(R.id.LogoutBut)
        logoutButton.setOnClickListener {
            // Sign out the user
            auth.signOut()

            // Display toast message for successful logout
            Toast.makeText(this, "Logout Successful", Toast.LENGTH_SHORT).show()

            // Redirect to login screen
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish() // Close the current activity
        }
    }



    fun ButtonProfile (View: View){

        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}