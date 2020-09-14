package com.abhisek.rednit.ui.main.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.res.ResourcesCompat.getDrawable
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.MainFragmentBinding
import com.abhisek.rednit.ui.main.adapter.MainPageAdapter
import com.google.android.material.tabs.TabLayoutMediator

class MainFragment : Fragment() {

    private lateinit var binding: MainFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = DataBindingUtil.inflate(inflater, R.layout.main_fragment, container, false)

        val adapter: MainPageAdapter = MainPageAdapter(this)
        binding.viewPager.adapter = adapter
        binding.viewPager.setCurrentItem(1, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        TabLayoutMediator(binding.tabLayout, binding.viewPager) { tab, position ->
            tab.icon = getDrawable(
                resources, when (position) {
                    0 -> R.drawable.ic_round_account_circle
                    1 -> R.drawable.ic_baseline_whatshot_24
                    else -> R.drawable.ic_baseline_like
                }, activity?.theme
            )
        }.attach()
    }

}