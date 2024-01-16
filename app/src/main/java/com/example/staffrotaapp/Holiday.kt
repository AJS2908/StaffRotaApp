package com.example.staffrotaapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.TextView

class Holiday : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_holiday)


        val changetoBooked: Button = findViewById(R.id.yourHoliday)
        val changetorequests: Button = findViewById(R.id.currentRequests)
        val ShowBookedHoliday: ListView = findViewById(R.id.yourHolidayList)
        val ShowHolidayRequests: ListView = findViewById(R.id.yourRequestsList)

        changetoBooked.setOnClickListener {
                ShowHolidayRequests.visibility = View.INVISIBLE
                ShowBookedHoliday.visibility = View.VISIBLE
            }

        changetorequests.setOnClickListener {
            ShowHolidayRequests.visibility = View.VISIBLE
            ShowBookedHoliday.visibility = View.INVISIBLE
        }

    }
}