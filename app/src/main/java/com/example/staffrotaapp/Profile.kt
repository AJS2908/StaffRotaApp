package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View

class Profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)
    }

    fun ButtonReturnHome (View: View){

        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
    }

}