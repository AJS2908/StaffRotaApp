package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_home)

        // Retrieve admin ID from intent extras
        val adminId = intent.getStringExtra("adminId")

        val gotoprofile: Button = findViewById(R.id.AdminProfileBut)
        gotoprofile.setOnClickListener {
            // Navigate to AdminProfile activity and pass admin ID
            val intent = Intent(this, AdminProfile::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        val gotoViewaccount: Button = findViewById(R.id.Accountviewbut)
        gotoViewaccount.setOnClickListener {
            val intent = Intent(this, ViewAccounts::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        val goshiftcreation: Button = findViewById(R.id.shiftassignbut)
        goshiftcreation.setOnClickListener {
            val intent = Intent(this, shiftAssignment::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        val gotoTimetabel: Button = findViewById(R.id.timetableviewBut)
        gotoTimetabel.setOnClickListener {
            val intent = Intent(this, AdminTimetable::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

        val AdminLogout: Button = findViewById(R.id.AdmLogOutBut)
        AdminLogout.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }
    }
}