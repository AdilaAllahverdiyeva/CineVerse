package com.example.cineverse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ImageView
import android.widget.TextView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class MovieAdapter(
    private var movies: List<Movie>,
    private val itemClickListener: (Movie) -> Unit = {}
) : RecyclerView.Adapter<MovieAdapter.MovieViewHolder>() {



    inner class MovieViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.movieImage)
        val titleView: TextView = view.findViewById(R.id.movieTitle)
        val genreView: TextView = view.findViewById(R.id.movieGenre)

        /*       init {
            view.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    itemClickListener(movies[position])
                }
            }
        }
    }
*/
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_movie, parent, false)
        return MovieViewHolder(view)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val movie = movies[position]
        holder.titleView.text = movie.title
        holder.genreView.text = movie.genre
        Picasso.get().load(movie.imageResId).into(holder.imageView)

        // Tıklama olayını ekleyin
        /*      holder.itemView.setOnClickListener { view ->
            val bundle = Bundle().apply {
                putParcelable("selectedMovie", movie)
            }
            view.findNavController().navigate(R.id.action_homeFragment_to_detailsFragment, bundle)
        } */
        holder.itemView.setOnClickListener {
            itemClickListener(movie)
        }
    }
    class MovieDiffCallback : DiffUtil.ItemCallback<Movie>() {
        override fun areItemsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem.imageResId == newItem.imageResId
        }

        override fun areContentsTheSame(oldItem: Movie, newItem: Movie): Boolean {
            return oldItem == newItem
        }
    }

    override fun getItemCount() = movies.size

    fun updateMovies(newMovies: List<Movie>) {
        movies = newMovies
        notifyDataSetChanged()
    }

    fun updateList(newList: List<Movie>) {
        movies = newList
        notifyDataSetChanged()
    }
}
