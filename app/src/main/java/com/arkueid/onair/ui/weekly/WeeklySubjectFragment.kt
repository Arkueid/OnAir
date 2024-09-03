package com.arkueid.onair.ui.weekly

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.recyclerview.widget.GridLayoutManager
import com.arkueid.onair.databinding.FragmentWeeklySubjectListBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


@AndroidEntryPoint
class WeeklySubjectFragment : Fragment() {

    private lateinit var binding: FragmentWeeklySubjectListBinding
    private lateinit var adapter: WeeklySubjectRecyclerViewAdapter
    private lateinit var viewModel: WeeklyViewModel

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

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(requireParentFragment())[WeeklyViewModel::class.java]

        adapter = WeeklySubjectRecyclerViewAdapter(emptyList())
        binding.subjectRecyclerView.adapter = adapter
        binding.subjectRecyclerView.layoutManager = GridLayoutManager(context, 3)

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.RESUMED) {
                viewModel.currentWeeklySubjects.collectLatest {
                    adapter.data = it.getOrElse(position) { emptyList() }
                    adapter.notifyDataSetChanged()
                }
            }
        }

        Log.e(TAG, "onViewCreated: $viewModel")
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