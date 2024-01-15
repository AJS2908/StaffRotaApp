package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ViewAdminAccounts : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_admin_accounts)

        val ViewAdAccounsRetBut: Button = findViewById(R.id.viewAdminAccountsReturn)
        ViewAdAccounsRetBut.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
        }

        val AddAdAccounsBut: Button = findViewById(R.id.addNewAdAccountBut)
        AddAdAccounsBut.setOnClickListener {
            val intent = Intent(this, AddNewAdmin::class.java)
            startActivity(intent)
        }

    }
}