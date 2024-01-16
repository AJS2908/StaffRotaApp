package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ViewAccounts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_accounts)

        val AccountViewReturn: Button = findViewById(R.id.viewAccountsReturn)
        AccountViewReturn.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }
    }
}