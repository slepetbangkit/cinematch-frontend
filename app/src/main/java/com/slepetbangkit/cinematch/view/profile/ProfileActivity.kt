package com.slepetbangkit.cinematch.view.profile

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.slepetbangkit.cinematch.data.local.preferences.SessionPreferences
import com.slepetbangkit.cinematch.data.local.preferences.dataStore
import com.slepetbangkit.cinematch.databinding.ActivityProfileBinding
import com.slepetbangkit.cinematch.helpers.ViewModelFactory

class ProfileActivity : AppCompatActivity() {
    private lateinit var binding: ActivityProfileBinding
    private lateinit var sessionPrefs: SessionPreferences
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProfileBinding.inflate(layoutInflater)
        sessionPrefs = SessionPreferences.getInstance(this.dataStore)
        profileViewModel = ViewModelFactory.getInstance(sessionPrefs).create(ProfileViewModel::class.java)
        setContentView(binding.root)

        profileViewModel.profile.observe(this) {
            it.followingCount?.let { followingCount -> binding.profileCard.setFollowingCount(followingCount) }
            it.followerCount?.let { followersCount -> binding.profileCard.setFollowersCount(followersCount) }
            it.username?.let { username -> binding.profileCard.setUsername(username) }
            it.bio?.let { bio -> binding.profileCard.setBio(bio) }
        }

        profileViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) {
                binding.profileCard.visibility = View.GONE
                binding.shimmerViewContainer.let {
                    it.startShimmer()
                    it.visibility = View.VISIBLE
                }
            } else {
                binding.profileCard.visibility = View.VISIBLE
                binding.shimmerViewContainer.let {
                    it.stopShimmer()
                    it.visibility = View.GONE
                }
            }
        }

        binding.profileCard.setEdtProfileButtonClickListener {
            // TO-DO: Implement edit profile feature
        }
    }
}