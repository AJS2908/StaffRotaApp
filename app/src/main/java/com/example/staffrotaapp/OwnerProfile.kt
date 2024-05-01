package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class OwnerProfile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_owner_profile)

        // Button to return to owner's home screen
        val ViewOwnerProfRetBut: Button = findViewById(R.id.viewOwnerProfileReturn)
        ViewOwnerProfRetBut.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
        }

        // Initialize views
        val EditOwnerUsername: EditText = findViewById(R.id.username)
        val EditOwnerPassword: EditText = findViewById(R.id.password)
        val EditOwnerConPassword: EditText = findViewById(R.id.confirmPassword)
        val EditOwnerProfRetBut: Button = findViewById(R.id.editOwnerProfileBut)
        val EditOwnerProfcanBut: Button = findViewById(R.id.editOwnerProfileCancel)
        val EditOwnerProfconBut: Button = findViewById(R.id.editOwnerProfileConBut)
        val ShownOwnerPassword: TextView = findViewById(R.id.PasswordDisplay)
        val ShownOwnerUsername: TextView = findViewById(R.id.UsernameDisplay)

        // Set click listener for edit profile button
        EditOwnerProfRetBut.setOnClickListener{
            // Show edit fields and hide display fields
            EditOwnerUsername.visibility = View.VISIBLE
            EditOwnerPassword.visibility = View.VISIBLE
            EditOwnerConPassword.visibility = View.VISIBLE
            EditOwnerProfcanBut.visibility = View.VISIBLE
            EditOwnerProfconBut.visibility = View.VISIBLE
            ShownOwnerPassword.visibility = View.INVISIBLE
            ShownOwnerUsername.visibility = View.INVISIBLE
        }

        // Set click listener for cancel button
        EditOwnerProfcanBut.setOnClickListener{
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
            // Hide edit fields and show display fields
            EditOwnerUsername.visibility = View.INVISIBLE
            EditOwnerPassword.visibility = View.INVISIBLE
            EditOwnerConPassword.visibility = View.INVISIBLE
            EditOwnerProfcanBut.visibility = View.INVISIBLE
            EditOwnerProfconBut.visibility = View.INVISIBLE
            ShownOwnerPassword.visibility = View.VISIBLE
            ShownOwnerUsername.visibility = View.VISIBLE
        }

        // Set click listener for confirm button
        EditOwnerProfconBut.setOnClickListener{
            val intent = Intent(this, OwnerHome::class.java)
            startActivity(intent)
            // Hide edit fields and show display fields
            EditOwnerUsername.visibility = View.INVISIBLE
            EditOwnerPassword.visibility = View.INVISIBLE
            EditOwnerConPassword.visibility = View.INVISIBLE
            EditOwnerProfcanBut.visibility = View.INVISIBLE
            EditOwnerProfconBut.visibility = View.INVISIBLE
            ShownOwnerPassword.visibility = View.VISIBLE
            ShownOwnerUsername.visibility = View.VISIBLE
        }
    }

}