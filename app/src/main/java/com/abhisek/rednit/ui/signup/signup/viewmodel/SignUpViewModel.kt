package com.abhisek.rednit.ui.signup.signup.viewmodel

import android.app.Application
import androidx.lifecycle.*
import com.abhisek.rednit.util.Event

class SignUpViewModel(application: Application) : AndroidViewModel(application) {
    val email = MutableLiveData<String>("")
    val password = MutableLiveData<String>("")

    private val _registrationSuccess = MutableLiveData<Event<Pair<String, String>>>()
    val registrationSuccess: LiveData<Event<Pair<String, String>>>
        get() = _registrationSuccess

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    fun validateRegistration() {
        if(email.value!!.isEmpty()){
            _errorMessage.value = Event("Email field is empty!")
            return
        }

        if(password.value!!.isEmpty()){
            _errorMessage.value = Event("Password field is empty!")
            return
        }

       if(email.value!!.isNotEmpty() && password.value!!.isNotEmpty()){
           _registrationSuccess.value = Event(Pair(email.value!!, password.value!!))
       }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(SignUpViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return SignUpViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}