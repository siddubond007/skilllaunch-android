package com.skilllaunch.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun SignupStageTwo(
    state: AuthUiState,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    username: String,
    usernameSuggestions: List<String>,
    onUsernameChange: (String) -> Unit,
    onRefreshSuggestions: () -> Unit,
    onCreateAccount: () -> Unit,
    onBackToStageOne: () -> Unit
) {
    var localError by rememberSaveable { mutableStateOf("") }
    val visibleError = localError.ifBlank { state.errorMessage.orEmpty() }

    AuthBackground(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .padding(horizontal = 24.dp, vertical = 12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 520.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = onBackToStageOne,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(
                        text = "←  Back to Account Details",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.weight(1f))

                ThemeToggle(
                    darkTheme = darkTheme,
                    onToggleTheme = onToggleTheme
                )
            }

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .widthIn(max = 520.dp)
                        .verticalScroll(rememberScrollState())
                        .padding(vertical = 16.dp)
                ) {
                    Text(
                        text = "Choose Your Username",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Create the unique profile handle people will know you by.",
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AuthField(
                        label = "Username",
                        value = username,
                        onValueChange = {
                            onUsernameChange(
                                it.lowercase()
                                    .filter { char ->
                                        char.isLetterOrDigit() || char == '_'
                                    }
                            )
                            localError = ""
                        },
                        placeholder = "your_username",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Ascii
                        ),
                        leadingIcon = AuthFieldIcon.User
                    )

                    Text(
                        text = "3+ characters • lowercase letters, numbers and underscore",
                        modifier = Modifier.padding(
                            top = 6.dp,
                            start = 3.dp
                        ),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.height(19.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Suggested for you",
                            color = MaterialTheme.colorScheme.onSurface,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold
                        )

                        Spacer(modifier = Modifier.weight(1f))

                        TextButton(
                            onClick = {
                                localError = ""
                                onRefreshSuggestions()
                            },
                            contentPadding = androidx.compose.foundation.layout.PaddingValues(
                                horizontal = 4.dp,
                                vertical = 0.dp
                            )
                        ) {
                            Text(
                                text = "↻ Refresh",
                                color = MaterialTheme.colorScheme.primary,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    if (usernameSuggestions.isEmpty()) {
                        GlassSuggestionCard(
                            title = "Add your name first",
                            subtitle = "Suggestions will appear after you enter your details.",
                            selected = false,
                            onClick = onRefreshSuggestions
                        )
                    } else {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            usernameSuggestions.chunked(2).forEachIndexed { rowIndex, rowItems ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    rowItems.forEachIndexed { columnIndex, suggestion ->
                                        val index = rowIndex * 2 + columnIndex
                                        GlassSuggestionCard(
                                            title = "@$suggestion",
                                            subtitle = if (index < 2) "CLASSIC" else "FANCY",
                                            selected = username == suggestion,
                                            onClick = {
                                                onUsernameChange(suggestion)
                                                localError = ""
                                            },
                                            modifier = Modifier.weight(1f)
                                        )
                                    }

                                    if (rowItems.size == 1) {
                                        Spacer(modifier = Modifier.weight(1f))
                                    }
                                }
                            }
                        }
                    }

                    if (visibleError.isNotBlank()) {
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 14.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.82f)
                        ) {
                            Text(
                                text = visibleError,
                                modifier = Modifier.padding(
                                    horizontal = 13.dp,
                                    vertical = 10.dp
                                ),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(18.dp))

                    AuthPrimaryButton(
                        text = "Create Account",
                        enabled = !state.isLoading,
                        loading = state.isLoading,
                        onClick = {
                            localError = when {
                                username.trim().length < 3 ->
                                    "Please choose a username of at least 3 characters."
                                !username.matches(Regex("[a-z0-9_]+")) ->
                                    "Username can contain only lowercase letters, numbers and underscore."
                                else -> ""
                            }

                            if (localError.isBlank()) {
                                onCreateAccount()
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    Text(
                        text = "You can change this later from your profile settings.",
                        modifier = Modifier.fillMaxWidth(),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.70f),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun GlassSuggestionCard(
    title: String,
    subtitle: String,
    selected: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .height(74.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(
                if (selected) {
                    Brush.horizontalGradient(
                        listOf(
                            androidx.compose.ui.graphics.Color(0xFF5A46E9),
                            androidx.compose.ui.graphics.Color(0xFF8337E7)
                        )
                    )
                } else {
                    Brush.linearGradient(
                        listOf(
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.32f),
                            MaterialTheme.colorScheme.surface.copy(alpha = 0.22f)
                        )
                    )
                }
            )
            .border(
                1.dp,
                if (selected) {
                    MaterialTheme.colorScheme.primary.copy(alpha = 0.65f)
                } else {
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)
                },
                RoundedCornerShape(18.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 10.dp)
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                color = if (selected) {
                    androidx.compose.ui.graphics.Color.White
                } else {
                    MaterialTheme.colorScheme.onSurface
                },
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                maxLines = 1
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = if (selected) "✓ Selected" else subtitle,
                color = if (selected) {
                    androidx.compose.ui.graphics.Color.White.copy(alpha = 0.82f)
                } else if (subtitle == "FANCY") {
                    MaterialTheme.colorScheme.primary
                } else {
                    MaterialTheme.colorScheme.onSurfaceVariant
                },
                fontSize = 10.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
