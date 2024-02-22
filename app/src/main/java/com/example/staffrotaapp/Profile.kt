package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class Profile : AppCompatActivity() {
    private lateinit var emailView: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_profile)

        emailView = findViewById(R.id.viewUsername)
        val currentUser = FirebaseAuth.getInstance().currentUser
        val userEmail = currentUser?.email
        emailView.text = userEmail
    }

    fun ButtonReturnHome (View: View){

        val intent = Intent(this, HomeScreen::class.java)
        startActivity(intent)
    }

}