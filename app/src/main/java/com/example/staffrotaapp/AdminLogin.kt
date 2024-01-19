package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

private lateinit var username: EditText
private lateinit var password: EditText
private lateinit var LoginButton: Button

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

        username = findViewById(R.id.username)
        password = findViewById(R.id.password)
        LoginButton = findViewById(R.id.adminloginbut)

        LoginButton.setOnClickListener {
            val Username = username.text.toString()
            val Password = password.text.toString()
            if (TextUtils.isEmpty(Username)) {
                Toast.makeText(this, "please enter Username", Toast.LENGTH_SHORT).show()
            } else {
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminHome::class.java)
                    startActivity(intent)
                }
            }

        }
    }
}