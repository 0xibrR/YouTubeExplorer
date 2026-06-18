package com.example.youtubeexplorer.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.youtubeexplorer.fragment.DetailsFragment
import com.example.youtubeexplorer.fragment.VideosFragment

class ViewPagerAdapter(
    activity: FragmentActivity,
    private val videosFragment: VideosFragment,
    private val detailsFragment: DetailsFragment
) : FragmentStateAdapter(activity) {

    override fun getItemCount() = 2

    override fun createFragment(position: Int): Fragment {
        return if (position == 0)
            videosFragment
        else
            detailsFragment
    }
}