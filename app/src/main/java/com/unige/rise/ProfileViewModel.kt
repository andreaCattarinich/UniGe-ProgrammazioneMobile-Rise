package com.unige.rise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // Use LiveData in ViewModel
    private val _displayName = MutableLiveData("Name")
    private val _email = MutableLiveData("rise@project.com")
    private val _phoneNumber = MutableLiveData("+33 1234567890")
    private val _courseCompletion = MutableLiveData("75%")

    val displayName: LiveData<String> get() = _displayName
    val email: LiveData<String> get() = _email
    val phoneNumber: LiveData<String> get() = _phoneNumber
    val courseCompletion: LiveData<String> get() = _courseCompletion

    fun updateDisplayName(newName : String) {
        _displayName.value = newName
    }

    fun updateEmail(newEmail : String) {
        _email.value = newEmail
    }

    fun updatePhoneNumber(newPhoneNumber : String) {
        _phoneNumber.value = newPhoneNumber
    }

    fun updateCourseCompletion(newCourseCompletion : Int) {
        _courseCompletion.value = "$newCourseCompletion%"
    }
}