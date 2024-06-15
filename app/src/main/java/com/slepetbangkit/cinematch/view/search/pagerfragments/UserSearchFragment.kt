package com.slepetbangkit.cinematch.view.search.pagerfragments

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.data.preferences.dataStore
import com.slepetbangkit.cinematch.data.remote.response.UsersItem
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.data.repository.UserRepository
import com.slepetbangkit.cinematch.databinding.FragmentUserSearchBinding
import com.slepetbangkit.cinematch.di.Injection
import com.slepetbangkit.cinematch.factories.SearchUserViewModelFactory
import com.slepetbangkit.cinematch.view.search.adapter.UserAdapter
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchUserViewModel
import kotlinx.coroutines.launch

class UserSearchFragment : Fragment(){
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var userRepository: UserRepository
    private lateinit var factory: SearchUserViewModelFactory
    private lateinit var searchUserViewModel: SearchUserViewModel
    private lateinit var userAdapter: UserAdapter
    private lateinit var navController: NavController
    private var sessionUsername = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        sessionRepository = Injection.provideSessionRepository(requireContext())
        userRepository = Injection.provideUserRepository(requireContext())
        factory = SearchUserViewModelFactory.getInstance(sessionRepository, userRepository)
        searchUserViewModel = ViewModelProvider(this, factory)[SearchUserViewModel::class.java]
        navController = findNavController()

        lifecycleScope.launch {
            sessionUsername = sessionRepository.getUsername()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setupRecyclerView()
        setupSearchInput()

        searchUserViewModel.searchUserResult.observe(viewLifecycleOwner) { users ->
            userAdapter.submitList(users)
        }

        searchUserViewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        searchUserViewModel.error.observe(viewLifecycleOwner) { error ->
            Log.e("UserSearchFragment", "Error: $error")
        }
    }

    private fun setupRecyclerView() {
        userAdapter = UserAdapter()
        binding.userRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = userAdapter
        }
        userAdapter.setOnItemClickCallback(object : UserAdapter.OnItemClickCallback {
            override fun onItemClicked(data: UsersItem) {
                data.username.let { username ->
                    if (username == sessionUsername) {
                        navController.navigate(R.id.action_navigation_search_to_navigation_self_profile)
                        return
                    } else {
                        val bundle = Bundle().apply {
                            putString("username", username)
                        }
                        navController.navigate(R.id.action_navigation_search_to_navigation_other_profile, bundle)
                    }
                }
            }
        })
    }

    private fun setupSearchInput() {
        binding.searchTv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.searchTv.text.toString().trim()
                if (query.isNotEmpty()) {
                    lifecycleScope.launch {
                        searchUserViewModel.searchUser(query)
                        v.clearFocus()
                        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                        imm.hideSoftInputFromWindow(v.windowToken, 0)
                    }
                }
                true
            } else {
                false
            }
        }
    }

}
