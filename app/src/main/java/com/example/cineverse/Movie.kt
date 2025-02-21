package com.example.cineverse

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Movie(
    val title: String,
    val year: String,
    val duration: String,
    val genre: String,
    val description: String,
    val imageResId: String,
    val trailerUrl: String,
    var isSaved: Boolean = false
    ) : Parcelable

