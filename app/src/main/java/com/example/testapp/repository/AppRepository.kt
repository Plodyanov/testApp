package com.example.testapp.repository

import androidx.annotation.WorkerThread
import com.example.testapp.database.User
import com.example.testapp.database.UserDao
import com.example.testapp.network.FlashSale
import com.example.testapp.network.ItemDescription
import com.example.testapp.network.Latest
import com.example.testapp.network.NetworkApi
import kotlinx.coroutines.flow.Flow

class AppRepository(private val userDao: UserDao) {

    val allUsers: Flow<List<User>> = userDao.getAllUsers()
    var currentUser: User? = null

    @WorkerThread
    suspend fun insert(user: User) {
        userDao.insert(user)
    }

    @WorkerThread
    suspend fun checkUserExists(email: String): Boolean {
        return userDao.checkUserExists(email)
    }

    @WorkerThread
    suspend fun getUser(email: String): User? {
        return userDao.getUser(email)
    }

    @WorkerThread
    suspend fun deleteUser(user: User) {
        userDao.deleteUser(user.email)
    }

    @WorkerThread
    suspend fun deleteAll(){
        userDao.deleteAll()
    }

    @WorkerThread
    suspend fun updateProfilePicture(user: User) {
        userDao.updateProfilePicture(user.email, user.profilePictureUri)
    }

    @WorkerThread
    suspend fun updateCurrentUser(): User? {
        currentUser?.email?.let {
            currentUser = userDao.getUser(it)
            return currentUser
        }
        return null
    }

    suspend fun getLatest(): Latest {
        return NetworkApi.retrofitService.getLatest()
    }

    suspend fun getFlashSale(): FlashSale {
        return NetworkApi.retrofitService.getFlashSale()
    }

    suspend fun getItemDescription(): ItemDescription {
        return NetworkApi.retrofitService.getItemDescription()
    }


}