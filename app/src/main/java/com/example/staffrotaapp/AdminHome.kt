package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        val gotoprofile: Button = findViewById(R.id.AdminProfileBut)
        gotoprofile.setOnClickListener {
            val intent = Intent(this, AdminProfile::class.java)
            startActivity(intent)
        }

        val gotoViewaccount: Button = findViewById(R.id.Accountviewbut)
        gotoViewaccount.setOnClickListener {
            val intent = Intent(this, ViewAccounts::class.java)
            startActivity(intent)
        }

        val gotoshiftcreation: Button = findViewById(R.id.ShiftAssign)
        gotoshiftcreation.setOnClickListener {
            val intent = Intent(this, CreateShift::class.java)
            startActivity(intent)
        }

        val gotoTimetabel: Button = findViewById(R.id.timetableviewBut)
        gotoTimetabel.setOnClickListener {
            val intent = Intent(this, AdminTimetable::class.java)
            startActivity(intent)
        }

        val AdminLogout: Button = findViewById(R.id.AdmLogOutBut)
        AdminLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
    }
}