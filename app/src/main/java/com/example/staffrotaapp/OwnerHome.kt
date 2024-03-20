package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class OwnerHome : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_home)

        val Viewadminaccountbut: Button = findViewById(R.id.viewAdminAccountBut)
        Viewadminaccountbut.setOnClickListener {
            val intent = Intent(this, ViewAdminAccounts::class.java)
            startActivity(intent)
        }

        val ViewOwnerProfbut: Button = findViewById(R.id.ownerViewProfile)
        ViewOwnerProfbut.setOnClickListener {
            val intent = Intent(this, OwnerProfile::class.java)
            startActivity(intent)
        }

        val OwnerLogOutbut: Button = findViewById(R.id.ownerLogoutBut)
        OwnerLogOutbut.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val OwnerFeedbackbut: Button = findViewById(R.id.FeedbackButton)
        OwnerFeedbackbut.setOnClickListener {
            val intent = Intent(this, ViewFeedback::class.java)
            startActivity(intent)
        }



    }
}