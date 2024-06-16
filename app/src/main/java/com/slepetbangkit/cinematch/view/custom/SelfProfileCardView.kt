package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.os.Bundle
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.findNavController
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ViewCardSelfProfileBinding

class SelfProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewCardSelfProfileBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewCardSelfProfileBinding.inflate(inflater, this, true)
    }

    fun setProfileImage() {
        // TO-DO: Set profile image
    }

    fun setFollowingCount(count: Int) {
        binding.tvFollowingCount.text = count.toString()
    }

    fun setFollowersCount(count: Int) {
        binding.tvFollowerCount.text = count.toString()
    }

    fun setUsername(username: String) {
        binding.tvUname.text = buildString {
            append("@")
            append(username)
        }
    }

    fun setBio(bio: String) {
        binding.tvBio.text = bio
    }

    fun setFollowingLayoutClickListener(listener: OnClickListener) {
        binding.followingLayout.setOnClickListener(listener)
    }

    fun setFollowersLayoutClickListener(listener: OnClickListener) {
        binding.followersLayout.setOnClickListener(listener)
    }

    fun setEdtProfileButtonClickListener(listener: OnClickListener) {
        binding.btnEditProfile.setOnClickListener(listener)
    }

    fun setSettingsButtonClickListener(listener: OnClickListener) {
        binding.btnSettings.setOnClickListener(listener)
    }
}