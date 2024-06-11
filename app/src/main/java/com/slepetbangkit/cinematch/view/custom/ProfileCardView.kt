package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.slepetbangkit.cinematch.databinding.CardViewProfileBinding

class ProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: CardViewProfileBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CardViewProfileBinding.inflate(inflater, this, true)
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

    fun setEdtProfileButtonClickListener(listener: OnClickListener) {
        binding.btnEditProfile.setOnClickListener(listener)
    }

    fun setSettingsButtonClickListener(listener: OnClickListener) {
        binding.btnSettings.setOnClickListener(listener)
    }

    fun setFollowButtonClickListener(listener: OnClickListener) {
        binding.btnFollow.setOnClickListener(listener)
    }

    fun setIsOwnProfileView(isOwnProfile: Boolean) {
        if (isOwnProfile) {
            binding.btnEditProfile.visibility = VISIBLE
            binding.btnSettings.visibility = VISIBLE
            binding.btnFollow.visibility = GONE
        } else {
            binding.btnEditProfile.visibility = GONE
            binding.btnSettings.visibility = GONE
            binding.btnFollow.visibility = VISIBLE
        }
    }
}