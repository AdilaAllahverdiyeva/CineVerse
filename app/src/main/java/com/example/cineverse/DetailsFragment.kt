package com.example.cineverse

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.cineverse.databinding.FragmentDetailsBinding
import com.squareup.picasso.Picasso

class DetailsFragment : Fragment() {

    private var _binding: FragmentDetailsBinding? = null
    private val binding get() = _binding!!
    private val args: DetailsFragmentArgs by navArgs()
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var movie: Movie  // ✅ movie-ni lateinit kimi elan et

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDetailsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)
        movie = args.movie!!  // ✅ movie-ni burada təyin et

        Log.d("DetailsFragment", "Alınan obyekt: $movie")

        // ImageButton-u layout-dan tapırıq
        val shareButton = view.findViewById<ImageButton>(R.id.shareBtn)
        shareButton.setOnClickListener {
            // Paylaşmaq istədiyiniz linki təyin edin
            val shareLink = "https://example.com"  // Burada öz linkinizi yerləşdirin

            // Paylaşma Intent-i yaradılır
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, shareLink)
                type = "text/plain"
            }
            // İstifadəçiyə tətbiq seçimi göstərmək üçün chooser açırıq
            startActivity(Intent.createChooser(shareIntent, "Linki paylaş"))
        }

        movie.let {
            Picasso.get()
                .load(it.imageResId)
                .into(binding.movieImage)
            binding.movieTitle.text = it.title
            binding.movieYear.text = it.year
            binding.movieGenre.text = it.genre
            binding.movieDuration.text = it.duration
            binding.movieDescription.text = it.description
            setupWebView(it.trailerUrl)
        }

        binding.savedBtn.setImageResource(
            if (movie.isSaved) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
        )

        binding.savedBtn.setOnClickListener {
            movieViewModel.toggleSaveMovie(movie)
            binding.savedBtn.setImageResource(
                if (movie.isSaved) R.drawable.baseline_bookmark_24 else R.drawable.baseline_bookmark_border_24
            )
        }

        binding.backBtn.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun setupWebView(trailerUrl: String) {
        val videoId = extractYouTubeVideoId(trailerUrl)
        val embedHtml = """
            <html>
            <body style="margin:0;padding:0;">
                <iframe width="100%" height="100%" 
                    src="https://www.youtube.com/embed/$videoId?autoplay=0&controls=1"
                    frameborder="0" allowfullscreen>
                </iframe>
            </body>
            </html>
        """

        binding.videoWebView.settings.javaScriptEnabled = true
        binding.videoWebView.loadData(embedHtml, "text/html", "utf-8")
    }

    private fun extractYouTubeVideoId(url: String): String {
        val regex = ".*(?:youtu.be/|v/|u/\\w/|embed/|watch\\?v=|&v=)([^#&?]*).*".toRegex()
        val matchResult = regex.find(url)
        return matchResult?.groups?.get(1)?.value ?: ""
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
