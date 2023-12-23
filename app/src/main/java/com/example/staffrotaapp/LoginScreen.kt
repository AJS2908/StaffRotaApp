package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class LoginScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)
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