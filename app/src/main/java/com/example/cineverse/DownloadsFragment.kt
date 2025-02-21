package com.example.cineverse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class DownloadsFragment : Fragment() {

    private lateinit var sharedViewModel: SharedViewModel
    private lateinit var adapter: SavedMoviesAdapter  // Öz RecyclerView adapter sinifiniz

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_download.xml layout faylını inflate edirik
        return inflater.inflate(R.layout.fragment_downloads, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        sharedViewModel = ViewModelProvider(requireActivity()).get(SharedViewModel::class.java)

        val recyclerView = view.findViewById<RecyclerView>(R.id.downloadRecyclerView)
        adapter = SavedMoviesAdapter(emptyList())  // Adapter sinifinizin nümunəsini yaradın
        recyclerView.layoutManager = LinearLayoutManager(context)
        recyclerView.adapter = adapter

        // Download siyahısı dəyişdikcə adapter-i yeniləyirik
        sharedViewModel.downloadList.observe(viewLifecycleOwner, Observer { films ->
            adapter.updateMovies(films.toList())
        })
    }
}
