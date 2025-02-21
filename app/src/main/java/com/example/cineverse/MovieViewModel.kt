package com.example.cineverse

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MovieViewModel : ViewModel() {
    private val _savedMovies = MutableLiveData<List<Movie>>(emptyList()) // Düzəliş olundu
    val savedMovies: LiveData<List<Movie>> get() = _savedMovies

    fun toggleSaveMovie(movie: Movie) {
        val currentList = _savedMovies.value?.toMutableList() ?: mutableListOf()
        if (movie.isSaved) {
            currentList.remove(movie)
        } else {
            currentList.add(movie)
        }
        movie.isSaved = !movie.isSaved
        _savedMovies.value = currentList // `toMutableList()` artıq lazım deyil
    }
}
