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
    private var courseId : String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_quiz)
        binding.lifecycleOwner = this
        binding.viewModel = viewModel

        courseId = intent.extras?.getString("courseId") ?: "0"

        db.child("courses")
            .child(courseId)
            .get()
            .addOnSuccessListener { dataSnapshot ->

                viewModel.updateCourseId(courseId)
                viewModel.updateQuestionSize(dataSnapshot.child("quiz").childrenCount.toInt())

                //Toast.makeText(this, viewModel.arrayQuestionSize.value.toString(), Toast.LENGTH_SHORT).show()
                //Toast.makeText(this, viewModel.currentQuestionIndex.value.toString(), Toast.LENGTH_SHORT).show()
                var question = dataSnapshot
                    .child("quiz")
                    .child("1")
                    .child("question")
                    .getValue(String::class.java)
                viewModel.loadCurrentQuestion(question.toString())
            }

        binding.trueBtn.setOnClickListener {
            var nextQuestion = viewModel.answer(true)
            if(nextQuestion != -1 && nextQuestion!! <= 2) {
                loadNextQuestion(nextQuestion!!)
            } else {
                getQuizResult()
            }
        }

        binding.falseBtn.setOnClickListener {
            var nextQuestion = viewModel.answer(false)
            if(nextQuestion != -1 && nextQuestion!! <= 2) {
                loadNextQuestion(nextQuestion!!)
            } else {
                getQuizResult()
            }
        }

    }

    private fun loadNextQuestion(nextQuestionIndex : Int) {
        db.child("courses")
            .child(courseId)
            .get()
            .addOnSuccessListener { dataSnapshot ->

                //Toast.makeText(this, viewModel.currentQuestionIndex.value.toString(), Toast.LENGTH_SHORT).show()
                var question = dataSnapshot
                    .child("quiz")
                    .child(nextQuestionIndex.toString())
                    .child("question")
                    .getValue(String::class.java)

                viewModel.loadCurrentQuestion(question.toString())
            }
    }

    private fun getQuizResult() {
        var count = 0
        db.child("courses")
            .child(courseId)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                for (i in 1 until 3) {

                    var correct_answer = dataSnapshot
                        .child("quiz")
                        .child(i.toString())
                        .child("response")
                        .getValue(Boolean::class.java)

                    var user_answer = viewModel.getUserAnswer(i)
                    if (user_answer == correct_answer) {
                        count++
                    }
                }
                Toast.makeText(this, "Correct: $count", Toast.LENGTH_SHORT).show()
            }
    }
}
