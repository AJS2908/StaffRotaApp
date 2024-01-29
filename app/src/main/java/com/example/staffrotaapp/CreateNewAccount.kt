package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class CreateNewAccount : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_new_account)
        val createAccountret: Button = findViewById(R.id.createAccountsReturn)
        createAccountret.setOnClickListener {
            val intent = Intent(this, ViewAccounts::class.java)
            startActivity(intent)
        }
    }
}