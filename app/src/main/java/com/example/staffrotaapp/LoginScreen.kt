package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

private lateinit var username: EditText
private lateinit var password: EditText
private lateinit var LoginButton: Button
class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val gotoAdminlogin: Button = findViewById(R.id.GotoAdmin)
        gotoAdminlogin.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
        }

        val cancellogin: Button = findViewById(R.id.LoginCancel)
        cancellogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        username = findViewById(R.id.Username)
        password = findViewById(R.id.Password)
        LoginButton = findViewById(R.id.LoginSubmit)

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
                    val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                }
            }

        }

    }

}