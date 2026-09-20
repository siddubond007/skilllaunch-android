package com.skilllaunch.app.data.model.profile

data class ProfileUser(
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
    val dob: String? = null,
    val profile: ProfileData? = null
)

data class ProfileData(
    val tagline: String? = null,
    val bio: String? = null,
    val college: String? = null,
    val category: String? = null,
    val hourlyRate: Double? = null,
    val avatarUrl: String? = null,
    val coverUrl: String? = null,
    val skills: List<String>? = null,
    val responseTimeExpectation: String? = null
)

data class ProfileUpdateRequest(
    val tagline: String? = null,
    val bio: String? = null,
    val college: String? = null,
    val category: String? = null,
    val hourlyRate: Double? = null,
    val skills: List<String>? = null,
    val responseTimeExpectation: String? = null
)

data class ProfileUpdateResponse(
    val message: String? = null,
    val profile: ProfileData? = null
)

