package com.slepetbangkit.cinematch.view.profile.movielist.selfprofile

import android.app.AlertDialog
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
import com.slepetbangkit.cinematch.data.remote.response.Movie
import com.slepetbangkit.cinematch.data.remote.response.MoviesItem
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentMovieListBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.view.profile.movielist.MovieListViewModel

class MovieListFragment : Fragment() {

    private var _binding: FragmentMovieListBinding? = null
    private val binding get() = _binding!!
    private lateinit var movieListViewModel: MovieListViewModel
    private lateinit var selfProfileMovieListAdapter: SelfProfileMovieListAdapter
    private lateinit var navController: NavController
    private lateinit var listId: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMovieListBinding.inflate(inflater, container, false)
        listId = arguments?.getString("listId") ?: ""

        initializeViewModel()
        navController = findNavController()

        return binding.root
    }

    private fun initializeViewModel() {
        val context = requireContext()
        val sessionRepository = Injection.provideSessionRepository(context)
        val movieListRepository = Injection.provideMovieListRepository(context)
        val factory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository).apply {
            updateListId(listId)
        }

        movieListViewModel = ViewModelProvider(this, factory)[MovieListViewModel::class.java]
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeViewModel()
        fetchMovieList()
    }

    override fun onResume() {
        super.onResume()
        fetchMovieList()
    }

    private fun setupRecyclerView() {
        selfProfileMovieListAdapter = SelfProfileMovieListAdapter()
        binding.rvMovies.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = selfProfileMovieListAdapter
        }
    }

    private fun observeViewModel() {
        with(movieListViewModel) {
            movieListDetails.observe(viewLifecycleOwner) { movie ->
                updateUIWithMovieDetails(movie)
                setupAdapterCallbacks()
            }

            isLoading.observe(viewLifecycleOwner) { isLoading ->
                binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
                binding.movieListContainer.visibility = if (isLoading) View.GONE else View.VISIBLE
            }

            movies.observe(viewLifecycleOwner) { movies ->
                binding.emptyTv.visibility = if (movies.isEmpty()) View.VISIBLE else View.GONE
            }

            isDeleted.observe(viewLifecycleOwner) { isDeleted ->
                if (isDeleted) {
                    navController.navigateUp()
                }
            }
        }
    }

    private fun updateUIWithMovieDetails(movie: PlaylistsItem) {
        with(binding) {
            tvTitle.text = movie.title
            tvUser.text = getString(R.string.by_username, movie.username)

            tvDesc.visibility = if (movie.description.isEmpty()) View.GONE else View.VISIBLE
            tvDesc.text = movie.description

            btnDeleteList.visibility = if (movie.isFavorite) View.GONE else View.VISIBLE
            btnDeleteList.setOnClickListener {
                showDeleteListConfirmationDialog { movieListViewModel.deleteMovieListById(movie.id) }
            }

            btnEditList.visibility = if (movie.isFavorite) View.GONE else View.VISIBLE
            btnBack.setOnClickListener { navController.navigateUp() }
        }
        selfProfileMovieListAdapter.submitList(movie.movies)
    }

    private fun setupAdapterCallbacks() {
        selfProfileMovieListAdapter.setOnItemClickCallback(object : SelfProfileMovieListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: MoviesItem) {
                val bundle = Bundle().apply { putInt("tmdbId", data.tmdbId) }
                navController.navigate(R.id.action_movieListFragment_to_movieDetailsFragment2, bundle)
            }
        })
        selfProfileMovieListAdapter.setOnRemoveClickCallback(object : SelfProfileMovieListAdapter.OnRemoveClickCallback {
            override fun onRemoveClicked(data: MoviesItem) {
                showRemoveMovieConfirmationDialog { movieListViewModel.deleteMovieById(listId, data.tmdbId) }
            }
        })

        binding.btnEditList.setOnClickListener {
            val bundle = Bundle().apply { putString("listId", listId) }
            navController.navigate(R.id.action_navigation_movie_list_to_editListFragment, bundle)
        }
    }

    private fun showDeleteListConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle("Delete List")
            .setMessage("Are you sure you want to delete this list?")
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun showRemoveMovieConfirmationDialog(onConfirm: () -> Unit) {
        AlertDialog.Builder(requireContext(), R.style.AlertDialog)
            .setTitle("Remove Movie")
            .setMessage("Are you sure you want to remove this movie from your list?")
            .setPositiveButton("Yes") { _, _ -> onConfirm() }
            .setNegativeButton("No", null)
            .show()
    }

    private fun fetchMovieList() {
        movieListViewModel.fetchMovieListDetails(listId)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
