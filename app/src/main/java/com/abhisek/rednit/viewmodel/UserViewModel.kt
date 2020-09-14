package com.abhisek.rednit.viewmodel

import android.app.Application
import androidx.core.content.edit
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.abhisek.rednit.database.getDatabase
import com.abhisek.rednit.domain.User
import com.abhisek.rednit.repository.RednitRepository
import com.abhisek.rednit.util.Event
import kotlinx.coroutines.*

class UserViewModel(application: Application) : AndroidViewModel(application) {


    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val sharedPreference = PreferenceManager.getDefaultSharedPreferences(application)

    private val repository = RednitRepository(database, sharedPreference)

    val email = MutableLiveData<String>()
    val password = MutableLiveData<String>()

    private val _loginSuccess = MutableLiveData<Event<Boolean>>()
    val loginSuccess: LiveData<Event<Boolean>>
        get() = _loginSuccess

    private val _startRegistration = MutableLiveData<Event<Boolean>>()
    val startRegistration: LiveData<Event<Boolean>>
        get() = _startRegistration

    private val _navigateToUserInfo = MutableLiveData<Event<Boolean>>()
    val navigateToUserInfo: LiveData<Event<Boolean>>
        get() = _navigateToUserInfo

    private val _registrationComplete = MutableLiveData<Event<Boolean>>()
    val registrationComplete: LiveData<Event<Boolean>>
        get() = _registrationComplete

    private val _isSignedIn = MutableLiveData<Event<Boolean>>()
    val isSignedIn: LiveData<Event<Boolean>>
        get() = _isSignedIn

    private val _errorMessage = MutableLiveData<Event<String>>()
    val errorMessage: LiveData<Event<String>>
        get() = _errorMessage

    private val _user = MutableLiveData<User>()
    val user: LiveData<User>
        get() = _user

    init {
        viewModelScope.launch {
            val user: User? = withContext(Dispatchers.IO) {
                delay(2000)
                return@withContext loadUser()
            }

            _isSignedIn.value = Event(user?.name?.isNotEmpty() ?: false)
        }
    }

    private suspend fun loadUser(): User? {
        val user = repository.getUser()
        withContext(Dispatchers.Main) { _user.value = user }
        return user
    }

    fun validateLogin() {
        viewModelScope.launch {
            if (email.value?.isNotEmpty() == true) {
                if (password.value?.isNotEmpty() == true) {
                    val user = repository.getUser(email.value!!)
                    user?.let {
                        if (it.password == password.value!!) {
                            _loginSuccess.value = Event(true)
                            return@launch
                        }
                    }
                    _errorMessage.value = Event("Invalid email id or password")
                } else {
                    _errorMessage.value = Event("Password field is empty!")
                }
            } else {
                _errorMessage.value = Event("Email field is empty!")
            }
        }

    }

    fun startRegistration() {
        _startRegistration.value = Event(true)
    }

    fun goToUserInfo() {
        _navigateToUserInfo.value = Event(true)
    }

    fun validateRegistration() {
        _registrationComplete.value = Event(true)
    }

    fun logInUser(user: User) {
        viewModelScope.launch {
            try {
                repository.createNewUser(user)
                loadUser()
                _registrationComplete.value = Event(true)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return UserViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}