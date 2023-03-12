package com.example.testapp.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class User(
    @ColumnInfo(name = "first_name") val firstName: String,
    @ColumnInfo(name = "last_name") val lastName: String,
    @PrimaryKey val email: String,
    val password: String = email,
    @ColumnInfo(name = "profile_picture_uri") var profilePictureUri: String? = null
)
