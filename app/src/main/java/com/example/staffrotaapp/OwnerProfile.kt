package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OwnerProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_profile)
        val ViewOwnerProfRetBut: Button = findViewById(R.id.viewOwnerProfileReturn)
        ViewOwnerProfRetBut.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
        }
    }
}