package com.abhisek.rednit.database

import androidx.room.*
import com.abhisek.rednit.domain.Profile
import com.abhisek.rednit.domain.User

@Entity(tableName = "user_table")
data class DatabaseUser(
    @PrimaryKey
    val email: String,

    val password: String,

    val name: String,

    val dob: Long,

    val gender: String,

    @ColumnInfo(name = "picture_url")
    val pictureUrl: String,

    val undo: Int = 0
)

@Entity(tableName = "profiles_table")
data class DatabaseProfiles(
    @PrimaryKey
    val id: String,

    val picture: String,

    val name: String,

    val gender: String,

    val age: Int,

    @ColumnInfo(name = "favorite_color")
    val favoriteColor: String,
)

@Entity(tableName = "likes_table")
data class DatabaseLikes(
    @PrimaryKey
    @ColumnInfo(name = "profile_id")
    val profileId: String,

    val liked: Boolean
)

data class DatabaseLikeAndProfile(
    @Embedded val likes: DatabaseLikes,
    @Relation(
        parentColumn = "profile_id",
        entityColumn = "id"
    )
    val profile: DatabaseProfiles
)

fun DatabaseProfiles.asDomainModel(): Profile {
    return Profile(id, picture, name, gender, age, favoriteColor)
}

fun DatabaseUser.asDomainModel(): User {
    return User(email, password, name, dob, gender, pictureUrl, undo)
}