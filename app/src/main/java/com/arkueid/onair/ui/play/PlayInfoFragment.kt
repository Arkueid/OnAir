package com.arkueid.onair.ui.play

import android.graphics.Bitmap
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.transition.ChangeBounds
import androidx.transition.TransitionManager
import com.arkueid.onair.R
import com.arkueid.onair.databinding.FragmentPlayInfoBinding
import com.arkueid.plugin.data.entity.Anime
import com.arkueid.onair.ui.following.FollowingViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * @author: Arkueid
 * @date: 2024/9/3
 * @desc:
 */
@AndroidEntryPoint
class PlayInfoFragment : Fragment() {

    companion object {
        private const val TAG = "PlayInfoFragment"
    }

    private lateinit var binding: FragmentPlayInfoBinding
    private lateinit var anime: Anime

    @Inject
    lateinit var followingViewModel: FollowingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentPlayInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            anime = it.getParcelable<Anime>("anime")!!
            Glide.with(binding.cover).asBitmap().placeholder(R.drawable.placeholder)
                .addListener(object : RequestListener<Bitmap> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Bitmap>,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.cover.run {
                            layoutParams = layoutParams.apply {
                                height =
                                    resources.getDimension(R.dimen.wideModuleItemHeight).toInt()
                            }
                        }
                        return false
                    }

                    override fun onResourceReady(
                        resource: Bitmap,
                        model: Any,
                        target: Target<Bitmap>?,
                        dataSource: DataSource,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.cover.run {
                            setImageBitmap(resource)
                            layoutParams = layoutParams.apply {
                                height =
                                    resources.getDimension(R.dimen.wideModuleItemHeight).toInt()
                            }
                        }
                        return true
                    }

                }).load(anime.cover).into(binding.cover)
            binding.title.text = anime.name
            binding.description.text = getText(R.string.re0_brief)
        }

        binding.expandBtn.setOnClickListener {
            binding.expandBtn.run { isSelected = !isSelected }
            TransitionManager.beginDelayedTransition(binding.root,
                ChangeBounds().apply { duration = 300 })
            if (binding.expandBtn.isSelected) {
                binding.title.maxLines = Int.MAX_VALUE
                binding.description.maxLines = Int.MAX_VALUE
                binding.cover.run {
                    layoutParams = layoutParams.apply {
                        height = 0
                    }
                }
            } else {
                binding.title.maxLines = 1
                binding.description.maxLines = 2
                binding.cover.run {
                    layoutParams = layoutParams.apply {
                        height = resources.getDimension(R.dimen.wideModuleItemHeight).toInt()
                    }
                }
            }
        }

        followingViewModel.isFollowed(anime).let {
            binding.followBtn.isSelected = it
        }

        binding.followBtn.setOnClickListener {
            binding.followBtn.run {
                isEnabled = false
                isSelected = !isSelected
                if (isSelected) {
                    followingViewModel.addFollowedAnime(anime)
                } else {
                    followingViewModel.removeFollowedAnime(anime)
                }
                postDelayed(1000) {
                    isEnabled = true
                }
            }
        }
    }
}