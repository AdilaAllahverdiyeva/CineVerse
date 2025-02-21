package com.example.cineverse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class SharedViewModel : ViewModel() {
    // Download olunan filmlərin siyahısı
    private val _downloadList = MutableLiveData<MutableList<Movie>>(mutableListOf())
    val downloadList: LiveData<MutableList<Movie>>
        get() = _downloadList

    fun addFilm(film: Movie) {
        _downloadList.value?.add(film)
        // Dəyişikliklərin müşahidə olunması üçün LiveData-nı yenidən təyin edirik
        _downloadList.value = _downloadList.value
    }
}
