package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ViewCardOtherProfileBinding
import com.slepetbangkit.cinematch.util.GlideApp

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

    fun setProfileImage(imageUrl: String?) {
        if (imageUrl == "null") return

        GlideApp.with(binding.imgProfile.context)
            .load(imageUrl)
            .error(R.drawable.account_circle_24)
            .circleCrop()
            .into(binding.imgProfile)
    }

    fun setFollowingCount(count: Int) {
        binding.tvFollowingCount.text = count.toString()
    }

    fun setFollowersCount(count: Int) {
        binding.tvFollowerCount.text = count.toString()
    }

    fun setUsername(username: String) {
        binding.tvUname.text = username
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