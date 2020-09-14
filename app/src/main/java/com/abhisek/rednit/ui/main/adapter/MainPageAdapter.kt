package com.abhisek.rednit.ui.main.adapter

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.abhisek.rednit.ui.likes.view.LikesFragment
import com.abhisek.rednit.ui.userprofile.view.UserProfileFragment
import com.abhisek.rednit.ui.profileList.view.ProfileListFragment

class MainPageAdapter(fragment: Fragment) : FragmentStateAdapter(fragment){
    override fun getItemCount(): Int {
        return 3
    }



    override fun createFragment(position: Int): Fragment {
        return when(position){
            0 -> UserProfileFragment()
            1 -> ProfileListFragment()
            else -> LikesFragment()
        }
    }

}