package com.unige.rise


import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.unige.rise.databinding.ActivityQuizBinding
import kotlin.math.roundToInt

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
                val question = dataSnapshot
                    .child("quiz")
                    .child("1")
                    .child("question")
                    .getValue(String::class.java)
                viewModel.loadCurrentQuestion(question.toString())
            }

        binding.trueBtn.setOnClickListener {
            val nextQuestion = viewModel.answer(true)
            if(nextQuestion <= viewModel.getArrayQuestionSize()!!) {
                loadNextQuestion(nextQuestion)
            } else {
                getQuizResult()
            }
        }

        binding.falseBtn.setOnClickListener {
            val nextQuestion = viewModel.answer(false)
            if(nextQuestion <= viewModel.getArrayQuestionSize()!!) {
                loadNextQuestion(nextQuestion)
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
                val question = dataSnapshot
                    .child("quiz")
                    .child(nextQuestionIndex.toString())
                    .child("question")
                    .getValue(String::class.java)

                viewModel.loadCurrentQuestion(question.toString())
            }
    }

    private fun getQuizResult() {

        binding.trueBtn.visibility = View.GONE
        binding.falseBtn.visibility = View.GONE

        var count = 0
        db.child("courses")
            .child(courseId)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                for (i in 1 until (viewModel.getArrayQuestionSize()!! + 1)) {

                    val correctAnswer = dataSnapshot
                        .child("quiz")
                        .child(i.toString())
                        .child("response")
                        .getValue(Boolean::class.java)

                    val userAnswer = viewModel.getUserAnswer(i)
                    if (userAnswer == correctAnswer) {
                        count++
                    }
                }

                val score = (100*(count.toFloat() / viewModel.getArrayQuestionSize()!!)).roundToInt()
                val wrong = viewModel.getArrayQuestionSize()!! - count
                binding.question.text = "Correct: $count\nWrong: $wrong\nYou're score is $score%"
                Toast.makeText(this, "Great Job!", Toast.LENGTH_SHORT).show()

                binding.homeBtn.visibility = View.VISIBLE
                binding.homeBtn.setOnClickListener {
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                }
            }
    }
}
