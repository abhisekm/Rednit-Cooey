package com.abhisek.rednit.ui.userprofile.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.UserProfileFragmentBinding
import com.abhisek.rednit.viewmodel.UserViewModel

class UserProfileFragment : Fragment() {


    private val viewModel: UserViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            activity,
            UserViewModel.Factory(activity.application)
        ).get(UserViewModel::class.java)
    }
    private lateinit var binding: UserProfileFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.user_profile_fragment, container, false)

        viewModel.user.observe(viewLifecycleOwner, {
            binding.user = it
        })

        return binding.root
    }

}