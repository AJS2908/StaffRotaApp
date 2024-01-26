package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class HomeScreen : AppCompatActivity() {
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
    }

    fun ButtonlogOut (View: View){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun ButtonProfile (View: View){

        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}