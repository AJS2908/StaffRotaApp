package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class shiftAssignment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_assignment)

        val AdminLogout: Button = findViewById(R.id.shiftRetBut)
        AdminLogout.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }

    }
}