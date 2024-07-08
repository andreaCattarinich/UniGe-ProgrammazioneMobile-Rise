package com.unige.rise

import android.widget.TextView
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.Firebase
import com.google.firebase.auth.auth

class MainViewModel : ViewModel(){
    private val _username = MutableLiveData<String>("Name Surname")
    private val _email = MutableLiveData<String>("rise@project.com")

    val username: LiveData<String> get() = _username
    val email: LiveData<String> get() = _email

    fun setupNavigationDrawerData() {
        // Set user information in the fragment
        val user = Firebase.auth.currentUser
        user?.let {
            _username.value = it.displayName
            _email.value = it.email
        }
    }
}