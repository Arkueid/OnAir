package com.arkueid.onair.ui.weekly

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.arkueid.onair.databinding.FragmentWeeklySubjectListBinding
import com.arkueid.onair.event.weekly.WeeklySubjectEvent
import org.greenrobot.eventbus.EventBus
import org.greenrobot.eventbus.Subscribe
import org.greenrobot.eventbus.ThreadMode


class WeeklySubjectFragment : Fragment() {

    private lateinit var binding: FragmentWeeklySubjectListBinding
    private lateinit var adapter: WeeklySubjectRecyclerViewAdapter

    private var position: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            position = it.getInt("position")
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentWeeklySubjectListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = WeeklySubjectRecyclerViewAdapter(emptyList())
        binding.subjectRecyclerView.adapter = adapter
        binding.subjectRecyclerView.layoutManager = GridLayoutManager(context, 3)
    }

    @SuppressLint("NotifyDataSetChanged")
    @Subscribe(sticky = true, threadMode = ThreadMode.MAIN)
    fun onEvent(event: WeeklySubjectEvent) {
        event.subjectsByWeek.let {
            if (it.size <= position) return
            adapter.data = it[position]
            adapter.notifyDataSetChanged()
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

    companion object {
        private const val TAG = "WeeklySubjectListFragment"

        @JvmStatic
        fun newInstance(position: Int) = WeeklySubjectFragment().apply {
            arguments = Bundle().apply {
                putInt("position", position)
            }
        }
    }
}