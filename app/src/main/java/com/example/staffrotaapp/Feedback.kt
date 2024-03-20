package com.example.staffrotaapp

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

private lateinit var FeedbackLV: EditText
private lateinit var Confirm: Button

class Feedback : AppCompatActivity() {

    private lateinit var database: FirebaseDatabase
    private lateinit var feedbackRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_feedback)

        FeedbackLV = findViewById(R.id.feedback)
        Confirm = findViewById(R.id.submitFeedbackButton)

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        feedbackRef = database.getReference("Feedback")

        Confirm.setOnClickListener {
            val feedbackText = FeedbackLV.text.toString()
            if (TextUtils.isEmpty(feedbackText)) {
                Toast.makeText(this, "Please enter feedback", Toast.LENGTH_SHORT).show()
            } else {
                // Save feedback to the database
                saveFeedbackToDatabase(feedbackText)
                Toast.makeText(this, "Feedback submitted", Toast.LENGTH_SHORT).show()
            }
        }

        val returnButton: Button = findViewById(R.id.returnToHome)
        returnButton.setOnClickListener {
            val intent = Intent(this, HomeScreen::class.java)
            startActivity(intent)
        }
    }

    private fun saveFeedbackToDatabase(feedbackText: String) {
        // Generate a unique key for the feedback entry using push()
        val feedbackKey = feedbackRef.push().key
        if (feedbackKey != null) {
            // Save the feedback under the generated key
            feedbackRef.child(feedbackKey).setValue(feedbackText)
        }
    }
}
