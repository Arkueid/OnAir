package com.arkueid.onair.ui.home

import android.annotation.SuppressLint
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.View.OnClickListener
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.R
import com.arkueid.onair.databinding.FragmentHomeBinding
import com.arkueid.onair.ui.home.search.SearchActivity
import com.scwang.smart.refresh.header.ClassicsHeader
import dagger.hilt.android.AndroidEntryPoint
import java.util.Date

@AndroidEntryPoint
class HomeFragment : Fragment(), OnClickListener {
    companion object {
        const val TAG = "HomeFragment"
    }

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: ModuleAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener(this)

        adapter = ModuleAdapter(emptyList())

        binding.moduleRecyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        viewModel.modules.observe(viewLifecycleOwner) {
            adapter.data = it
            adapter.notifyDataSetChanged()
        }

        viewModel.loadingState.observe(viewLifecycleOwner) {
            if (it) {
                (binding.refreshLayout.refreshHeader as ClassicsHeader).setLastUpdateTime(Date())
            }
            binding.refreshLayout.finishRefresh(it)
            binding.initProgressBar.visibility = View.INVISIBLE
        }

        if (savedInstanceState == null && !viewModel.loadingState.value!!) {
            view.post {
                binding.initProgressBar.visibility = View.VISIBLE
                viewModel.getModuleData()
            }
        }

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getModuleData()
        }
    }

    override fun onClick(v: View?) {
        when (v!!.id) {
            R.id.searchButton -> {
                val intent = Intent(context, SearchActivity::class.java)
                ActivityOptions.makeCustomAnimation(
                    context, R.anim.slide_in_bottom, android.R.anim.fade_out
                ).let {
                    startActivity(intent, it.toBundle())
                }
            }
        }
    }
}