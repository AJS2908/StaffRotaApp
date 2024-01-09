package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        val gotoAdminlogin: Button = findViewById(R.id.GotoAdmin)
        gotoAdminlogin.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
        }

    }

    fun ButtonCancel (View: View){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun ButtonSubmitLogin (View: View){

        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
    }

}