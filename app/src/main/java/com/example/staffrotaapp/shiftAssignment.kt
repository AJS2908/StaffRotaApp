package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class shiftAssignment : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shift_assignment)

        // Retrieve admin ID from intent extras
        val adminId = intent.getStringExtra("adminId")


        val Adminret: Button = findViewById(R.id.shiftRetBut)
        Adminret.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java).apply {
                putExtra("adminId", adminId)
            }
            startActivity(intent)
        }

    }
}