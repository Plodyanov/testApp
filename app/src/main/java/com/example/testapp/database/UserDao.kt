package com.example.testapp.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface UserDao {

    @Query("SELECT * FROM user_table ORDER BY email ASC")
    fun getAllUsers(): Flow<List<User>>

//    @Query("SELECT * FROM user_table WHERE email = :email")
//    suspend fun findUser(email: String): User

    @Query("SELECT EXISTS(SELECT * FROM user_table WHERE email = :email)")
    suspend fun checkUserExists(email: String): Boolean

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insert(user: User)

    @Query("DELETE FROM user_table WHERE email = :email")
    suspend fun deleteUser(email: String)

    @Query("DELETE FROM user_table")
    suspend fun deleteAll()

    @Query("SELECT * FROM user_table WHERE email = :email")
    suspend fun getUser(email: String): User?

    @Query("UPDATE user_table SET profile_picture_uri = :uri WHERE email = :email")
    suspend fun updateProfilePicture(email: String, uri: String?)
}