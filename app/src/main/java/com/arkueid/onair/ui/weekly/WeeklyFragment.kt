package com.arkueid.onair.ui.weekly

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.postDelayed
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.arkueid.onair.databinding.FragmentWeeklyBinding
import com.arkueid.onair.event.weekly.WeeklySubjectEvent
import com.arkueid.onair.utils.Result
import com.arkueid.onair.utils.ToastUtils
import com.google.android.material.tabs.TabLayoutMediator
import com.scwang.smart.refresh.header.ClassicsHeader
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import org.greenrobot.eventbus.EventBus
import java.time.LocalDate
import java.util.Date


@AndroidEntryPoint
class WeeklyFragment : Fragment() {

    companion object {
        const val TAG = "WeeklyFragment"
    }

    private lateinit var binding: FragmentWeeklyBinding

    private lateinit var viewModel: WeeklyViewModel

    private lateinit var adapter: WeeklyPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel = ViewModelProvider(this)[WeeklyViewModel::class.java]

        adapter = WeeklyPagerAdapter(this)
        binding.weeklyViewPager.adapter = adapter
        binding.weeklyViewPager.offscreenPageLimit = 7


        TabLayoutMediator(binding.weekdayTab, binding.weeklyViewPager) { tab, index ->
            tab.text = numToHan(index)
        }.attach()

        binding.refreshLayout.setOnRefreshListener { getWeeklyData() }

        // first loading
        if (savedInstanceState == null) {
            binding.initProgressBar.visibility = View.VISIBLE
            view.post { getWeeklyData() }
            // set default tab according to the weekday
            binding.weeklyViewPager.setCurrentItem(viewModel.currentTabIndex, false)
        }
    }

    private fun getWeeklyData() {
        lifecycleScope.launch {
            viewModel.weeklySubjects.collectLatest {
                if (it.isFailure) {
                    ToastUtils.showContextToast(requireContext(), "${it.error!!.message}")
                } else {
                    (binding.refreshLayout.refreshHeader as ClassicsHeader).setLastUpdateTime(Date())
                }

                binding.refreshLayout.finishRefresh(it.isSuccess)
                binding.initProgressBar.visibility = View.INVISIBLE
            }
        }
    }

    private fun numToHan(index: Int): String {
        if (index == viewModel.currentTabIndex) return "今"
        return when (index) {
            0 -> "一"
            1 -> "二"
            2 -> "三"
            3 -> "四"
            4 -> "五"
            5 -> "六"
            6 -> "日"
            else -> ""
        }
    }
}