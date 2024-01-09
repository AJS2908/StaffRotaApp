package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminTimetable : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_timetable)

        val TimetableHome: Button = findViewById(R.id.timetablehomebut)
        TimetableHome.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }
    }
}