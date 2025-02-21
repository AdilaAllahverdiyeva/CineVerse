package com.example.cineverse

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.cineverse.databinding.ItemSavedMovieBinding
import com.squareup.picasso.Picasso

class SavedMoviesAdapter(private var movieList: List<Movie>) :
    RecyclerView.Adapter<SavedMoviesAdapter.MovieViewHolder>() {

    inner class MovieViewHolder(private val binding: ItemSavedMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(movie: Movie) {
            binding.movieTitle.text = movie.title
            binding.movieGenre.text = movie.genre
            binding.movieYear.text = movie.year
            Picasso.get()
                .load(movie.imageResId)
                .into(binding.movieImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding = ItemSavedMovieBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        holder.bind(movieList[position])
    }

    override fun getItemCount(): Int = movieList.size

    fun updateMovies(newMovies: List<Movie>) {
        movieList = newMovies
        notifyDataSetChanged()
    }
}
