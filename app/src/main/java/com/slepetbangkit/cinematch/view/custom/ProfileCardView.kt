package com.slepetbangkit.cinematch.view.custom

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.slepetbangkit.cinematch.databinding.CustomViewProfileCardBinding

class ProfileCardView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    private val binding: CustomViewProfileCardBinding

    init {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        binding = CustomViewProfileCardBinding.inflate(inflater, this, true)
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
}