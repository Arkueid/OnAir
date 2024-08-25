package com.arkueid.onair.presentation.weekly.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeeklyPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return WeeklySubjectListFragment.newInstance(position)
    }
}