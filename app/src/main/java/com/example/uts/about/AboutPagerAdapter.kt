package com.example.uts.about

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uts.about.FindmeFragment
import com.example.uts.about.AboutFragment
import com.example.uts.about.ContactFragment


class AboutPagerAdapter(fa: FragmentActivity) : FragmentStateAdapter(fa) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> AboutFragment()
            1 -> ContactFragment()
            2 -> FindmeFragment()
            else -> AboutFragment()
        }
    }
}