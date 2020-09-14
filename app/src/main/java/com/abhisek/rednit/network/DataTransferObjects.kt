package com.abhisek.rednit.network

import androidx.room.ColumnInfo
import com.abhisek.rednit.database.DatabaseProfiles
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkProfile(
    val _id: String,
    val picture: String,
    val name: String,
    val gender: String,
    val age: Int,
    val favoriteColor: String,
)

fun NetworkProfile.asDatabaseModel(): DatabaseProfiles {
    return DatabaseProfiles(_id, picture, name, gender, age, favoriteColor)
}

fun List<NetworkProfile>.asDatabaseModel(): Array<DatabaseProfiles> {
    return this.map {
        it.asDatabaseModel()
    }.toTypedArray()
}