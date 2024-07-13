package com.unige.rise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class HomeViewModel : ViewModel() {
    // Use LiveData in ViewModel
    private val _welcomeText = MutableLiveData("Welcome Anonymous!")
    val welcomeText: LiveData<String> get() = _welcomeText

    fun updateWelcomeText(newWelcomeText : String) {
        _welcomeText.value = "Welcome $newWelcomeText"
    }
}