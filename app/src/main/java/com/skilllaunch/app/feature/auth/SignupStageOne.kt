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
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter
import java.time.format.DateTimeParseException
import java.util.Locale
import androidx.compose.foundation.text.KeyboardOptions

@Composable
fun SignupStageOne(
    state: AuthUiState,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    role: String,
    firstName: String,
    lastName: String,
    email: String,
    dob: String,
    password: String,
    onRoleChange: (String) -> Unit,
    onFirstNameChange: (String) -> Unit,
    onLastNameChange: (String) -> Unit,
    onEmailChange: (String) -> Unit,
    onDobChange: (String) -> Unit,
    onPasswordChange: (String) -> Unit,
    onClearError: () -> Unit,
    onContinue: (String) -> Unit,
    onBackToLogin: () -> Unit
) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var showDobPicker by remember { mutableStateOf(false) }
    var confirmPassword by rememberSaveable { mutableStateOf("") }
    var localError by rememberSaveable { mutableStateOf("") }

    val visibleError = localError.ifBlank { state.errorMessage.orEmpty() }
    val emailError = visibleError.takeIf {
        it.contains("email", ignoreCase = true) ||
            it.contains("account is already", ignoreCase = true)
    }
    val dobError = visibleError.takeIf {
        it.contains("date of birth", ignoreCase = true) ||
            it.contains("dob", ignoreCase = true) ||
            it.contains("age", ignoreCase = true) ||
            it.contains("18", ignoreCase = true) ||
            it.contains("legal capacity", ignoreCase = true)
    }
    val passwordError = visibleError.takeIf {
        it.contains("password", ignoreCase = true)
    }
    val generalError = visibleError
        .takeIf { it.isNotBlank() }
        ?.takeUnless {
            emailError != null || dobError != null || passwordError != null
        }

    if (showDobPicker) {
        val today = LocalDate.now()
        val pickerState = rememberDatePickerState(
            initialSelectedDateMillis = dobToPickerMillis(dob),
            yearRange = 1900..today.year
        )

        DatePickerDialog(
            onDismissRequest = { showDobPicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = pickerState.selectedDateMillis
                        val selectedDate = millis?.let {
                            Instant.ofEpochMilli(it)
                                .atZone(ZoneOffset.UTC)
                                .toLocalDate()
                        }

                        if (selectedDate != null && !selectedDate.isAfter(today)) {
                            onDobChange(selectedDate.format(dobStorageFormatter))
                            showDobPicker = false
                        } else {
                            localError = "Please select a valid date of birth."
                        }
                    }
                ) {
                    Text("Done")
                }
            },
            dismissButton = {
                TextButton(onClick = { showDobPicker = false }) {
                    Text("Cancel")
                }
            }
        ) {
            DatePicker(state = pickerState)
        }
    }

    AuthBackground(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
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
                    onClick = onBackToLogin,
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(0.dp)
                ) {
                    Text(
                        text = "←  Back to Sign In",
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
                        .padding(vertical = 14.dp)
                ) {
                    Text(
                        text = "Join SkillLaunch",
                        color = MaterialTheme.colorScheme.onSurface,
                        fontSize = 32.sp,
                        lineHeight = 36.sp,
                        fontWeight = FontWeight.ExtraBold
                    )

                    Text(
                        text = "Create your free account and start today",
                        modifier = Modifier.padding(top = 5.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.bodyMedium
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "I'm joining as",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        style = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.SemiBold
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    AuthRoleToggle(
                        studentSelected = role == "STUDENT_FREELANCER",
                        onStudentSelected = {
                            onRoleChange("STUDENT_FREELANCER")
                            localError = ""
                        },
                        onClientSelected = {
                            onRoleChange("CLIENT")
                            localError = ""
                        }
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        AuthField(
                            label = "First Name",
                            value = firstName,
                            onValueChange = { value ->
                                onFirstNameChange(
                                    value.filter { it.isLetter() || it.isWhitespace() }
                                )
                            },
                            placeholder = "Alex",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            leadingIcon = AuthFieldIcon.User,
                            modifier = Modifier.weight(1f)
                        )

                        AuthField(
                            label = "Last Name",
                            value = lastName,
                            onValueChange = { value ->
                                onLastNameChange(
                                    value.filter { it.isLetter() || it.isWhitespace() }
                                )
                            },
                            placeholder = "Rivera",
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Text
                            ),
                            leadingIcon = AuthFieldIcon.User,
                            modifier = Modifier.weight(1f)
                        )
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        AuthField(
                            label = "Email Address",
                            value = email,
                            onValueChange = onEmailChange,
                            placeholder = if (role == "CLIENT") {
                                "alex@company.com"
                            } else {
                                "alex@college.edu"
                            },
                            keyboardOptions = KeyboardOptions(
                                keyboardType = KeyboardType.Email
                            ),
                            leadingIcon = AuthFieldIcon.Email
                        )

                        Text(
                            text = if (role == "CLIENT") {
                                "Use your work or business email"
                            } else {
                                "College or personal Gmail accepted"
                            },
                            modifier = Modifier.padding(
                                top = 6.dp,
                                start = 3.dp
                            ),
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                        emailError?.let { message ->
                            SignupFieldError(message)
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    DobField(
                        dob = dob,
                        calculatedAge = calculateAgeFromDob(dob),
                        onClick = {
                            localError = ""
                            onClearError()
                            showDobPicker = true
                        }
                    )

                    Text(
                        text = when {
                            calculateAgeFromDob(dob) != null -> {
                                if (role == "CLIENT") {
                                    "Age " + calculateAgeFromDob(dob) + " • Clients must be 18 or older."
                                } else {
                                    "Age " + calculateAgeFromDob(dob) + " • Used for account eligibility and safety."
                                }
                            }
                            role == "CLIENT" -> {
                                "Select your date of birth. Clients must be 18 or older."
                            }
                            else -> {
                                "Your age will be calculated automatically from your date of birth."
                            }
                        },
                        modifier = Modifier.padding(top = 6.dp, start = 3.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    dobError?.let { message ->
                        SignupFieldError(message)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthField(
                        label = "Password",
                        value = password,
                        onValueChange = onPasswordChange,
                        placeholder = "Create a strong password",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        leadingIcon = AuthFieldIcon.Lock,
                        password = true,
                        passwordVisible = showPassword,
                        onTogglePassword = {
                            showPassword = !showPassword
                        }
                    )

                    PasswordRequirements(password = password)
                    passwordError?.let { message ->
                        SignupFieldError(message)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    AuthField(
                        label = "Confirm Password",
                        value = confirmPassword,
                        onValueChange = {
                            confirmPassword = it
                            localError = ""
                        },
                        placeholder = "Re-enter your password",
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Password
                        ),
                        leadingIcon = AuthFieldIcon.Check,
                        password = true,
                        passwordVisible = showPassword
                    )

                    if (confirmPassword.isNotEmpty()) {
                        Text(
                            text = if (password == confirmPassword) {
                                "✓ Passwords match"
                            } else {
                                "○ Passwords do not match yet"
                            },
                            modifier = Modifier.padding(top = 7.dp, start = 3.dp),
                            color = if (password == confirmPassword) {
                                MaterialTheme.colorScheme.primary
                            } else {
                                MaterialTheme.colorScheme.onSurfaceVariant
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    generalError?.let { message ->
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 12.dp),
                            shape = RoundedCornerShape(14.dp),
                            color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.90f)
                        ) {
                            Text(
                                text = message,
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
                        text = if (role == "CLIENT") {
                            "Join Free as Client"
                        } else {
                            "Join Free as Student"
                        },
                        enabled = !state.isLoading,
                        loading = state.isLoading,
                        onClick = {
                            when {
                                firstName.trim().length < 3 -> {
                                    localError = "First Name must contain at least 3 letters."
                                }
                                lastName.trim().length < 3 -> {
                                    localError = "Last Name must contain at least 3 letters."
                                }
                                email.trim().isBlank() -> {
                                    localError = "Please enter your email address."
                                }
                                dob.isBlank() -> {
                                    localError = "Please select your date of birth."
                                }
                                calculateAgeFromDob(dob) == null -> {
                                    localError = "Please select a valid date of birth."
                                }
                                role == "CLIENT" && calculateAgeFromDob(dob)!! < 18 -> {
                                    localError = "Clients must be 18 or older. You can continue as a Student Freelancer."
                                }
                                password.length < 8 -> {
                                    localError = "Password must contain at least 8 characters."
                                }
                                !hasStrongPassword(password) -> {
                                    localError = "Use 8+ characters with uppercase, lowercase, a number, and a special character."
                                }
                                password != confirmPassword -> {
                                    localError = "Passwords do not match."
                                }
                                else -> {
                                    localError = ""
                                    onContinue(role)
                                }
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(18.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Already have an account?",
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )

                        Spacer(modifier = Modifier.width(5.dp))

                        Text(
                            text = "Sign in",
                            color = MaterialTheme.colorScheme.primary,
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.clickable(onClick = onBackToLogin)
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                }
            }
        }
    }
}


private fun hasStrongPassword(password: String): Boolean {
    return password.length >= 8 &&
        password.any(Char::isUpperCase) &&
        password.any(Char::isLowerCase) &&
        password.any(Char::isDigit) &&
        password.any { !it.isLetterOrDigit() } &&
        password.none(Char::isWhitespace)
}

@Composable
private fun DobField(
    dob: String,
    calculatedAge: Int?,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Text(
            text = "Date of Birth",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .clip(RoundedCornerShape(16.dp))
                .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.45f))
                .border(
                    1.dp,
                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f),
                    RoundedCornerShape(16.dp)
                )
                .clickable(onClick = onClick)
                .padding(horizontal = 17.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AuthFieldIconViewForDob(
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.86f)
            )
            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (dob.isBlank()) {
                        "Select your date of birth"
                    } else {
                        formatDobForDisplay(dob)
                    },
                    color = if (dob.isBlank()) {
                        MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.52f)
                    } else {
                        MaterialTheme.colorScheme.onSurface
                    },
                    style = MaterialTheme.typography.bodyMedium,
                    fontWeight = FontWeight.Medium
                )

                if (calculatedAge != null) {
                    Text(
                        text = "Age $calculatedAge",
                        color = MaterialTheme.colorScheme.primary.copy(alpha = 0.90f),
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Text(
                text = "›",
                color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.72f),
                fontSize = 24.sp
            )
        }
    }
}

@Composable
private fun AuthFieldIconViewForDob(color: androidx.compose.ui.graphics.Color) {
    androidx.compose.foundation.Canvas(modifier = Modifier.size(18.dp)) {
        val strokeWidth = size.minDimension * 0.09f
        val left = size.width * 0.14f
        val top = size.height * 0.23f
        val right = size.width * 0.86f
        val bottom = size.height * 0.82f

        drawRoundRect(
            color = color,
            topLeft = androidx.compose.ui.geometry.Offset(left, top),
            size = androidx.compose.ui.geometry.Size(right - left, bottom - top),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.minDimension * 0.08f),
            style = androidx.compose.ui.graphics.drawscope.Stroke(width = strokeWidth)
        )
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(left, size.height * 0.40f),
            end = androidx.compose.ui.geometry.Offset(right, size.height * 0.40f),
            strokeWidth = strokeWidth
        )
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(size.width * 0.32f, size.height * 0.11f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.32f, size.height * 0.31f),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
        drawLine(
            color = color,
            start = androidx.compose.ui.geometry.Offset(size.width * 0.68f, size.height * 0.11f),
            end = androidx.compose.ui.geometry.Offset(size.width * 0.68f, size.height * 0.31f),
            strokeWidth = strokeWidth,
            cap = androidx.compose.ui.graphics.StrokeCap.Round
        )
    }
}


private val dobStorageFormatter: DateTimeFormatter = DateTimeFormatter.ISO_LOCAL_DATE

private val dobDisplayFormatter: DateTimeFormatter =
    DateTimeFormatter.ofPattern("dd MMM yyyy", Locale.getDefault())

internal fun calculateAgeFromDob(
    dob: String,
    today: LocalDate = LocalDate.now()
): Int? {
    val date = try {
        LocalDate.parse(dob, dobStorageFormatter)
    } catch (_: DateTimeParseException) {
        return null
    }

    if (date.isAfter(today)) return null

    var age = today.year - date.year
    if (
        today.monthValue < date.monthValue ||
        (today.monthValue == date.monthValue && today.dayOfMonth < date.dayOfMonth)
    ) {
        age--
    }

    return age.takeIf { it in 1..120 }
}

private fun formatDobForDisplay(dob: String): String {
    return try {
        LocalDate.parse(dob, dobStorageFormatter).format(dobDisplayFormatter)
    } catch (_: DateTimeParseException) {
        dob
    }
}

private fun dobToPickerMillis(dob: String): Long? {
    return try {
        LocalDate.parse(dob, dobStorageFormatter)
            .atStartOfDay(ZoneOffset.UTC)
            .toInstant()
            .toEpochMilli()
    } catch (_: DateTimeParseException) {
        null
    }
}

@Composable
private fun PasswordRequirements(password: String) {
    val lengthMet = password.length >= 8
    val upperMet = password.any(Char::isUpperCase)
    val lowerMet = password.any(Char::isLowerCase)
    val numberMet = password.any(Char::isDigit)
    val specialMet = password.any { !it.isLetterOrDigit() }
    val spacesMet = password.isNotEmpty() && password.none(Char::isWhitespace)

    Column(
        modifier = Modifier.padding(top = 8.dp, start = 3.dp),
        verticalArrangement = Arrangement.spacedBy(3.dp)
    ) {
        PasswordRequirementLine(lengthMet, "8+ characters")
        PasswordRequirementLine(upperMet, "One uppercase letter")
        PasswordRequirementLine(lowerMet, "One lowercase letter")
        PasswordRequirementLine(numberMet, "One number")
        PasswordRequirementLine(specialMet, "One special character")
        PasswordRequirementLine(spacesMet, "No spaces")
    }
}

@Composable
private fun PasswordRequirementLine(met: Boolean, label: String) {
    Text(
        text = (if (met) "✓ " else "○ ") + label,
        color = if (met) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.78f)
        },
        fontSize = 11.sp,
        fontWeight = if (met) FontWeight.Bold else FontWeight.Medium
    )
}


@Composable
private fun SignupFieldError(message: String) {
    Text(
        text = "⚠ $message",
        modifier = Modifier.padding(
            top = 6.dp,
            start = 3.dp
        ),
        color = MaterialTheme.colorScheme.error,
        style = MaterialTheme.typography.bodySmall,
        fontWeight = FontWeight.SemiBold
    )
}
