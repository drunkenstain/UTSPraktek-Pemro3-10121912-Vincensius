package com.example.uts

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.uts.other.GalleryFragment
import com.example.uts.other.MediaFragment

class OtherPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> GalleryFragment()
            1 -> MediaFragment()
            else -> Fragment()
        }
    }
}
