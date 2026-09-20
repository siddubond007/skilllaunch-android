package com.skilllaunch.app.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.relocation.BringIntoViewRequester
import androidx.compose.foundation.relocation.bringIntoViewRequester
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.withFrameNanos
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skilllaunch.app.data.model.auth.AuthUser
import com.skilllaunch.app.data.repository.profile.ProfileRepository

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    user: AuthUser,
    repository: ProfileRepository,
    onLogout: () -> Unit
) {
    val factory = remember(repository) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>
            ): T {
                return ProfileViewModel(repository) as T
            }
        }
    }

    val profileViewModel: ProfileViewModel = viewModel(factory = factory)
    val uiState by profileViewModel.uiState.collectAsState()

    LaunchedEffect(user.id) {
        user.id?.let(profileViewModel::loadProfile)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Profile") },
                actions = {
                    TextButton(onClick = onLogout) {
                        Text("Logout")
                    }
                }
            )
        }
    ) { innerPadding ->
        if (uiState.isLoading) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                CircularProgressIndicator()
                Spacer(modifier = Modifier.height(12.dp))
                Text("Loading profile…")
            }
        } else {
            ProfileContent(
                user = user,
                uiState = uiState,
                onTaglineChange = profileViewModel::updateTagline,
                onBioChange = profileViewModel::updateBio,
                onCollegeChange = profileViewModel::updateCollege,
                onCategoryChange = profileViewModel::updateCategory,
                onHourlyRateChange = profileViewModel::updateHourlyRate,
                onSkillsChange = profileViewModel::updateSkills,
                onResponseTimeChange = profileViewModel::updateResponseTimeExpectation,
                onSave = profileViewModel::saveProfile,
                onClearMessages = profileViewModel::clearMessages,
                modifier = Modifier.padding(innerPadding)
            )
        }
    }
}

@Composable
private fun ProfileContent(
    user: AuthUser,
    uiState: ProfileUiState,
    onTaglineChange: (String) -> Unit,
    onBioChange: (String) -> Unit,
    onCollegeChange: (String) -> Unit,
    onCategoryChange: (String) -> Unit,
    onHourlyRateChange: (String) -> Unit,
    onSkillsChange: (String) -> Unit,
    onResponseTimeChange: (String) -> Unit,
    onSave: () -> Unit,
    onClearMessages: () -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        state = rememberLazyListState(),
        modifier = modifier
            .fillMaxSize()
            .imePadding(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Card(
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(18.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "○",
                        style = MaterialTheme.typography.displaySmall,
                        modifier = Modifier.size(64.dp)
                    )

                    Spacer(modifier = Modifier.width(14.dp))

                    Column(
                        modifier = Modifier.weight(1f)
                    ) {
                        val displayName = user.fullName
                            ?: listOfNotNull(
                                user.firstName,
                                user.middleName,
                                user.lastName
                            ).joinToString(" ").ifBlank { "SkillLaunch User" }

                        Text(
                            text = displayName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold
                        )

                        user.username?.takeIf { it.isNotBlank() }?.let {
                            Text("@$it", style = MaterialTheme.typography.bodyMedium)
                        }

                        user.email?.takeIf { it.isNotBlank() }?.let {
                            Text(it, style = MaterialTheme.typography.bodyMedium)
                        }

                        user.role?.takeIf { it.isNotBlank() }?.let {
                            Text(it.replace("_", " "), style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
        }

        item {
            Text(
                text = "Profile information",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.tagline,
                onValueChange = onTaglineChange,
                label = "Tagline",
                singleLine = true
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.bio,
                onValueChange = onBioChange,
                label = "Bio",
                minLines = 4
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.college,
                onValueChange = onCollegeChange,
                label = "College",
                singleLine = true
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.category,
                onValueChange = onCategoryChange,
                label = "Category",
                singleLine = true
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.hourlyRate,
                onValueChange = onHourlyRateChange,
                label = "Hourly rate",
                singleLine = true
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.skills,
                onValueChange = onSkillsChange,
                label = "Skills",
                supportingText = "Example: Kotlin, React, UI Design"
            )
        }

        item {
            OutlinedProfileField(
                value = uiState.form.responseTimeExpectation,
                onValueChange = onResponseTimeChange,
                label = "Response time expectation",
                singleLine = true
            )
        }

        uiState.errorMessage?.let { message ->
            item {
                Text(message, color = MaterialTheme.colorScheme.error)
                TextButton(onClick = onClearMessages) {
                    Text("Dismiss")
                }
            }
        }

        uiState.successMessage?.let { message ->
            item {
                Text(message, color = MaterialTheme.colorScheme.primary)
                TextButton(onClick = onClearMessages) {
                    Text("Dismiss")
                }
            }
        }

        item {
            Button(
                onClick = onSave,
                modifier = Modifier.fillMaxWidth(),
                enabled = !uiState.isSaving
            ) {
                if (uiState.isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        strokeWidth = 2.dp
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Saving…")
                } else {
                    Text("Save profile")
                }
            }
        }

        if (uiState.isSaving) {
            item {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun OutlinedProfileField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    singleLine: Boolean = false,
    minLines: Int = 1,
    supportingText: String? = null
) {
    val requester = remember { BringIntoViewRequester() }
    var focused by remember { mutableStateOf(false) }
    val imeVisible = WindowInsets.isImeVisible

    LaunchedEffect(focused, imeVisible) {
        if (focused && imeVisible) {
            withFrameNanos { }
            withFrameNanos { }
            requester.bringIntoView()
        }
    }

    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .bringIntoViewRequester(requester)
            .onFocusChanged {
                focused = it.isFocused
            },
        label = { Text(label) },
        singleLine = singleLine,
        minLines = minLines,
        supportingText = supportingText?.let { text -> { Text(text) } }
    )
}
