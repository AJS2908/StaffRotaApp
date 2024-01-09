package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class Owner_Login : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_login)

        val returnbutton: Button = findViewById(R.id.returnbutton)
        returnbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val OwnerLoginbut: Button = findViewById(R.id.OwnerLogin)
        OwnerLoginbut.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
        }

    }
}