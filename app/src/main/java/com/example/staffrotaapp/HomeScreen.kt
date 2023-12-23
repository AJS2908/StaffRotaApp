package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class HomeScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home_screen)
    }

    fun ButtonlogOut (View: View){

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun ButtonProfile (View: View){

        val intent = Intent(this, Profile::class.java)
        startActivity(intent)
    }
}