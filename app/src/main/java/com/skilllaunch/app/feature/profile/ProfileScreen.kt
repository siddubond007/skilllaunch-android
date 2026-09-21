package com.skilllaunch.app.feature.profile

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.consumeWindowInsets
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarDuration
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
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
    val snackbarHostState = remember { SnackbarHostState() }
    var moreMenuExpanded by remember { mutableStateOf(false) }

    LaunchedEffect(user.id) {
        user.id?.let(profileViewModel::loadProfile)
    }

    LaunchedEffect(uiState.successMessage) {
        val message = uiState.successMessage
        if (!message.isNullOrBlank()) {
            snackbarHostState.showSnackbar(
                message = message,
                actionLabel = "Dismiss",
                duration = SnackbarDuration.Short
            )
            profileViewModel.clearMessages()
        }
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Scaffold(
            containerColor = MaterialTheme.colorScheme.background,
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            topBar = {
                TopAppBar(
                    title = { Text("Profile") },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        scrolledContainerColor = MaterialTheme.colorScheme.background
                    ),
                    actions = {
                        TextButton(
                            onClick = profileViewModel::saveProfile,
                            enabled = !uiState.isLoading && !uiState.isSaving
                        ) {
                            Text("Save")
                        }

                        androidx.compose.foundation.layout.Box {
                            TextButton(onClick = { moreMenuExpanded = true }) {
                                Text("More")
                            }

                            DropdownMenu(
                                expanded = moreMenuExpanded,
                                onDismissRequest = { moreMenuExpanded = false }
                            ) {
                                DropdownMenuItem(
                                    text = { Text("Logout") },
                                    onClick = {
                                        moreMenuExpanded = false
                                        onLogout()
                                    }
                                )
                            }
                        }
                    }
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
                    .consumeWindowInsets(innerPadding)
                    .imePadding()
            ) {
                if (uiState.isLoading) {
                    Column(
                        modifier = Modifier.fillMaxSize(),
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
                        onClearMessages = profileViewModel::clearMessages,
                        modifier = Modifier.fillMaxSize()
                    )
                }
            }
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
    onClearMessages: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 14.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 2.dp)
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

        Text(
            text = "Profile information",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(horizontal = 2.dp)
        )

        OutlinedProfileField(
            value = uiState.form.tagline,
            onValueChange = onTaglineChange,
            label = "⭐ Tagline",
            singleLine = true
        )

        OutlinedProfileField(
            value = uiState.form.bio,
            onValueChange = onBioChange,
            label = "📝 Bio",
            minLines = 4
        )

        OutlinedProfileField(
            value = uiState.form.college,
            onValueChange = onCollegeChange,
            label = "🎓 College",
            singleLine = true
        )

        OutlinedProfileField(
            value = uiState.form.category,
            onValueChange = onCategoryChange,
            label = "📂 Category",
            singleLine = true
        )

        OutlinedProfileField(
            value = uiState.form.hourlyRate,
            onValueChange = onHourlyRateChange,
            label = "💰 Hourly rate",
            singleLine = true
        )

        OutlinedProfileField(
            value = uiState.form.skills,
            onValueChange = onSkillsChange,
            label = "🛠 Skills",
            supportingText = "Example: Kotlin, React, UI Design"
        )

        OutlinedProfileField(
            value = uiState.form.responseTimeExpectation,
            onValueChange = onResponseTimeChange,
            label = "⏱ Response time expectation",
            singleLine = true
        )

        uiState.errorMessage?.let { message ->
            Text(
                text = message,
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.padding(horizontal = 2.dp)
            )

            TextButton(onClick = onClearMessages) {
                Text("Dismiss")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
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
    androidx.compose.material3.OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 2.dp),
        label = { Text(label) },
        singleLine = singleLine,
        minLines = minLines,
        supportingText = supportingText?.let { text ->
            {
                Text(
                    text = text,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    modifier = Modifier.padding(
                        top = 2.dp,
                        bottom = 12.dp
                    )
                )
            }
        }
    )
}
