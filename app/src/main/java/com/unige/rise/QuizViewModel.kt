package com.unige.rise

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.database.*

class QuizViewModel : ViewModel() {

    private var db: DatabaseReference = FirebaseDatabase.getInstance().reference

    private val _courseId = MutableLiveData<String>()
    private val _currentQuestionIndex = MutableLiveData<Int>(0)
    private val _currentQuestion = MutableLiveData("Question")
    private val _arrayQuestionSize = MutableLiveData<Int>(0)
    private val _arrayAnswers = MutableLiveData<List<Boolean>>()

    val courseId: LiveData<String> get() = _courseId
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex
    val currentQuestion: LiveData<String> get() = _currentQuestion
    val arrayQuestionSize: LiveData<Int> get() = _arrayQuestionSize
    val arrayAnswers: LiveData<List<Boolean>> get() = _arrayAnswers

    fun initQuiz(courseId: String?, sizeArray : Int) {
        _courseId.value = courseId
        _currentQuestionIndex.value = 1
        _arrayQuestionSize.value = sizeArray
        _arrayAnswers.value = emptyList()

        loadQuestionNumber(courseId.toString(), _currentQuestionIndex.value.toString())
    }

    fun loadQuestionNumber(courseID : String, questionIndex : String) {
        db.child("courses").child(courseID).child("quiz").child(questionIndex)
            .get()
            .addOnSuccessListener { dataSnapshot ->
                val question = dataSnapshot.child("question").getValue(String::class.java)
                updateCurrentQuizQuestion(question.toString())
            }
            .addOnFailureListener { exception ->

                Log.e("QuizError", "Errore durante il caricamento della domanda", exception)
            }
    }

    fun updateCurrentQuizQuestion(currQuestion : String) {
        _currentQuestion.value = currQuestion
    }

    fun answer(userAnswer : Boolean) {
        if (_currentQuestionIndex.value!! < _arrayQuestionSize.value!!) {
            _currentQuestionIndex.value = _currentQuestionIndex.value?.plus(1)

            val currentAnswers = _arrayAnswers.value?.toMutableList() ?: mutableListOf()
            currentAnswers.add(userAnswer)
            _arrayAnswers.value = currentAnswers

            loadQuestionNumber(courseId.toString(), _currentQuestionIndex.value.toString())
        } else {
            var correct = 0

            for (i in 1 until _arrayQuestionSize.value!!) {
                db.child("courses")
                    .child(courseId.toString())
                    .child("quiz")
                    .child(i.toString())
                    .get()
                    .addOnSuccessListener { dataSnapshot ->
                        val correct_answer =
                            dataSnapshot.child("answer").getValue(Boolean::class.java)

                        if (correct_answer == _arrayAnswers.value?.get(i-1)) {
                            correct++
                        }
                    }
            }
        }
    }
}
