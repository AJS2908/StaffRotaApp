package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminTimetable : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_timetable)

        // Retrieve admin ID from intent extras
        val adminId = intent.getStringExtra("adminId")

        val TimetableHome: Button = findViewById(R.id.timetablehomebut)
        TimetableHome.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }
    }
}