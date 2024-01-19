package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

private lateinit var FeedbackLV: EditText
private lateinit var Confirm: Button

class Feedback : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        FeedbackLV = findViewById(R.id.feedback)
        Confirm = findViewById(R.id.submitFeedbackButton)


        Confirm.setOnClickListener {
            val FeedbackText = FeedbackLV.text.toString()
            if (TextUtils.isEmpty(FeedbackText)) {
                Toast.makeText(this, "please enter Feedback", Toast.LENGTH_SHORT).show()
            }else {
                    Toast.makeText(this, "Feedback Submitted", Toast.LENGTH_SHORT).show()
            }
        }

        val Returnbut: Button = findViewById(R.id.returnToHome)
        Returnbut.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }

    }
}