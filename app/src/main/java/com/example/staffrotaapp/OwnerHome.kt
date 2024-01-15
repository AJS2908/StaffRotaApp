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

        val OwnerLogOutbut: Button = findViewById(R.id.ownerLogoutBut)
        OwnerLogOutbut.setOnClickListener {
            val intent = Intent(this, ViewAdminAccounts::class.java)
            startActivity(intent)
        }


    }
}