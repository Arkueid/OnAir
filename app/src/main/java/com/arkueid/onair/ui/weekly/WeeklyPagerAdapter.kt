package com.arkueid.onair.ui.weekly

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class WeeklyPagerAdapter(fragment: Fragment) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int = 7

    override fun createFragment(position: Int): Fragment {
        return WeeklySubjectFragment.newInstance(position)
    }
}