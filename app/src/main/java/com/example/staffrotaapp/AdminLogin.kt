package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

private lateinit var username: EditText
private lateinit var password: EditText
private lateinit var LoginButton: Button

class AdminLogin : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        val returnbutton: Button = findViewById(R.id.returnbut)
        returnbutton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }
        val adminloginbut: Button = findViewById(R.id.adminloginbut)
        adminloginbut.setOnClickListener {
            val intent = Intent(this, AdminHome::class.java)
            startActivity(intent)
        }
        val gotoownerlogin: Button = findViewById(R.id.gotoownerlogin)
        gotoownerlogin.setOnClickListener {
            val intent = Intent(this, Owner_Login::class.java)
            startActivity(intent)
        }

        username = findViewById(R.id.PhoneNumber)
        password = findViewById(R.id.password)
        LoginButton = findViewById(R.id.adminloginbut)

        LoginButton.setOnClickListener {
            val Username = username.text.toString()
            val Password = password.text.toString()
            if (TextUtils.isEmpty(Username)) {
                Toast.makeText(this, "please enter Username", Toast.LENGTH_SHORT).show()
            } else {
                if (TextUtils.isEmpty(Password)) {
                    Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
                } else {
                    Toast.makeText(this, "Logged in", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminHome::class.java)
                    startActivity(intent)
                }
            }

        }
    }
}

/*
package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthInvalidUserException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthProvider
import java.util.concurrent.TimeUnit

private lateinit var phoneNumber: EditText
private lateinit var loginButton: Button

class AdminLogin : AppCompatActivity() {

    // Firebase Authentication instance
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_login)

        // Initialize Firebase Authentication
        auth = FirebaseAuth.getInstance()

        val returnButton: Button = findViewById(R.id.returnbut)
        returnButton.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        val goToOwnerLogin: Button = findViewById(R.id.gotoownerlogin)
        goToOwnerLogin.setOnClickListener {
            val intent = Intent(this, Owner_Login::class.java)
            startActivity(intent)
        }

        phoneNumber = findViewById(R.id.PhoneNumber)
        loginButton = findViewById(R.id.adminloginbut)

        loginButton.setOnClickListener {
            val userPhoneNumber = phoneNumber.text.toString()

            if (TextUtils.isEmpty(userPhoneNumber)) {
                Toast.makeText(this, "Please enter phone number", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Request OTP to verify the phone number
            val callbacks = object : PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                override fun onVerificationCompleted(credential: PhoneAuthCredential) {
                    signInWithPhoneAuthCredential(credential)
                }

                override fun onVerificationFailed(e: FirebaseException) {
                    // Handle verification failure
                    Toast.makeText(this@AdminLogin, "Verification failed: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
            PhoneAuthProvider.getInstance().verifyPhoneNumber(
                "+$userPhoneNumber", // Phone number in E.164 format
                60,
                TimeUnit.SECONDS,
                this,
                callbacks
            )
        }
    }

    private fun signInWithPhoneAuthCredential(credential: PhoneAuthCredential) {
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Login successful, navigate to AdminHome
                    Toast.makeText(this, "Logged in successfully", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, AdminHome::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    // Login failed, display error message
                    val errorMessage = when (task.exception) {
                        is FirebaseAuthInvalidCredentialsException -> "Invalid verification code"
                        is FirebaseAuthInvalidUserException -> "User not found"
                        else -> "Login failed: ${task.exception?.message ?: "Unknown error occurred"}"
                    }
                    Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
                }
            }
    }
}
 */