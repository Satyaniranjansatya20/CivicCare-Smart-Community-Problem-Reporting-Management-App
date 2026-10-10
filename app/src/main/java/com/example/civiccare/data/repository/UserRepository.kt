package com.example.civiccare.data.repository

import com.example.civiccare.data.database.UserDao
import com.example.civiccare.data.model.User
import java.security.MessageDigest

class UserRepository(private val userDao: UserDao) {

    private fun hashPassword(password: String): String {
        val bytes = MessageDigest.getInstance("SHA-256")
            .digest(password.toByteArray())
        return bytes.joinToString("") { "%02x".format(it) }
    }

    // Returns true if registered, false if the email already exists
    suspend fun register(
        name: String,
        email: String,
        phone: String,
        password: String,
        role: String = "USER"
    ): Boolean {
        if (userDao.getUserByEmail(email) != null) {
            return false
        }
        val user = User(
            name = name,
            email = email,
            phone = phone,
            password = hashPassword(password),
            role = role
        )
        userDao.insertUser(user)
        return true
    }

    // Returns the User if credentials match, otherwise null
    suspend fun login(email: String, password: String): User? {
        return userDao.login(email, hashPassword(password))
    }

    suspend fun getUserById(id: Int): User? {
        return userDao.getUserById(id)
    }

    suspend fun updateUser(user: User) {
        userDao.updateUser(user)
    }

    // Returns true if the password was changed, false if no account has this email
    suspend fun resetPassword(email: String, newPassword: String): Boolean {
        return userDao.updatePassword(email, hashPassword(newPassword)) > 0
    }

    // Google sign-in: returns the existing user, or creates one if the email is new
    suspend fun googleSignIn(name: String, email: String): User? {
        val existing = userDao.getUserByEmail(email)
        if (existing != null) {
            return existing
        }
        val displayName = if (name.isBlank()) email.substringBefore("@") else name
        val user = User(
            name = displayName,
            email = email,
            phone = "",
            password = "GOOGLE_SIGNIN",
            role = "USER"
        )
        userDao.insertUser(user)
        return userDao.getUserByEmail(email)
    }
}