package com.slepetbangkit.cinematch.view.profile.movielist.otherprofile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.MoviesItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieListOtherBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.view.profile.movielist.MovieListViewModel

class MovieListOtherFragment : Fragment() {
    private var _binding: FragmentMovieListOtherBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var movieListRepository: MovieListRepository
    private lateinit var factory: MovieListViewModelFactory
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var otherProfileMovieListAdapter: OtherProfileMovieListAdapter
    private lateinit var navController: NavController
    private lateinit var listId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        listId = arguments?.getString("listId") ?: ""

        _binding = FragmentMovieListOtherBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        movieListRepository = Injection.provideMovieListRepository(requireContext())

        factory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository)
        factory.updateListId(listId)

        movieListViewModel = ViewModelProvider(this, factory)[MovieListViewModel::class.java]
        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        observeViewModel()
        fetchMovieList()
    }

    override fun onResume() {
        super.onResume()
        fetchMovieList()
    }

    private fun observeViewModel() {
        movieListViewModel.movieListDetails.observe(viewLifecycleOwner) { movie ->
            movie.apply {
                otherProfileMovieListAdapter = OtherProfileMovieListAdapter()
                binding.rvMovies.apply {
                    layoutManager = LinearLayoutManager(context)
                    adapter = otherProfileMovieListAdapter
                }
                otherProfileMovieListAdapter.submitList(movies)

                binding.tvTitle.text = title
                binding.tvUser.text = getString(R.string.by_username, movie.username)

                binding.tvDesc.visibility = if (movie.description.isEmpty()) View.GONE else View.VISIBLE
                binding.tvDesc.text = movie.description

                binding.btnBack.setOnClickListener { navController.navigateUp() }

                otherProfileMovieListAdapter.setOnItemClickCallback(object : OtherProfileMovieListAdapter.OnItemClickCallback {
                    override fun onItemClicked(data: MoviesItem) {
                        data.tmdbId.let { tmdbId ->
                            val bundle = Bundle().apply {
                                putInt("tmdbId", tmdbId)
                            }
                            navController.navigate(R.id.action_navigation_movie_list_other_to_movieDetailsFragment, bundle)
                        }
                    }
                })
            }
        }

        movieListViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.movieListContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        movieListViewModel.movies.observe(viewLifecycleOwner) { movies ->
            binding.emptyTv.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
        }
    }

    private fun fetchMovieList() {
        val listId = arguments?.getString("listId") ?: ""
        movieListViewModel.fetchMovieListDetails(listId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
