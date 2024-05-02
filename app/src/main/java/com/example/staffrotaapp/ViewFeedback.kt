package com.example.staffrotaapp

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.*

class ViewFeedback : AppCompatActivity() {

    // Reference to the Firebase Database
    private lateinit var database: FirebaseDatabase
    private lateinit var feedbackRef: DatabaseReference

    // RecyclerView and its adapter
    private lateinit var feedbackRecyclerView: RecyclerView
    private lateinit var feedbackAdapter: RecyclerView.Adapter<*>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_feedback)
        val addminId = intent.getStringExtra("addminId")

        // Initialize Firebase Database
        database = FirebaseDatabase.getInstance()
        feedbackRef = database.getReference("Feedback")

        // Initialize RecyclerView
        feedbackRecyclerView = findViewById(R.id.feedbackRecyclerView)
        feedbackRecyclerView.layoutManager = LinearLayoutManager(this)

        // Set up an empty list for feedback data
        val feedbackList = mutableListOf<String>()

        val ViewFeedbackRetBut: Button = findViewById(R.id.ViewFeedbackRet)
        ViewFeedbackRetBut.setOnClickListener {
            val intent = Intent(this, OwnerHome::class.java)
            intent.putExtra("addminId", addminId)
            startActivity(intent)
        }


        // Set up RecyclerView Adapter
        feedbackAdapter = object : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
            override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
                val itemView = LayoutInflater.from(parent.context).inflate(android.R.layout.simple_list_item_1, parent, false)
                return object : RecyclerView.ViewHolder(itemView) {}
            }

            override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
                (holder.itemView as TextView).text = feedbackList[position]
            }

            override fun getItemCount(): Int = feedbackList.size
        }

        feedbackRecyclerView.adapter = feedbackAdapter

        // Listen for changes in the database and update the RecyclerView
        feedbackRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                feedbackList.clear()
                for (feedbackSnapshot in snapshot.children) {
                    val feedback = feedbackSnapshot.getValue(String::class.java)
                    feedback?.let {
                        feedbackList.add(it)
                    }
                }
                feedbackAdapter.notifyDataSetChanged()
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle database error
                Toast.makeText(this@ViewFeedback, "Failed to fetch feedback", Toast.LENGTH_SHORT).show()
            }
        })
    }
}




