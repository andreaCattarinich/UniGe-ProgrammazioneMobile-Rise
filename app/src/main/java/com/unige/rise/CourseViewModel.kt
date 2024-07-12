package com.unige.rise

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CourseViewModel : ViewModel() {
    private val _titleText = MutableLiveData("Title")
    private val _subtitleText = MutableLiveData("Subtitle")
    private val _descriptionText = MutableLiveData("Description")

    val titleText: LiveData<String> get() = _titleText
    val subtitleText: LiveData<String> get() = _subtitleText
    val descriptionText: LiveData<String> get() = _descriptionText

    fun updateTitleText(newTitleText : String) {
        _titleText.value = newTitleText
    }

    fun updateSubtitleText(newSubtitleText : String) {
        _subtitleText.value = newSubtitleText
    }

    fun updateDescriptionText(newDescriptionText : String) {
        _descriptionText.value = newDescriptionText
    }
}