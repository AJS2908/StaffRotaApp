package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)


    }


    fun ButtonLogin (View: View){

        val intent = Intent(this, LoginScreen::class.java)
        startActivity(intent)
    }

    fun ButtonRegister (View: View){
        val intent = Intent(this, Register::class.java)
        startActivity(intent)
    }

}