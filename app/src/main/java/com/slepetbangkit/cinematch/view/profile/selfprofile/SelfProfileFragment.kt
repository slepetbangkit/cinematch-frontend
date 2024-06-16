package com.slepetbangkit.cinematch.view.profile.selfprofile

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.remote.response.PlaylistsItem
import com.slepetbangkit.cinematch.data.repository.MovieListRepository
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentSelfProfileBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.MovieListViewModelFactory
import com.slepetbangkit.cinematch.factories.SelfProfileViewModelFactory
import com.slepetbangkit.cinematch.view.profile.movielist.create.CreateMovieListViewModel
import kotlinx.coroutines.launch

class SelfProfileFragment : Fragment() {
    private var _binding: FragmentSelfProfileBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var movieListRepository: MovieListRepository
    private lateinit var selfProfileFactory: SelfProfileViewModelFactory
    private lateinit var selfProfileViewModel: SelfProfileViewModel
    private lateinit var movieListFactory: MovieListViewModelFactory
    private lateinit var createMovieListViewModel: CreateMovieListViewModel
    private lateinit var navController: NavController
    private lateinit var username: String
    private lateinit var movieListAdapter: ProfileMovieListAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSelfProfileBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())

        userRepository = Injection.provideUserRepository(requireContext())
        movieListRepository = Injection.provideMovieListRepository(requireContext())

        selfProfileFactory = SelfProfileViewModelFactory.getInstance(sessionRepository, userRepository)
        selfProfileViewModel = ViewModelProvider(requireActivity(), selfProfileFactory)[SelfProfileViewModel::class.java]

        movieListFactory = MovieListViewModelFactory.getInstance(sessionRepository, movieListRepository)
        createMovieListViewModel = ViewModelProvider(requireActivity(), movieListFactory)[CreateMovieListViewModel::class.java]

        navController = findNavController()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        movieListAdapter = ProfileMovieListAdapter()
        binding.movieListRv.apply {
            layoutManager = object : LinearLayoutManager(context) {
                override fun canScrollVertically(): Boolean {
                    return false
                }
            }
            adapter = movieListAdapter
        }

        selfProfileViewModel.profile.observe(viewLifecycleOwner) {
            it.followingCount.let { followingCount -> binding.profileCard.setFollowingCount(followingCount) }
            it.followerCount.let { followersCount -> binding.profileCard.setFollowersCount(followersCount) }
            it.username.let { uname ->
                binding.profileCard.setUsername(uname)
                username = uname
            }
            it.bio.let { bio -> binding.profileCard.setBio(bio) }
            it.playlists.let { playlists -> movieListAdapter.submitList(playlists) }
        }

        selfProfileViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            if (isLoading) {
                binding.profileCard.visibility = View.GONE
                binding.listsTv.visibility = View.GONE
                binding.btnAddMovielist.visibility = View.GONE
                binding.shimmerViewContainer.let {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                }
                binding.movieListRv.visibility = View.GONE
                binding.btnAddMovielist.visibility = View.GONE
                binding.listsTv.visibility = View.GONE
            } else {
                binding.profileCard.visibility = View.VISIBLE
                binding.listsTv.visibility = View.VISIBLE
                binding.btnAddMovielist.visibility = View.VISIBLE
                binding.shimmerViewContainer.let {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
                binding.movieListRv.visibility = View.VISIBLE
                binding.btnAddMovielist.visibility = View.VISIBLE
                binding.listsTv.visibility = View.VISIBLE
            }
        }

        binding.profileCard.setFollowingLayoutClickListener {
            val bundle = Bundle().apply {
                putInt("tabIndex", 0)
            }
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_self_follow_list, bundle)
        }

        binding.profileCard.setFollowersLayoutClickListener {
            val bundle = Bundle().apply {
                putInt("tabIndex", 1)
            }
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_self_follow_list, bundle)
        }

        binding.profileCard.setEdtProfileButtonClickListener {
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_edit_profile)
        }

        binding.profileCard.setSettingsButtonClickListener {
            navController.navigate(R.id.action_navigation_self_profile_to_navigation_settings)
        }

        movieListAdapter.setOnItemClickCallback(object : ProfileMovieListAdapter.OnItemClickCallback {
            override fun onItemClicked(data: PlaylistsItem) {
                data.id.let { listId ->
                    val bundle = Bundle().apply {
                        putString("listId", listId)
                    }
                    navController.navigate(R.id.action_navigation_self_profile_to_movieListFragment, bundle)
                }
            }
        })

        binding.btnAddMovielist.setOnClickListener {
            showAddMovieListDialog()
        }

        createMovieListViewModel.createMovieListResult.observe(viewLifecycleOwner) { isCreated ->
            if (isCreated != null) {
                fetchProfileData()
            }
        }
    }

    private fun showAddMovieListDialog() {
        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.view_add_movie_list, null)
        val dialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogView)
            .create()

        val edtListName = dialogView.findViewById<EditText>(R.id.edt_list_title)
        val btnCancel = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_cancel)
        val btnCreate = dialogView.findViewById<com.google.android.material.button.MaterialButton>(R.id.btn_create)

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        edtListName.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                updateCreateButtonState(edtListName, btnCreate)
            }

            override fun afterTextChanged(s: Editable?) {}
        })

        btnCreate.setOnClickListener {
            val listName = edtListName.text.toString()
            if (listName.isNotBlank()) {
                createMovieListViewModel.createNewList(listName)
                dialog.dismiss()
            }
        }

        updateCreateButtonState(edtListName, btnCreate)

        dialog.show()
    }

    private fun updateCreateButtonState(editText: EditText, button: com.google.android.material.button.MaterialButton) {
        val isValid = editText.text.toString().isNotBlank()
        button.apply {
            isClickable = isValid
            background.alpha = if (isValid) 255 else 77
        }
    }

    override fun onResume() {
        super.onResume()
        fetchProfileData()
    }

    private fun fetchProfileData() {
        selfProfileViewModel.viewModelScope.launch {
            selfProfileViewModel.getSelfProfile()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
