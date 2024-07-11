package com.unige.rise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class QuizViewModel : ViewModel() {

    private val _courseId = MutableLiveData<String>()
    private val _currentQuestionIndex = MutableLiveData<Int>(1)
    private val _currentQuestionFraction = MutableLiveData<String>("1/5")
    private val _currentQuestion = MutableLiveData("Question")
    private val _arrayQuestionSize = MutableLiveData<Int>(0)
    private val _arrayAnswers = MutableLiveData<List<Boolean>>()

    val courseId: LiveData<String> get() = _courseId
    val currentQuestionIndex: LiveData<Int> get() = _currentQuestionIndex
    val currentQuestionFraction: LiveData<String> get() = _currentQuestionFraction
    val currentQuestion: LiveData<String> get() = _currentQuestion
    val arrayQuestionSize: LiveData<Int> get() = _arrayQuestionSize
    val arrayAnswers: LiveData<List<Boolean>> get() = _arrayAnswers

    // GETTERS
    fun getUserAnswer(i: Int) : Boolean {
        return _arrayAnswers.value?.get(i-1)!!
    }

    fun getArrayQuestionSize() : Int {
        return _arrayQuestionSize.value!!.toInt()
    }


    fun updateCourseId(newId : String) {
        _courseId.value = newId
    }

    fun updateQuestionSize(newSize: Int) {
        _arrayQuestionSize.value = newSize
    }

    fun loadCurrentQuestion(newQuestion : String) {
        _currentQuestion.value = newQuestion
        _currentQuestionFraction.value = _currentQuestionIndex.value.toString() + "/" + _arrayQuestionSize.value.toString()
    }

    fun answer(userAnswer : Boolean): Int? {
        if(_currentQuestionIndex.value!!.toInt() <= _arrayQuestionSize.value!!.toInt()) {
            _currentQuestionIndex.value = _currentQuestionIndex.value!!.plus(1)

            val answers = _arrayAnswers.value?.toMutableList() ?: mutableListOf()
            answers.add(userAnswer)
            _arrayAnswers.value = answers
        }

        return _currentQuestionIndex.value!!.toInt()
    }



}
