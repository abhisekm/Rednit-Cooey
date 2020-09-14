package com.abhisek.rednit.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [DatabaseUser::class, DatabaseLikes::class, DatabaseProfiles::class],
    version = 1,
    exportSchema = false
)

abstract class RednitDatabase : RoomDatabase() {
    abstract val userDao: UserDao
    abstract val profileDao: ProfileDao
    abstract val likesDao: LikesDao
}

private lateinit var INSTANCE: RednitDatabase

fun getDatabase(context: Context): RednitDatabase {
    synchronized(RednitDatabase::class.java) {
        if (!::INSTANCE.isInitialized) {
            INSTANCE = Room.databaseBuilder(
                context.applicationContext,
                RednitDatabase::class.java,
                "rednit"
            ).build()
        }
    }
    return INSTANCE
}