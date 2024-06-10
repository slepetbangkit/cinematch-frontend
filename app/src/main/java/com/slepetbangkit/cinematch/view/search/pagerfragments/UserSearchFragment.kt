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
import androidx.recyclerview.widget.LinearLayoutManager
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.data.repository.SessionRepository
import com.slepetbangkit.cinematch.databinding.FragmentUserSearchBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory
import com.slepetbangkit.cinematch.view.search.adapter.UserAdapter
import com.slepetbangkit.cinematch.view.search.viewmodels.SearchUserViewModel

class UserSearchFragment : Fragment(){
    private var _binding: FragmentUserSearchBinding? = null
    private val binding get() = _binding!!
    private lateinit var sessionRepository: SessionRepository
    private lateinit var searchUserViewModel: SearchUserViewModel
    private lateinit var userAdapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        sessionRepository = SessionRepository.getInstance(requireContext().dataStore)
        val viewModelFactory = ViewModelFactory.getInstance(sessionRepository)
        val viewModel: SearchUserViewModel by viewModels { viewModelFactory }
        searchUserViewModel = viewModel

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
    }

    private fun setupSearchInput() {
        binding.searchTv.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH || actionId == EditorInfo.IME_ACTION_DONE || event?.action == KeyEvent.ACTION_DOWN && event.keyCode == KeyEvent.KEYCODE_ENTER) {
                val query = binding.searchTv.text.toString().trim()
                if (query.isNotEmpty()) {
                    searchUserViewModel.getSearchUsers(query)
                    v.clearFocus()
                    val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(v.windowToken, 0)
                }
                true
            } else {
                false
            }
        }
    }

}