package com.arkueid.onair.ui.following

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.databinding.FragmentFollowingBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject


@AndroidEntryPoint
class FollowingFragment : Fragment() {
    companion object {
        const val TAG = "FollowingFragment"
    }

    private lateinit var binding: FragmentFollowingBinding

    @Inject
    lateinit var viewModel: FollowingViewModel

    private lateinit var adapter: FollowedAnimeAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentFollowingBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FollowedAnimeAdapter {
            if (it.liked) {
                viewModel.addFollowedAnime(it)
            } else {
                viewModel.removeFollowedAnime(it)
            }
        }
        binding.followedAnimeList.adapter = adapter
        binding.followedAnimeList.layoutManager = LinearLayoutManager(requireContext())

        viewModel.followedAnimeList.observe(viewLifecycleOwner) {
            adapter.submitList(it)
        }
    }
}