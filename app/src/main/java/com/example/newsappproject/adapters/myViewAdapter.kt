package com.example.newsappproject.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.newsappproject.ui.FavoriteFragment
import com.example.newsappproject.ui.HomeFragment
import com.example.newsappproject.ui.SearchFragment

class myViewAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(
    fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 3
    }

    override fun createFragment(position: Int): Fragment {
        return when(position) {
            0 -> HomeFragment()
            1 -> FavoriteFragment()
            2 -> SearchFragment()
            else -> {HomeFragment()}
        }
    }

}