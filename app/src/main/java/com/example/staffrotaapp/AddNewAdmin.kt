package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class AddNewAdmin : AppCompatActivity() {
    private lateinit var Username: EditText
    private lateinit var Password: EditText
    private lateinit var passwordCon: EditText
    private lateinit var NINumber: EditText
    private lateinit var Confirm: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_new_admin)

        val OwnerLoginbut: Button = findViewById(R.id.crtAdmProfileRetBut)
        OwnerLoginbut.setOnClickListener {
            val intent = Intent(this, ViewAdminAccounts::class.java)
            startActivity(intent)
        }


        Username = findViewById(R.id.username)
        Password = findViewById(R.id.password)
        passwordCon = findViewById(R.id.confirm)
        NINumber = findViewById(R.id.adminNINumber)
        Confirm = findViewById(R.id.addNewAddAccount)

        Confirm.setOnClickListener {
            val userText = Username.text.toString()
            val passText = Password.text.toString()
            val confirmText = passwordCon.text.toString()
            val NINumberText = NINumber.text.toString()

            if (TextUtils.isEmpty(userText) || TextUtils.isEmpty(passText) || TextUtils.isEmpty(NINumberText) || TextUtils.isEmpty(NINumberText)) {
                Toast.makeText(this, "please enter details", Toast.LENGTH_SHORT).show()
            }else {
                if (passText.equals(confirmText)) {
                    Toast.makeText(this, "Account Made", Toast.LENGTH_SHORT).show()
                }else {
                    Toast.makeText(this, "Password doesn't match", Toast.LENGTH_SHORT).show()
                }

            }
        }
    }
}