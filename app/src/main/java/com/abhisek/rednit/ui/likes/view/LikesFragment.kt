package com.abhisek.rednit.ui.likes.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.LikesFragmentBinding
import com.abhisek.rednit.ui.likes.adapter.LikesAdapter
import com.abhisek.rednit.viewmodel.ProfilesViewModel

class LikesFragment : Fragment() {
    private val viewModel: ProfilesViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            activity,
            ProfilesViewModel.Factory(activity.application)
        ).get(ProfilesViewModel::class.java)
    }

    private lateinit var binding: LikesFragmentBinding
    private val manager by lazy { GridLayoutManager(context,2) }

    private val adapter by lazy { LikesAdapter() }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.likes_fragment, container, false)

        binding.lifecycleOwner = this

        binding.rvLikes.layoutManager = manager
        binding.rvLikes.adapter = adapter

        viewModel.likedProfiles.observe(viewLifecycleOwner, {
            adapter.submitList(it)
        })

        return binding.root
    }



}