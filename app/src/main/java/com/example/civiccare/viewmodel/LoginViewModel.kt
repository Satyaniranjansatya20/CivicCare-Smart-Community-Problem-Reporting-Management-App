package com.example.civiccare.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.civiccare.data.database.CivicCareDatabase
import com.example.civiccare.data.model.User
import com.example.civiccare.data.repository.UserRepository
import kotlinx.coroutines.launch

sealed class LoginResult {
    data class Success(val user: User) : LoginResult()
    object Failure : LoginResult()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: UserRepository

    init {
        val dao = CivicCareDatabase.getDatabase(application).userDao()
        repository = UserRepository(dao)
    }

    private val _loginResult = MutableLiveData<LoginResult>()
    val loginResult: LiveData<LoginResult> = _loginResult

    private val _registerResult = MutableLiveData<Boolean>()
    val registerResult: LiveData<Boolean> = _registerResult

    private val _currentUser = MutableLiveData<User?>()
    val currentUser: LiveData<User?> = _currentUser

    private val _updateResult = MutableLiveData<Boolean>()
    val updateResult: LiveData<Boolean> = _updateResult

    private val _resetResult = MutableLiveData<Boolean>()
    val resetResult: LiveData<Boolean> = _resetResult

    fun login(email: String, password: String) {
        viewModelScope.launch {
            val user = repository.login(email, password)
            _loginResult.value = if (user != null) {
                LoginResult.Success(user)
            } else {
                LoginResult.Failure
            }
        }
    }

    fun register(name: String, email: String, phone: String, password: String) {
        viewModelScope.launch {
            _registerResult.value = repository.register(name, email, phone, password)
        }
    }

    fun loadUser(id: Int) {
        viewModelScope.launch {
            _currentUser.value = repository.getUserById(id)
        }
    }

    fun updateProfile(user: User) {
        viewModelScope.launch {
            repository.updateUser(user)
            _updateResult.value = true
        }
    }

    fun resetPassword(email: String, newPassword: String) {
        viewModelScope.launch {
            _resetResult.value = repository.resetPassword(email, newPassword)
        }
    }
}