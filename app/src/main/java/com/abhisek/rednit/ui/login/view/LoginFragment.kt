package com.abhisek.rednit.ui.login.view

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.LoginFragmentBinding
import com.abhisek.rednit.viewmodel.UserViewModel

class LoginFragment : Fragment() {

    private val viewModel: UserViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            activity,
            UserViewModel.Factory(activity.application)
        ).get(UserViewModel::class.java)
    }

    private lateinit var binding: LoginFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.login_fragment, container, false)

        binding.lifecycleOwner = this
        binding.userViewModel = viewModel

        viewModel.isSignedIn.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { signedIn: Boolean ->
               if(signedIn) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                    return@observe
                }

                binding.splashScreen.visibility = View.GONE
                binding.btnLogIn.visibility = View.VISIBLE
            }
        })

        viewModel.loginSuccess.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { success ->
                if (success) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToMainFragment())
                }
            }
        })

        viewModel.startRegistration.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { newUser ->
                if (newUser) {
                    findNavController().navigate(LoginFragmentDirections.actionLoginFragmentToSignUpFragment())
                }
            }
        })

        viewModel.errorMessage.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { errorMsg ->
                if (errorMsg.isNotEmpty()) {
                    binding.txtError.text = errorMsg
                }
            }
        })

        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        (activity as AppCompatActivity).supportActionBar?.hide()
    }
}