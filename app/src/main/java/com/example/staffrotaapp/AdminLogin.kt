package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class AdminLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val returnbutton: Button = findViewById(R.id.returnbut)
        returnbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val adminloginbut: Button = findViewById(R.id.adminloginbut)
        adminloginbut.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }
        val gotoownerlogin: Button = findViewById(R.id.gotoownerlogin)
        gotoownerlogin.setOnClickListener {
            val intent = Intent(this, Owner_Login::class.java)
            startActivity(intent)
        }
    }
}