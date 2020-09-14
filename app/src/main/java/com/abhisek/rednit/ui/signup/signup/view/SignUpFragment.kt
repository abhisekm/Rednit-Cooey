package com.abhisek.rednit.ui.signup.signup.view


import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.SignUpFragmentBinding
import com.abhisek.rednit.ui.signup.signup.viewmodel.SignUpViewModel

class SignUpFragment : Fragment() {

    private val viewModel: SignUpViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        ViewModelProvider(
            activity,
            SignUpViewModel.Factory(activity.application)
        ).get(SignUpViewModel::class.java)
    }

    private lateinit var binding: SignUpFragmentBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.sign_up_fragment, container, false)

        binding.lifecycleOwner = this
        binding.signupViewModel = viewModel

        viewModel.registrationSuccess.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { emailPassword ->
                findNavController().navigate(
                    SignUpFragmentDirections.actionSignUpFragmentToBasicInfoFragment(
                        emailPassword.first,
                        emailPassword.second
                    )
                )
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
}