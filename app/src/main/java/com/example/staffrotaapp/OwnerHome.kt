package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OwnerHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_home)
        val addminId = intent.getStringExtra("addminId")


        // Button to view admin accounts
        val Viewadminaccountbut: Button = findViewById(R.id.viewAdminAccountBut)
        Viewadminaccountbut.setOnClickListener {
            val intent = Intent(this, ViewAdminAccounts::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

// Button to view owner profile
        val ViewOwnerProfbut: Button = findViewById(R.id.ownerViewProfile)
        ViewOwnerProfbut.setOnClickListener {
            val intent = Intent(this, OwnerProfile::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

// Button to log out the owner
        val OwnerLogOutbut: Button = findViewById(R.id.ownerLogoutBut)
        OwnerLogOutbut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

// Button to view feedback
        val OwnerFeedbackbut: Button = findViewById(R.id.FeedbackButton)
        OwnerFeedbackbut.setOnClickListener {
            val intent = Intent(this, ViewFeedback::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }

    }
}