package com.example.footapp.ui.customer

import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class BannerFragmentStateAdapter(val bannerList: ArrayList<String>, val fragment: FragmentActivity) :
    FragmentStateAdapter(fragment) {
    override fun getItemCount(): Int {
        return bannerList.size
    }

    override fun createFragment(position: Int): Fragment {
        val fragment = BannerFragment()
        fragment.arguments = Bundle().apply {
            this.putString("banner",bannerList[position])
        }
        return fragment
    }
}
