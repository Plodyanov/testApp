package com.example.testapp.ui

import android.util.Log
import android.util.Patterns
import androidx.lifecycle.*
import com.example.testapp.database.User
import com.example.testapp.repository.AppRepository
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class UserViewModel(private val repository: AppRepository) : ViewModel() {

    private val _needToNavigate = MutableLiveData<Boolean?>()
    val needToNavigate: LiveData<Boolean?> = _needToNavigate

    val allUsers: LiveData<List<User>> = repository.allUsers.asLiveData()

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    fun createUser(user: User) = viewModelScope.launch {
        _needToNavigate.value = null
        val result = !repository.checkUserExists(user.email)
        if (result) {
            repository.currentUser = user
            repository.insert(user)
        }
        _needToNavigate.value = result
    }

    fun checkUserExist(firstName: String, password: String) = viewModelScope.launch {
        val user = repository.getUser(password)
        if (user != null
            && user.firstName == firstName
            && user.email == password
        ) {
            repository.currentUser = user
            _needToNavigate.value = true
        } else {
            _needToNavigate.value = false
        }
    }

    fun getCurrentUser() {
        _currentUser.value = null
        _currentUser.value = repository.currentUser
    }

    fun updateProfilePicture(user: User) = viewModelScope.launch {
        repository.updateProfilePicture(user)
        updateCurrentUser()
    }

    fun isValidEmail(email: String): Boolean {
        return (email.isNotBlank() && Patterns.EMAIL_ADDRESS.matcher(email).matches())
    }

    fun navigated() {
        _needToNavigate.value = null
    }

    fun logOut() = viewModelScope.launch {
        repository.currentUser?.let {
            repository.deleteUser(it)
        }
        repository.currentUser = null
    }

    fun deleteAll() = viewModelScope.launch {
        repository.deleteAll()
    }

    fun updateCurrentUser() = viewModelScope.launch {
        _currentUser.value = repository.updateCurrentUser()
    }
}

class UserViewModelFactory(private val repository: AppRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(UserViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return UserViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}