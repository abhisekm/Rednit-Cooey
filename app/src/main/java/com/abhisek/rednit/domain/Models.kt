package com.abhisek.rednit.domain

import androidx.room.ColumnInfo
import com.abhisek.rednit.database.DatabaseLikes
import com.abhisek.rednit.database.DatabaseUser
import com.abhisek.rednit.util.Helper
import java.time.LocalDate
import java.util.*

data class User(
    val email: String,

    val password: String,

    val name: String,

    val dob: Long,

    val gender: String,

    val pictureUrl: String = "",

    val undo: Int = 0
) {
    val age: String = "${Helper.getYears(dob)} years"
}

data class Profile(
    val id: String,

    val picture: String,

    val name: String,

    val gender: String,

    val age: Int,

    @ColumnInfo(name = "favorite_color")
    val favoriteColor: String,
) {
    val shortGender: String = if (gender == "male") "M" else "F"
}

data class Like(
    val profileId: String,
    val liked: Boolean
)

fun User.asDatabaseModel(): DatabaseUser {
    return DatabaseUser(email, password, name, dob, gender, pictureUrl, undo)
}

fun Like.asDatabaseModel(): DatabaseLikes {
    return DatabaseLikes(profileId, liked)
}

fun User.addUndoCount(): User {
    return User(email, password, name, dob, gender, pictureUrl, undo + 1)
}