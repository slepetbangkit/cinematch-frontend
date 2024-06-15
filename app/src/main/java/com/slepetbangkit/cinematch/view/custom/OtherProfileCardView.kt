package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ViewCardOtherProfileBinding

class OtherProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewCardOtherProfileBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewCardOtherProfileBinding.inflate(inflater, this, true)
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

    fun setIsFollowed(isFollowed: Boolean) {
        if (isFollowed) {
            binding.btnFollow.visibility = GONE
            binding.btnUnfollow.visibility = VISIBLE
        } else {
            binding.btnFollow.visibility = VISIBLE
            binding.btnUnfollow.visibility = GONE
        }
    }

    fun setIsFollowingUser(isFollowingUser: Boolean) {
        if (isFollowingUser) {
            binding.btnFollow.text = context.getString(R.string.follow_back)
        } else {
            binding.btnFollow.text = context.getString(R.string.follow)
        }
    }

    fun setFollowingLayoutClickListener(listener: OnClickListener) {
        binding.followingLayout.setOnClickListener(listener)
    }

    fun setFollowersLayoutClickListener(listener: OnClickListener) {
        binding.followersLayout.setOnClickListener(listener)
    }

    fun setFollowButtonClickListener(listener: OnClickListener) {
        binding.btnFollow.setOnClickListener(listener)
    }

    fun setUnfollowButtonClickListener(listener: OnClickListener) {
        binding.btnUnfollow.setOnClickListener(listener)
    }
}