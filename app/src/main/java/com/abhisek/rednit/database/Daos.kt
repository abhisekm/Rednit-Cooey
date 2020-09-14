package com.abhisek.rednit.database

import androidx.lifecycle.LiveData
import androidx.room.*

@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addUser(user: DatabaseUser)

    @Update
    fun updateUser(user: DatabaseUser)

    @Query("select * from user_table where email = :email")
    fun getUser(email: String) : DatabaseUser?

}

@Dao
interface ProfileDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAllProfiles(vararg profiles: DatabaseProfiles)

//    @Query("""
//        select * from profiles_table pt
//        inner join likes_table lt on pt.id = lt.profile_id
//        where lt.profile_id is null
//    """)
    @Query("select * from profiles_table pt where gender <> (select gender from user_table where email = :email)")
    fun getAll(email: String?): LiveData<List<DatabaseProfiles>>

    @Query("select * from profiles_table where id = :profileId")
    fun getProfile(profileId: String): LiveData<DatabaseProfiles>
}

@Dao
interface LikesDao {
    @Transaction
    @Query("select * from likes_table where liked=1")
    fun getAllLikes(): LiveData<List<DatabaseLikeAndProfile>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun addLike(like: DatabaseLikes)

    @Delete
    fun removeLike(like: DatabaseLikes)
}