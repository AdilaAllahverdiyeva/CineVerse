package com.example.cineverse

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.cineverse.databinding.FragmentSavedBinding

class SavedFragment : Fragment() {
    private var _binding: FragmentSavedBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieViewModel: MovieViewModel
    private lateinit var savedAdapter: SavedMoviesAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSavedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieViewModel = ViewModelProvider(requireActivity()).get(MovieViewModel::class.java)

        // Adapteri qururuq (boş siyahı ilə başlanır)
        savedAdapter = SavedMoviesAdapter(emptyList())

        // RecyclerView-i konfiqurasiya edirik
        binding.savedRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.savedRecyclerView.adapter = savedAdapter

        // ViewModel-də saxlanan filmləri dinləyirik və adapteri yeniləyirik
        movieViewModel.savedMovies.observe(viewLifecycleOwner) { movies ->
            savedAdapter.updateMovies(movies)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
