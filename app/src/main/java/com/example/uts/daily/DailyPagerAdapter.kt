package com.example.uts.daily

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uts.daily.DailyFragment

class DailyPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> DailyFragment()
            1 -> FriendlistFragment()
            else -> throw IllegalStateException("Invalid position")
        }
    }
}
