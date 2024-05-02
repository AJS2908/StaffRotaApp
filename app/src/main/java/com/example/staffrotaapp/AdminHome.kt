package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class AdminHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // Set the layout for this activity
        setContentView(R.layout.activity_admin_home)

        // Retrieve admin ID from intent extras
        val addminId = intent.getStringExtra("addminId")

        // Set click listener for the button to navigate to AdminProfile activity
        val gotoProfile: Button = findViewById(R.id.AdminProfileBut)
        gotoProfile.setOnClickListener {
            // Create intent to navigate to AdminProfile activity and pass admin ID
            val intent = Intent(this@AdminHome, AdminProfile::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Set click listener for the button to navigate to ViewAccounts activity
        val gotoViewAccount: Button = findViewById(R.id.Accountviewbut)
        gotoViewAccount.setOnClickListener {
            // Create intent to navigate to ViewAccounts activity and pass admin ID
            val intent = Intent(this@AdminHome, ViewAccounts::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Set click listener for the button to navigate to ShiftAssignment activity
        val goShiftCreation: Button = findViewById(R.id.shiftassignbut)
        goShiftCreation.setOnClickListener {
            // Create intent to navigate to ShiftAssignment activity and pass admin ID
            val intent = Intent(this@AdminHome, ShiftAssignment::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Set click listener for the button to navigate to AdminTimetable activity
        val gotoTimetable: Button = findViewById(R.id.timetableviewBut)
        gotoTimetable.setOnClickListener {
            // Create intent to navigate to AdminTimetable activity and pass admin ID
            val intent = Intent(this@AdminHome, AdminTimetable::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Set click listener for the button to navigate to MainActivity (logout)
        val adminLogout: Button = findViewById(R.id.AdmLogOutBut)
        adminLogout.setOnClickListener {
            // Create intent to navigate to MainActivity (logout) and pass admin ID
            val intent = Intent(this@AdminHome, MainActivity::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }

        // Set click listener for the button to navigate to AdminHoliday activity
        val adminHolidayButton: Button = findViewById(R.id.holidayBut)
        adminHolidayButton.setOnClickListener {
            // Create intent to navigate to AdminHoliday activity and pass admin ID
            val intent = Intent(this@AdminHome, AdminHoliday::class.java).apply {
                putExtra("addminId", addminId)
            }
            startActivity(intent)
        }
    }
}