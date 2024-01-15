package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AddNewAdmin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_admin)

        val OwnerLoginbut: Button = findViewById(R.id.crtAdmProfileRetBut)
        OwnerLoginbut.setOnClickListener {
            val intent = Intent(this, ViewAdminAccounts::class.java)
            startActivity(intent)
        }
    }
}