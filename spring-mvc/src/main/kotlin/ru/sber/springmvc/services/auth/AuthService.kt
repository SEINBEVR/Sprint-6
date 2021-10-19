package ru.sber.springmvc.services.auth

import org.springframework.stereotype.Service

@Service
class AuthService {
    val username = "admin"
    val passwords = "admin"

    fun getAuthParams(sessionUsername: String?, sessionPassword: String?): Boolean {
        return sessionUsername == username && sessionPassword == passwords
    }
}