package com.skilllaunch.app.data.model.auth

data class LoginRequest(
    val email: String,
    val password: String
)

data class LoginResponse(
    val message: String? = null,
    val token: String? = null,
    val user: AuthUser? = null
)

data class MeResponse(
    val user: AuthUser? = null
)

data class AuthUser(
    val id: String? = null,
    val username: String? = null,
    val email: String? = null,
    val firstName: String? = null,
    val middleName: String? = null,
    val lastName: String? = null,
    val fullName: String? = null,
    val role: String? = null,
    val isMinor: Boolean? = null,
    val age: Int? = null,
    val dob: String? = null
)