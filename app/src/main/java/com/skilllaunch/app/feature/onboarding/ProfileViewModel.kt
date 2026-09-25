package com.skilllaunch.app.feature.onboarding

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.skilllaunch.app.data.repository.profile.ProfileRepository
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val repository: ProfileRepository
) : ViewModel() {

    var isResumeUploading = false
        private set

    var uploadedResumeFileName: String = ""
        private set

    var resumeUploadError: String? = null
        private set

    fun uploadResumeToBackend(
        uri: Uri,
        context: Context
    ) {
        if (isResumeUploading) return

        viewModelScope.launch {
            isResumeUploading = true
            resumeUploadError = null

            /*
             * ProfileRepository.uploadResume(...) is the Retrofit path today.
             * It creates MultipartBody.Part and calls UploadApi.uploadResume().
             * A Firebase Storage implementation can replace this call later.
             */
            repository.uploadResume(context, uri)
                .onSuccess { response ->
                    uploadedResumeFileName = response.fileName
                        .orEmpty()
                        .ifBlank { "Resume uploaded" }
                    isResumeUploading = false
                }
                .onFailure { exception ->
                    isResumeUploading = false
                    resumeUploadError = exception.message
                        ?: "Unable to upload your resume."
                }
        }
    }

    fun clearResumeUploadError() {
        resumeUploadError = null
    }

    companion object {
        fun factory(
            repository: ProfileRepository
        ): ViewModelProvider.Factory = object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : ViewModel> create(
                modelClass: Class<T>
            ): T {
                if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
                    return ProfileViewModel(repository) as T
                }
                throw IllegalArgumentException(
                    "Unknown ViewModel class: ${modelClass.name}"
                )
            }
        }
    }
}
