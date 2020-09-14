package com.abhisek.rednit.ui.signup.basicInfo.view

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.abhisek.rednit.R
import com.abhisek.rednit.databinding.BasicInfoFragmentBinding
import com.abhisek.rednit.ui.signup.basicInfo.viewmodel.BasicInfoViewModel
import com.abhisek.rednit.viewmodel.UserViewModel
import com.bumptech.glide.Glide
import com.esafirm.imagepicker.features.ImagePicker
import java.util.*

class BasicInfoFragment : Fragment(), DatePickerDialog.OnDateSetListener {
    private val viewModel: UserViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }

        ViewModelProvider(
            activity,
            UserViewModel.Factory(activity.application)
        ).get(UserViewModel::class.java)
    }


    private val basicInfoViewModel: BasicInfoViewModel by lazy {
        val activity = requireNotNull(this.activity) {
            "You can only access the viewModel after onActivityCreated()"
        }
        val arguments = BasicInfoFragmentArgs.fromBundle(requireArguments())

        ViewModelProvider(
            activity,
            BasicInfoViewModel.Factory(arguments.email, arguments.password, activity.application)
        ).get(BasicInfoViewModel::class.java)
    }

    private lateinit var binding: BasicInfoFragmentBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.basic_info_fragment, container, false)

        binding.lifecycleOwner = this
        binding.userViewModel = viewModel
        binding.basicInfoViewModel = basicInfoViewModel

        basicInfoViewModel.showDatePicker.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { timeInMillis ->
                val calendar: Calendar = Calendar.getInstance()
                if (timeInMillis > 0) {
                    calendar.timeInMillis = timeInMillis
                }
                val day = calendar.get(Calendar.DAY_OF_MONTH)
                val month = calendar.get(Calendar.MONTH)
                val year = calendar.get(Calendar.YEAR)
                val datePickerDialog =
                    DatePickerDialog(requireContext(), this@BasicInfoFragment, year, month, day)
                datePickerDialog.datePicker.maxDate =
                    Calendar.getInstance().timeInMillis - (13 * 365 * 24 * 60 * 60 * 1000L) // Atleast 13yr old
                datePickerDialog.show()
            }
        })

        basicInfoViewModel.showImagePicker.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { show ->
                if (show) {
                    ImagePicker.create(this)
                        .single()
                        .start()
                }
            }
        })

        basicInfoViewModel.validateUser.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { user ->
                user?.apply {
                    viewModel.logInUser(this)
                }
            }
        })

        viewModel.registrationComplete.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let {
                findNavController().navigate(BasicInfoFragmentDirections.actionBasicInfoFragmentToMainFragment())
            }
        })

        basicInfoViewModel.errorMessage.observe(viewLifecycleOwner, {
            it.getContentIfNotHandled()?.let { errorMsg ->
                binding.txtError.text = errorMsg
            }
        })

        return binding.root
    }

    override fun onDateSet(p0: DatePicker?, year: Int, month: Int, date: Int) {
        basicInfoViewModel.updateDate(year, month, date)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (ImagePicker.shouldHandle(requestCode, resultCode, data)) {
            // or get a single image only
            val image = ImagePicker.getFirstImageOrNull(data)
            basicInfoViewModel.updateProfilePic(image.uri)
            Glide.with(binding.imageView).load(image.uri).into(binding.imageView)
        }
        super.onActivityResult(requestCode, resultCode, data)
    }
}