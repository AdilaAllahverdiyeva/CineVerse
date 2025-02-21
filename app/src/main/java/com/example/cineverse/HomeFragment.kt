package com.example.cineverse

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RadioGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.navigation.NavDirections
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import com.example.cineverse.databinding.FragmentHomeBinding

interface OnMovieClickListener {
    fun onItemClick(movie: Movie)
}


class HomeFragment : Fragment(), OnMovieClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieAdapter: MovieAdapter
    private lateinit var recyclerView: RecyclerView
    private lateinit var categoryGroup: RadioGroup

    private val movies = listOf(
        Movie("Artemis Fowl", "2020", "1 hour 30 minutes","Fantasy", "On the coast of Ireland, a media frenzy descends on Fowl Manor, where a stolen collection of world-famous relics is linked to wealthy businessman, Artemis Fowl I. Arrested at the manor, Mulch Diggums is interrogated by British intelligence and claims that his employer has stolen the powerful Aculos. Offering to prove the existence of magic, Diggums tells the story of Artemis Fowl Jr.","https://images.squarespace-cdn.com/content/v1/51b3dc8ee4b051b96ceb10de/1583164576290-HVI58WJODGJTVXL2G6FA/artemis-fowl-poster-1209380.jpegAdventurous+New+Trailer+For+Disney%27s+Fantasy+Film+ARTEMIS+FOUL1", "https://youtu.be/rA0NnDagh28?feature=shared"),
        Movie("Fairy Quenn", "2024", "2 hour 15 minutes","Fantasy", "Lured to a remote farmhouse by a deceitful relative, a family falls victim to malevolent fairies who need sacrifices to renew their magical life force.", "https://m.media-amazon.com/images/S/pv-target-images/addc7aa6bbdb16fdc9461802ddd76a41ff37d63953bd58791ff97cb79f3b28c6.jpg", "https://youtu.be/-3btfMEsVH4?feature=shared"),
        Movie("Fast & Furious", "2009", "1 hour 20 minutes","Drama", "High school car enthusiast Sean Boswell is sent to live in Tokyo with his father and finds solace in the city's drifting community. Vin Diesel makes a cameo appearance as Dominic Toretto at the end of the film.","https://upload.wikimedia.org/wikipedia/az/f/f8/Forsaj_4_%28film%2C_2009%29.jpg", "https://youtu.be/LPLPz92BMDk?feature=shared"),
        Movie("Culpa Mia", "2023", "2 hour 15 minutes","Romantic", "17-year-old Noah and her mother Rafaela pack and move to live with Rafaela's new husband, billionaire William Leister. Noah is upset over leaving behind her boyfriend Dan and her friends, and is frustrated by the extremely lavish lifestyle William lives that she is now a part of. ", "https://m.media-amazon.com/images/M/MV5BNTI4ZWQ5YjYtMmYwMy00OWFmLWFmNDYtZGNlNWMxNTllZjc4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg", "https://youtu.be/3CpKBAPqqM0?feature=shared"),
        Movie("Lucifer", "2016", "1 hours 15 minutes", "Drama", "The series focuses on Lucifer Morningstar, a powerful angel who was cast out of Heaven for his rebellion and forced to spend millennia punishing people as the lord of Hell. ", "https://resizing.flixster.com/PYMILH2RwjmJ3uCZyBAEDihOIG4=/ems.cHJkLWVtcy1hc3NldHMvdHZzZXJpZXMvUlRUVjI3OTYxMS53ZWJw", "https://youtu.be/X4bF_quwNtw?feature=shared"),
        Movie("Aladdin", "2019", "1 hour 30 minutes","Fantasy", "In the fictional city of Agrabah (based on Baghdad), an orphaned street urchin named Aladdin and his monkey, Abu, meet Princess Jasmine, who has snuck away from her sheltered life in the palace.","https://i.ebayimg.com/images/g/Q2oAAOSwa79i~Nat/s-l1200.jpg", "https://youtu.be/-G5XI61Y9ms?feature=shared"),
        Movie("Chupchupke", "2006", "2 hour 15 minutes", "Comedy", "Jeetu, the son of a retired teacher named Jaidev Prasad, is debt-ridden due to many failed businesses. Jeetu breaks off his engagement with his fiancé, Pooja and decides to commit suicide by throwing himself into the sea to claim insurance money that his father could use to pay off his debts. However, Jeetu is found tangled in the fishing nets by a pair of fishermen, Gundya and Bandya, who rescue him and mistake him to be mute and deaf." , "https://images.javatpoint.com/top10-technologies/images/top-10-comedy-movies-in-bollywood1.png", "https://youtu.be/1EAtI2FJ1Z4?feature=shared"),
        Movie("Culpa Tuya", "2024", "1 hour 30 minutes","Romantic", "Nick and Noah's relationship seems unwavering despite their parents' attempts to separate them. Nick is willing to strengthen his relationship with Noah and prove that he is no longer the Nick he used to be: the one who was into fighting, racing, and girls. Nick starts working for his father's law firm alongside an attractive colleague, Sofía; and Noah goes to college, where she shares an apartment with Briar, an enigmatic and fun girl who hides a dark secret behind her smile.","https://m.media-amazon.com/images/M/MV5BYTE0NDkwM2YtZmYwMS00NTRmLWI3YWYtMzY5Njc5NDk4YWFlXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg", "https://youtu.be/m_TWESxP_DE?feature=shared"),
        Movie("Little Man", "2006","1 hour 30 minutes","Comedy", "In Chicago, Calvin Babyface Simms is a very short convicted jewel thief who gets released from prison and meets up with his dimwitted cohort Percy. Percy tells Calvin of a job involving stealing a valuable diamond, ordered by a mobster named Mr. Walken. ", "https://cdn-images-1.medium.com/v2/resize:fit:1600/1*LnEpMHLa0Eg0FsIlBAu-WQ.jpeg", "https://youtu.be/n6ir-qPI2PU?feature=shared"),
        Movie("The Godfather", "1972","1 hour 30 minutes","Drama", "The Godfather is a trilogy of American crime films directed by Francis Ford Coppola inspired by the 1969 novel of the same name by Italian American author Mario Puzo.","https://ejazkhancinema.com/wp-content/uploads/2023/09/The-Godfather-best-drama-movies-blog-by-ejaz-khan-cinema.jpeg", "https://youtu.be/30ZepuATL2w?feature=shared"),
        Movie("Spider-Man", "2021",  "2 hour 15 minutes","Action","With Spider-Man's identity now revealed, Peter asks Doctor Strange for help. When a spell goes wrong, dangerous foes from other worlds start to appear.", "https://m.media-amazon.com/images/M/MV5BODY2MTAzOTQ4M15BMl5BanBnXkFtZTgwNzg5MTE0MjI@._V1_.jpg", "https://youtu.be/JfVOs4VSpmA?feature=shared"),
        Movie("Titanic", "1997", "1 hour 30 minutes","Romantic", "RMS Titanic was a British ocean liner that sank in the early hours of 15 April 1912 as a result of striking an iceberg on her maiden voyage from Southampton, England, to New York City, United States. It was the second time White Star Line had lost a ship on its maiden voyage, the first being the RMS Tayleur in 1854. Of the estimated 2,224 passengers and crew aboard, approximately 1,500 died (figures vary), making the incident one of the deadliest peacetime sinkings of a single ship.","https://m.media-amazon.com/images/M/MV5BYzYyN2FiZmUtYWYzMy00MzViLWJkZTMtOGY1ZjgzNWMwN2YxXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg", "https://youtu.be/I7c1etV7D7g?feature=shared"),
        Movie("The Martian", "2015", "1 hour 30 minutes","Action", "An astronaut becomes stranded on Mars after his team assume him dead, and must rely on his ingenuity to find a way to signal to Earth that he is alive and can survive until a potential rescue.", "https://lumiere-a.akamaihd.net/v1/images/image_a119dd78.jpeg?region=0%2C0%2C800%2C1200", "https://youtu.be/Ue4PCI0NamI?feature=shared"),
        Movie("The Hurt Locker", "2008",  "1 hour 30 minutes","Action", "During the second year of the Iraq War, a U.S. Army Explosive Ordnance Disposal team with Bravo Company identifies and attempts to destroy an improvised explosive device with a robot, but the wagon carrying the trigger charge breaks.", "https://hips.hearstapps.com/hmg-prod/images/best-action-movies-the-hurt-locker-1675267236.jpeg", "https://youtu.be/ti_6QLp0xHw?feature=shared"),
        Movie("The Hangover", "2009", "2 hour 15 minutes","Comedy", "Two days before his wedding, bachelor Doug Billings travels to Las Vegas with his best friends Phil Wenneck, a sarcastic secondary school teacher, Stu Price, an apprehensive dentist, and Alan Garner, his odd future brother-in-law. ", "https://m.media-amazon.com/images/M/MV5BMTM2MTM4MzY2OV5BMl5BanBnXkFtZTcwNjQ3NzI4NA@@._V1_.jpg", "https://youtu.be/tlize92ffnY?feature=shared")
    )




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        recyclerView = binding.recyclerView
        movieAdapter = MovieAdapter(movies) {movie ->
            onItemClick(movie)
        }
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = movieAdapter
        return binding.root
    }

    override fun onItemClick(movie: Movie) {

        // Göndərən fragmentdə:
        Log.d("Navigation", "Göndərilən obyekt: $movies")
        val action = HomeFragmentDirections.actionHomeFragmentToDetailsFragment(movie)
        findNavController().navigate(action)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.seeAllbtn).setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_mostPopularFragment)
        }


        recyclerView = view.findViewById(R.id.recyclerView)
        categoryGroup = view.findViewById(R.id.categoryGroup)

        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        recyclerView.adapter = movieAdapter

        categoryGroup.setOnCheckedChangeListener { _, checkedId ->
            val filteredMovies = when (checkedId) {
                R.id.allButton -> movies
                R.id.actionButton -> movies.filter { it.genre == "Action" }
                R.id.comedyButton -> movies.filter { it.genre == "Comedy" }
                R.id.romanticButton -> movies.filter { it.genre == "Romantic" }
                R.id.dramaButton -> movies.filter { it.genre == "Drama" }
                else -> movies
            }
            movieAdapter.updateList(filteredMovies)
        }
        initBanner()
    }

    private fun banners(items: ArrayList<SliderItems>) {
        binding.viewPagerSlider.adapter = SliderAdapter(items, binding.viewPagerSlider)
        binding.viewPagerSlider.clipToPadding = false
        binding.viewPagerSlider.clipChildren = false
        binding.viewPagerSlider.offscreenPageLimit = 3
        (binding.viewPagerSlider.getChildAt(0) as? RecyclerView)?.overScrollMode =
            RecyclerView.OVER_SCROLL_NEVER

        val compositePageTransformer = CompositePageTransformer().apply {
            addTransformer(MarginPageTransformer(40))
        }
        binding.viewPagerSlider.setPageTransformer(compositePageTransformer)
    }

    private fun initBanner() {
        val items = arrayListOf(
            SliderItems("https://m.media-amazon.com/images/I/91SdwPcfUnL._AC_UF1000,1000_QL80_.jpg"),
            SliderItems("https://m.media-amazon.com/images/M/MV5BNTI4ZWQ5YjYtMmYwMy00OWFmLWFmNDYtZGNlNWMxNTllZjc4XkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg"),
            SliderItems("https://m.media-amazon.com/images/M/MV5BYTE0NDkwM2YtZmYwMS00NTRmLWI3YWYtMzY5Njc5NDk4YWFlXkEyXkFqcGc@._V1_FMjpg_UX1000_.jpg")
        )

        banners(items)
        binding.progressBarBanner.visibility = View.GONE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
