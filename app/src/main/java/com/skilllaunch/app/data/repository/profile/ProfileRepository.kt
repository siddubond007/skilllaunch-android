package com.skilllaunch.app.data.repository.profile

import com.skilllaunch.app.data.api.UserApi
import com.skilllaunch.app.data.model.profile.ProfileUpdateRequest
import com.skilllaunch.app.data.model.profile.ProfileUpdateResponse
import com.skilllaunch.app.data.model.profile.ProfileUser

class ProfileRepository(
    private val userApi: UserApi
) {

    suspend fun getProfile(userId: String): Result<ProfileUser> {
        return runCatching {
            userApi.getUserProfile(userId)
        }.recoverCatching { error ->
            throw Exception(
                error.message ?: "Unable to load your profile right now"
            )
        }
    }

    suspend fun updateProfile(
        request: ProfileUpdateRequest
    ): Result<ProfileUpdateResponse> {
        return runCatching {
            userApi.updateProfile(request)
        }.recoverCatching { error ->
            throw Exception(
                error.message ?: "Unable to save your profile right now"
            )
        }
    }
}
