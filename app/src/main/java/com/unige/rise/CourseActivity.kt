package com.unige.rise

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.Firebase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.squareup.picasso.Picasso
import com.unige.rise.databinding.ActivityCourseBinding
import com.google.firebase.auth.auth

class CourseActivity : AppCompatActivity() {

    // Databinding and ViewModel
    private lateinit var binding : ActivityCourseBinding
    private val viewModel : CourseViewModel by viewModels()

    private lateinit var db: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_course)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val user = Firebase.auth.currentUser

        if(user == null || user.isAnonymous){
            Toast.makeText(this, "Please sign-in!", Toast.LENGTH_SHORT).show()
            val intent = Intent(this, AuthActivity::class.java)
            startActivity(intent)
            finish()
        } else {
            db = FirebaseDatabase.getInstance().reference

            val id : String = intent.extras?.getString("id") ?: "0"
            db.child("courses").child(id).get().addOnSuccessListener { dataSnapshot ->
                val title = dataSnapshot.child("title").getValue(String::class.java)
                val subtitle = dataSnapshot.child("subtitle").getValue(String::class.java)
                val description = dataSnapshot.child("description").getValue(String::class.java)
                val imgUrl = dataSnapshot.child("imgUrl").getValue(String::class.java)

                viewModel.updateTitleText(title.toString())
                viewModel.updateSubtitleText(subtitle.toString())
                viewModel.updateDescriptionText(description.toString())

                Picasso.get().load(imgUrl).into(binding.image)
            }.addOnFailureListener {
                Toast.makeText(this, "Something goes wrong", Toast.LENGTH_SHORT).show()
            }

            binding.quiz.setOnClickListener {
                val intent = Intent(this, QuizActivity::class.java)
                intent.putExtra("courseId", id)
                startActivity(intent)
            }
        }
    }
}