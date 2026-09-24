package com.skilllaunch.app.data.repository.profile

import com.skilllaunch.app.data.api.UserApi
import com.skilllaunch.app.data.model.profile.ProfileUpdateRequest
import com.skilllaunch.app.data.model.profile.ProfileUpdateResponse
import com.skilllaunch.app.data.model.profile.ProfileUser
import android.content.Context
import android.net.Uri
import android.provider.OpenableColumns
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody

class ProfileRepository(
    private val userApi: UserApi,
    private val uploadApi: com.skilllaunch.app.data.api.UploadApi
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

    suspend fun getMyProfile(): Result<ProfileUser> {
        return runCatching {
            userApi.getMyProfile()
        }.recoverCatching { error ->
            throw Exception(
                error.message ?: "Unable to load your profile right now"
            )
        }
    }



    suspend fun uploadResume(context: Context, uri: Uri): Result<com.skilllaunch.app.data.model.upload.ResumeUploadResponse> {
        return runCatching {
            val resolver = context.contentResolver
            val fileName = resolver.query(
                uri,
                arrayOf(OpenableColumns.DISPLAY_NAME),
                null,
                null,
                null
            )?.use { cursor ->
                if (cursor.moveToFirst()) {
                    cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                } else {
                    null
                }
            } ?: "resume.pdf"

            val extension = fileName.substringAfterLast('.', "").lowercase()
            require(extension in setOf("pdf", "doc", "docx")) {
                "Only PDF, DOC, or DOCX resume files are supported."
            }

            val bytes = resolver.openInputStream(uri)?.use { it.readBytes() }
                ?: throw IllegalStateException("Unable to read the selected resume.")

            require(bytes.size <= 10 * 1024 * 1024) {
                "Resume must be 10 MB or smaller."
            }

            val mimeType = resolver.getType(uri)
                ?: when (extension) {
                    "pdf" -> "application/pdf"
                    "doc" -> "application/msword"
                    "docx" -> "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                    else -> "application/octet-stream"
                }

            val body = bytes.toRequestBody(mimeType.toMediaTypeOrNull())
            val part = MultipartBody.Part.createFormData(
                "file",
                fileName,
                body
            )

            uploadApi.uploadResume(part)
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
