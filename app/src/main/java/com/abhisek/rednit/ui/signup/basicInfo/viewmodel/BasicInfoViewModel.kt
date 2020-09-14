package com.abhisek.rednit.ui.signup.basicInfo.viewmodel

import android.app.Application
import android.net.Uri
import androidx.lifecycle.*
import com.abhisek.rednit.domain.User
import com.abhisek.rednit.util.Event
import java.util.*

class BasicInfoViewModel(val email: String, val password: String, application: Application) :
    AndroidViewModel(application) {

    private val _date = MutableLiveData<String>("D D / M M / Y Y Y Y")
    val date: LiveData<String>
        get() = _date

    val name = MutableLiveData<String>("")
    private val _isMale = MutableLiveData<Boolean>(true)
    val isMale: LiveData<Boolean>
        get() = _isMale

    private val _pictureUrl = MutableLiveData<String>("")
    val pictureUrl: LiveData<String>
        get() = _pictureUrl

    private val _dateMillis = MutableLiveData<Long>(0L)
    val dateMillis: LiveData<Long>
        get() = _dateMillis

    private val _showDatePicker = MutableLiveData<Event<Long>>()
    val showDatePicker: LiveData<Event<Long>>
        get() = _showDatePicker

    private val _showImagePicker = MutableLiveData<Event<Boolean>>()
    val showImagePicker: LiveData<Event<Boolean>>
        get() = _showImagePicker

    private val _validateUser = MutableLiveData<Event<User>>()
    val validateUser: LiveData<Event<User>>
        get() = _validateUser


    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage


    fun showDatePicker() {
        _showDatePicker.value = Event(_dateMillis.value ?: Calendar.getInstance().timeInMillis)
    }

    fun updateDate(year: Int, month: Int, day: Int) {
        val cal: Calendar = Calendar.getInstance()
        cal.set(year, month, day)
        _dateMillis.value = cal.timeInMillis
        _date.value =
            ("$day/${(month + 1).toString().padStart(2, '0')}/$year").toCharArray()
                .joinToString(" ")
    }

    fun showImagePicker() {
        _showImagePicker.value = Event(true)
    }

    fun updateGender(male: Boolean) {
        _isMale.value = male
    }

    fun validateUser() {
        if (name.value!!.isEmpty()) {
            _errorMessage.value = Event("Name cannot be empty")
            return
        }

        if (_dateMillis.value!! == 0L) {
            _errorMessage.value = Event("Please select your date of birth")
            return
        }


        if (_pictureUrl.value!!.isEmpty()) {
            _errorMessage.value = Event("Please select your profile picture")
            return
        }

        val gender = if (isMale.value!!) "male" else "female"
        val user: User = User(
            email,
            password,
            name.value!!,
            _dateMillis.value!!,
            gender,
            pictureUrl.value!!
        )

        _validateUser.value = Event(user)
    }

    fun updateProfilePic(uri: Uri?) {
        _pictureUrl.value = uri.toString()
    }

    class Factory(val email: String, val password: String, val app: Application) :
        ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(BasicInfoViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return BasicInfoViewModel(email, password, app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}