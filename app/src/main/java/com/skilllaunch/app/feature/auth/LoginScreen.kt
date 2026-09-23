package com.skilllaunch.app.feature.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LoginScreen(
    state: AuthUiState,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onLogin: (String, String) -> Unit,
    onCreateAccount: () -> Unit
) {
    var email by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var passwordVisible by rememberSaveable { mutableStateOf(false) }

    val background = MaterialTheme.colorScheme.background
    val surface = MaterialTheme.colorScheme.surface
    val muted = MaterialTheme.colorScheme.onSurfaceVariant
    val border = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.08f)
    val inputBackground = if (darkTheme) {
        MaterialTheme.colorScheme.surface.copy(alpha = 0.78f)
    } else {
        Color.White.copy(alpha = 0.72f)
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = background
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .align(Alignment.TopEnd)
                    .offset(x = 80.dp, y = (-80).dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.primary.copy(alpha = if (darkTheme) 0.22f else 0.12f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Box(
                modifier = Modifier
                    .size(210.dp)
                    .align(Alignment.BottomStart)
                    .offset(x = (-80).dp, y = 80.dp)
                    .background(
                        Brush.radialGradient(
                            colors = listOf(
                                MaterialTheme.colorScheme.secondary.copy(alpha = if (darkTheme) 0.16f else 0.09f),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            )

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
                    .imePadding()
                    .padding(horizontal = 22.dp, vertical = 20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End
                ) {
                    Surface(
                        onClick = onToggleTheme,
                        modifier = Modifier.size(40.dp),
                        shape = CircleShape,
                        color = surface.copy(alpha = 0.76f),
                        border = androidx.compose.foundation.BorderStroke(1.dp, border)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = if (darkTheme) "☀" else "☾",
                                color = if (darkTheme) Color(0xFFFBBF24) else MaterialTheme.colorScheme.primary,
                                style = MaterialTheme.typography.titleMedium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(44.dp))

                Surface(
                    modifier = Modifier.size(72.dp),
                    shape = RoundedCornerShape(24.dp),
                    color = MaterialTheme.colorScheme.primary,
                    shadowElevation = if (darkTheme) 14.dp else 8.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "↗",
                            color = MaterialTheme.colorScheme.onPrimary,
                            fontSize = 30.sp,
                            fontWeight = FontWeight.Black
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Text(
                    text = "SkillLaunch",
                    color = MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.ExtraBold
                )

                Text(
                    text = "Where Student Talent Meets Real Work",
                    modifier = Modifier.padding(top = 5.dp),
                    color = muted,
                    style = MaterialTheme.typography.labelSmall,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(42.dp))

                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.Start
                ) {
                    Text(
                        text = "Welcome back",
                        color = MaterialTheme.colorScheme.onSurface,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )

                    Text(
                        text = "Sign in to continue your SkillLaunch journey.",
                        modifier = Modifier.padding(top = 5.dp),
                        color = muted,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    AuthInput(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = "Email or username",
                        leadingText = "@",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        containerColor = inputBackground,
                        borderColor = border
                    )

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthInput(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = "Password",
                        leadingText = "•",
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        visualTransformation = if (passwordVisible) {
                            VisualTransformation.None
                        } else {
                            PasswordVisualTransformation()
                        },
                        trailingContent = {
                            TextButton(
                                onClick = { passwordVisible = !passwordVisible },
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp)
                            ) {
                                Text(
                                    text = if (passwordVisible) "Hide" else "Show",
                                    style = MaterialTheme.typography.labelSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        },
                        containerColor = inputBackground,
                        borderColor = border
                    )

                    state.errorMessage?.takeIf { it.isNotBlank() }?.let { message ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.82f)
                        ) {
                            Text(
                                text = message,
                                modifier = Modifier.padding(
                                    horizontal = 13.dp,
                                    vertical = 11.dp
                                ),
                                color = MaterialTheme.colorScheme.onErrorContainer,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(22.dp))

                    Button(
                        onClick = { onLogin(email, password) },
                        enabled = !state.isLoading,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp),
                        shape = RoundedCornerShape(50.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            disabledContainerColor = MaterialTheme.colorScheme.primary.copy(alpha = 0.5f)
                        )
                    ) {
                        if (state.isLoading) {
                            CircularProgressIndicator(
                                modifier = Modifier.size(21.dp),
                                strokeWidth = 2.dp,
                                color = MaterialTheme.colorScheme.onPrimary
                            )
                        } else {
                            Text(
                                text = "Sign In",
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "New to SkillLaunch?",
                    color = muted,
                    style = MaterialTheme.typography.bodySmall
                )

                TextButton(
                    onClick = onCreateAccount,
                    modifier = Modifier.height(42.dp)
                ) {
                    Text(
                        text = "Create your account",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                Text(
                    text = "Student-first talent marketplace",
                    color = muted.copy(alpha = 0.68f),
                    style = MaterialTheme.typography.labelSmall
                )

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
private fun AuthInput(
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String,
    leadingText: String,
    keyboardOptions: KeyboardOptions,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingContent: @Composable (() -> Unit)? = null,
    containerColor: Color,
    borderColor: Color
) {
    BasicTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        singleLine = true,
        keyboardOptions = keyboardOptions,
        visualTransformation = visualTransformation,
        textStyle = TextStyle(
            color = MaterialTheme.colorScheme.onSurface,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium
        ),
        decorationBox = { innerTextField ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        color = containerColor,
                        shape = RoundedCornerShape(17.dp)
                    )
                    .border(
                        width = 1.dp,
                        color = borderColor,
                        shape = RoundedCornerShape(17.dp)
                    )
                    .padding(horizontal = 14.dp, vertical = 13.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = RoundedCornerShape(11.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.10f)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = leadingText,
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.size(10.dp))

                Box(modifier = Modifier.weight(1f)) {
                    if (value.isEmpty()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.75f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    innerTextField()
                }

                trailingContent?.invoke()
            }
        }
    )
}
