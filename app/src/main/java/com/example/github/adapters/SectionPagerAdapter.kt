package com.example.github.adapters

import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.github.fragments.FollowersFragment
import com.example.github.fragments.FollowingFragment

class SectionPagerAdapter(activity: AppCompatActivity, username: String) :
    FragmentStateAdapter(activity) {

    private val username = username

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        var fragment: Fragment? = null
        when (position) {
            0 -> fragment = FollowersFragment(username)
            1 -> fragment = FollowingFragment(username)
        }
        return fragment as Fragment
    }
}