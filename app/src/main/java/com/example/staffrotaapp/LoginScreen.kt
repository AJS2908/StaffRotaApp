package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.text.TextUtils
import android.text.method.PasswordTransformationMethod
import android.widget.Button
import android.widget.EditText
import android.widget.Switch
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth

class LoginScreen : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private lateinit var username: EditText
    private lateinit var password: EditText
    private lateinit var loginButton: Button
    private lateinit var passwordMask: Switch

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login_screen)

        auth = FirebaseAuth.getInstance() // Initialize FirebaseAuth

        val gotoAdminlogin: Button = findViewById(R.id.GotoAdmin)
        gotoAdminlogin.setOnClickListener {
            val intent = Intent(this, AdminLogin::class.java)
            startActivity(intent)
        }

        val gotoPasswordReset: Button = findViewById(R.id.FPassword)
        gotoPasswordReset.setOnClickListener {
            val intent = Intent(this, PasswordReset::class.java)
            startActivity(intent)
        }

        val cancellogin: Button = findViewById(R.id.LoginCancel)
        cancellogin.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
        }

        username = findViewById(R.id.Username)
        password = findViewById(R.id.Password)
        loginButton = findViewById(R.id.Send)
        passwordMask = findViewById(R.id.PassMask)

        passwordMask.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                // Show password
                password.transformationMethod = null
            } else {
                // Hide password (mask)
                password.transformationMethod = PasswordTransformationMethod.getInstance()
            }
        }

        loginButton.setOnClickListener {
            val user = username.text.toString()
            val pass = password.text.toString()

            if (TextUtils.isEmpty(user)) {
                Toast.makeText(this, "Please enter Username", Toast.LENGTH_SHORT).show()
            } else if (TextUtils.isEmpty(pass)) {
                Toast.makeText(this, "Please Enter Password", Toast.LENGTH_SHORT).show()
            } else {
                loginUser(user, pass)
            }
        }
    }

    private fun loginUser(username: String, password: String) {
        auth.signInWithEmailAndPassword(username, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    Toast.makeText(this, "Login Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, HomeScreen::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Login Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }
}