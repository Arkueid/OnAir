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
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.arkueid.onair.SourceViewModel
import com.arkueid.onair.R
import com.arkueid.onair.databinding.FragmentHomeBinding
import com.arkueid.onair.event.SourceChangedEvent
import com.arkueid.onair.ui.home.search.SearchActivity
import com.arkueid.onair.utils.ToastUtils
import com.scwang.smart.refresh.header.ClassicsHeader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode
import java.util.Date
import javax.inject.Inject

@AndroidEntryPoint
class HomeFragment : Fragment(), OnClickListener {
    companion object {
        const val TAG = "HomeFragment"
    }

    private lateinit var binding: FragmentHomeBinding

    private val viewModel by viewModels<HomeViewModel>()
    private lateinit var adapter: ModuleAdapter

    @Inject
    lateinit var sourceViewModel: SourceViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.searchButton.setOnClickListener(this)

        adapter = ModuleAdapter(emptyList())

        binding.moduleRecyclerView.let {
            it.adapter = adapter
            it.layoutManager = LinearLayoutManager(context)
        }

        binding.refreshLayout.setOnRefreshListener { getModuleData() }

        if (savedInstanceState == null) {
            binding.initProgressBar.visibility = View.VISIBLE
//            view.post { getModuleData() }
        }
    }

    @SuppressLint("NotifyDataSetChanged")
    private fun getModuleData() {
        lifecycleScope.launch {
            viewModel.modules.collectLatest {
                if (it.isSuccess) {
                    (binding.refreshLayout.refreshHeader as ClassicsHeader).setLastUpdateTime(Date())
                } else {
                    ToastUtils.showContextToast(requireContext(), it.error?.message ?: "加载失败")
                }
                binding.refreshLayout.finishRefresh(it.isSuccess)
                binding.initProgressBar.visibility = View.INVISIBLE

                // bind data
                adapter.data = it.data ?: emptyList()
                adapter.notifyDataSetChanged()
            }
        }
    }

    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onSourceChanged(event: SourceChangedEvent) {
        getModuleData()
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

    override fun onResume() {
        super.onResume()
        EventBus.getDefault().register(this)
    }

    override fun onPause() {
        super.onPause()
        EventBus.getDefault().unregister(this)
    }
}