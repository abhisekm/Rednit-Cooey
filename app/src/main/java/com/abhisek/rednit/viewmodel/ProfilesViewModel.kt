package com.abhisek.rednit.viewmodel

import android.accounts.NetworkErrorException
import android.app.Application
import androidx.lifecycle.*
import androidx.preference.PreferenceManager
import com.abhisek.rednit.database.RednitDatabase
import com.abhisek.rednit.database.getDatabase
import com.abhisek.rednit.domain.Like
import com.abhisek.rednit.domain.Profile
import com.abhisek.rednit.domain.addUndoCount
import com.abhisek.rednit.repository.RednitRepository
import com.abhisek.rednit.util.Event
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import java.io.IOException

class ProfilesViewModel(application: Application) : AndroidViewModel(application) {
    private val viewModelJob = SupervisorJob()
    private val viewModelScope = CoroutineScope(viewModelJob + Dispatchers.Main)

    private val database = getDatabase(application)
    private val sharedPreference = PreferenceManager.getDefaultSharedPreferences(application)
    private val repository = RednitRepository(database, sharedPreference)

    val profiles: LiveData<List<Profile>> = repository.profiles
    val likedProfiles: LiveData<List<Profile>> = repository.likedProfiles

    private val _eventLikeButtonClicked = MutableLiveData<Event<Boolean>>()
    val eventLikeButtonClicked: LiveData<Event<Boolean>>
        get() = _eventLikeButtonClicked

    private val _eventSkipButtonClicked = MutableLiveData<Event<Boolean>>()
    val eventSkipButtonClicked: LiveData<Event<Boolean>>
        get() = _eventSkipButtonClicked

    private val _eventUndoButtonClicked = MutableLiveData<Event<Boolean>>()
    val eventUndoButtonClicked: LiveData<Event<Boolean>>
        get() = _eventUndoButtonClicked

    private val _eventShowPaywall = MutableLiveData<Event<Boolean>>()
    val eventShowPaywall: LiveData<Event<Boolean>>
        get() = _eventShowPaywall

    init {
        viewModelScope.launch {
            try {
                repository.loadProfiles()
            } catch (networkError: IOException) {
                networkError.printStackTrace()
            }
        }
    }

    fun likeProfile() {
        _eventLikeButtonClicked.value = Event(true)
    }

    fun skipProfile() {
        _eventSkipButtonClicked.value = Event(true)
    }

    fun undoProfile() {
        viewModelScope.launch {
            val user = repository.getUser()
            user?.apply {
                if (undo > 2) {
                    _eventShowPaywall.value = Event(true)
                    return@launch
                }

                _eventUndoButtonClicked.value = Event(true)
            }
        }
    }

    fun addLikedProfile(profile: Profile?) {
        profile?.let {
            viewModelScope.launch {
                val like = Like(it.id, true)
                repository.addLike(like)
            }
        }
    }

    fun addSkippedProfile(profile: Profile?) {
        profile?.let {
            viewModelScope.launch {
                val like = Like(it.id, false)
                repository.addLike(like)
            }
        }
    }

    fun undoLastActionProfile(profile: Profile?) {
        profile?.let {
            viewModelScope.launch {
                val user = repository.getUser()
                user?.apply {
                    val like = Like(it.id, false)
                    repository.undoAction(like)
                    addUndoCount().let { it1 -> repository.updateUserProfile(it1) }
                }
            }
        }
    }

    class Factory(val app: Application) : ViewModelProvider.Factory {
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ProfilesViewModel::class.java)) {
                @Suppress("UNCHECKED_CAST")
                return ProfilesViewModel(app) as T
            }
            throw IllegalArgumentException("Unable to construct viewmodel")
        }
    }
}