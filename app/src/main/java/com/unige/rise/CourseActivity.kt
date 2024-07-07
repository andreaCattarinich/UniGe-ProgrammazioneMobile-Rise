package com.unige.rise

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.squareup.picasso.Picasso

class CourseActivity : AppCompatActivity() {

    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_course)

        val titleTextView = findViewById<TextView>(R.id.title)
        val subtitleTextView = findViewById<TextView>(R.id.subtitle)
        val descriptionTextView = findViewById<TextView>(R.id.description)
        val imageView = findViewById<ImageView>(R.id.image)
/*
        title.text = intent.extras?.getString("title") ?: "No title"
        description.text = intent.extras?.getString("description") ?: "No description"
*/
        db = FirebaseDatabase.getInstance().reference

        val id : String = intent.extras?.getString("id") ?: "0"
        db.child("courses").child(id).get().addOnSuccessListener { dataSnapshot ->
            val title = dataSnapshot.child("title").getValue(String::class.java)
            val subtitle = dataSnapshot.child("subtitle").getValue(String::class.java)
            val description = dataSnapshot.child("description").getValue(String::class.java)
            val imgUrl = dataSnapshot.child("imgUrl").getValue(String::class.java)

            titleTextView.text = title
            subtitleTextView.text = subtitle
            descriptionTextView.text = description

            Picasso.get().load(imgUrl).into(imageView)

        }.addOnFailureListener{

        }
    }
}