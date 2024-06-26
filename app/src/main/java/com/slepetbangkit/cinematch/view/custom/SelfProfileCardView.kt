package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import com.slepetbangkit.cinematch.R
import com.slepetbangkit.cinematch.databinding.ViewCardSelfProfileBinding
import com.slepetbangkit.cinematch.util.GlideApp

class SelfProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: ViewCardSelfProfileBinding
    private var tempImageUrl: String? = null

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = ViewCardSelfProfileBinding.inflate(inflater, this, true)
    }

    fun setProfileImage(imageUrl: String) {
        val slicedImageUrl = imageUrl.substringBefore("?")

        if (imageUrl == "null" || slicedImageUrl == tempImageUrl) return

        GlideApp.with(binding.imgProfile.context)
            .load(imageUrl)
            .error(R.drawable.account_circle_24)
            .circleCrop()
            .into(binding.imgProfile)
        tempImageUrl = imageUrl.substringBefore("?")
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