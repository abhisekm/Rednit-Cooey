package com.abhisek.rednit.repository

import android.content.SharedPreferences
import androidx.core.content.edit
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.map
import com.abhisek.rednit.database.RednitDatabase
import com.abhisek.rednit.database.asDomainModel
import com.abhisek.rednit.domain.Like
import com.abhisek.rednit.domain.Profile
import com.abhisek.rednit.domain.User
import com.abhisek.rednit.domain.asDatabaseModel
import com.abhisek.rednit.network.Network
import com.abhisek.rednit.network.asDatabaseModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RednitRepository(
    private val database: RednitDatabase,
    private val sharedPreference: SharedPreferences
) {

    companion object {
        const val EMAIL: String = "email"
    }

    val profiles = Transformations.map(database.profileDao.getAll(sharedPreference.getString(EMAIL, ""))) {
        it.map { dbProfile ->
            dbProfile.asDomainModel()
        }
    }

    val likedProfiles = Transformations.map(database.likesDao.getAllLikes()) {
        it.map { likeAndProfile ->
            likeAndProfile.profile.asDomainModel()
        }
    }

    suspend fun getUser(email: String? = sharedPreference.getString(EMAIL, "")): User? {
        return withContext(Dispatchers.IO) {
            email?.let { database.userDao.getUser(it)?.asDomainModel() }
        }
    }

    suspend fun loadProfiles() {
        withContext(Dispatchers.IO) {
            val profiles = Network.service.getProfiles()
            database.profileDao.insertAllProfiles(*profiles.asDatabaseModel())
        }
    }


    suspend fun getProfile(id: String): LiveData<Profile> {
        return withContext(Dispatchers.IO) {
            database.profileDao.getProfile(id).map {
                it.asDomainModel()
            }
        }
    }

    suspend fun getAllLike(): LiveData<List<Profile>> {
        return withContext(Dispatchers.IO) {
            database.likesDao.getAllLikes().map {
                it.map { databaseLikeAndProfile ->
                    databaseLikeAndProfile.profile.asDomainModel()
                }
            }
        }
    }

    suspend fun addLike(like: Like) {
        withContext(Dispatchers.IO) {
            database.likesDao.addLike(like.asDatabaseModel())
        }
    }

    suspend fun undoAction(like: Like) {
        withContext(Dispatchers.IO) {
            database.likesDao.removeLike(like.asDatabaseModel())
        }
    }

    suspend fun createNewUser(user: User) {
        withContext(Dispatchers.IO) {
            database.userDao.addUser(user.asDatabaseModel())
            sharedPreference.edit {
                putString(EMAIL, user.email)
                apply()
            }
        }
    }

    suspend fun updateUserProfile(user: User) {
        withContext(Dispatchers.IO) {
            database.userDao.updateUser(user.asDatabaseModel())
        }
    }
}