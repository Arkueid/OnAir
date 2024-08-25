package com.arkueid.onair.ui.weekly

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.arkueid.onair.databinding.FragmentWeeklyBinding
import com.arkueid.onair.event.weekly.WeeklySubjectEvent
import com.google.android.material.tabs.TabLayoutMediator
import dagger.hilt.android.AndroidEntryPoint
import org.greenrobot.eventbus.EventBus
import java.time.LocalDate


@AndroidEntryPoint
class WeeklyFragment : Fragment() {

    companion object {
        private const val TAG = "WeeklyFragment"
    }

    private lateinit var binding: FragmentWeeklyBinding

    private lateinit var viewModel: WeeklyViewModel

    private lateinit var adapter: WeeklyPagerAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[WeeklyViewModel::class.java]

        viewModel.weeklySubjects.observe(viewLifecycleOwner) {
            EventBus.getDefault().postSticky(WeeklySubjectEvent(it))
        }

        adapter = WeeklyPagerAdapter(this)

        binding.weeklyViewPager.adapter = adapter

        viewModel.weeklySubjects.observe(viewLifecycleOwner) {
            EventBus.getDefault().postSticky(WeeklySubjectEvent(it))
        }

        viewModel.loadingState.observe(viewLifecycleOwner) {
            binding.initialLoading.visibility = View.INVISIBLE
            binding.refreshLayout.finishRefresh(it)
        }

        TabLayoutMediator(binding.weekdayTab, binding.weeklyViewPager) { tab, index ->
            tab.text = numToHan(index)
        }.attach()

        binding.refreshLayout.setOnRefreshListener {
            viewModel.getWeeklySubjects()
        }

        binding.refreshLayout.setEnableLoadMore(false)
        binding.refreshLayout.setEnableOverScrollDrag(true)

        // first loading
        if (savedInstanceState == null) {
            view.post {
                binding.initialLoading.visibility = View.VISIBLE
                viewModel.getWeeklySubjects()
            }
        }
    }

    private fun numToHan(index: Int): String {
        if (index == LocalDate.now().dayOfWeek.value - 1) return "今"
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