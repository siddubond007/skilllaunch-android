package com.skilllaunch.app.data.repository.auth

import com.skilllaunch.app.core.session.SessionStore
import com.skilllaunch.app.data.api.AuthApi
import com.skilllaunch.app.data.model.auth.AuthUser
import com.skilllaunch.app.data.model.auth.LoginRequest

class AuthRepository(
    private val authApi: AuthApi,
    private val sessionStore: SessionStore
) {

    suspend fun login(
        email: String,
        password: String
    ): Result<AuthUser> {
        return try {
            val response = authApi.login(
                LoginRequest(
                    email = email.trim(),
                    password = password
                )
            )

            val token = response.token
                ?: return Result.failure(
                    IllegalStateException("Authentication token was not returned.")
                )

            val user = response.user
                ?: return Result.failure(
                    IllegalStateException("Authenticated user was not returned.")
                )

            sessionStore.saveAccessToken(token)
            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun getCurrentUser(): Result<AuthUser> {
        return try {
            val user = authApi.getMe().user
                ?: return Result.failure(
                    IllegalStateException("Current user was not returned.")
                )

            Result.success(user)
        } catch (exception: Exception) {
            Result.failure(exception)
        }
    }

    suspend fun logout() {
        sessionStore.clearSession()
    }
}