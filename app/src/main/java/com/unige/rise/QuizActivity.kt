package com.unige.rise


import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unige.rise.databinding.ActivityQuizBinding

class QuizActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuizBinding
    private val viewModel: QuizViewModel by viewModels()

    private var db: DatabaseReference = FirebaseDatabase.getInstance().reference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        val courseId : String = intent.extras?.getString("courseId") ?: "0"

        db.child("courses").child(courseId).child("quiz").get().addOnSuccessListener { dataSnapshot ->
            viewModel.initQuiz(courseId, dataSnapshot.childrenCount.toInt())
        }.addOnFailureListener {
            Toast.makeText(this, "Something goes wrong", Toast.LENGTH_SHORT).show()
        }

        //viewModel.initQuiz(courseId, )

        binding.trueBtn.setOnClickListener {
            viewModel.answer(true)
        }

        binding.falseBtn.setOnClickListener {
            viewModel.answer(false)
        }
    }
}
