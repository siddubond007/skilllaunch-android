package com.skilllaunch.app.feature.auth

import androidx.activity.compose.BackHandler

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import java.time.LocalDate
import java.time.Period
import java.util.Locale
import kotlin.random.Random

@Composable
fun SignupScreen(
    state: AuthUiState,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onSignup: (String, String?, String, String, String, String, String, Int) -> Unit,
    onBackToLogin: () -> Unit
) {
    var step by rememberSaveable { mutableIntStateOf(1) }
    var firstName by rememberSaveable { mutableStateOf("") }
    var middleName by rememberSaveable { mutableStateOf("") }
    var lastName by rememberSaveable { mutableStateOf("") }
    var email by rememberSaveable { mutableStateOf("") }
    var dobIso by rememberSaveable { mutableStateOf("") }
    var password by rememberSaveable { mutableStateOf("") }
    var username by rememberSaveable { mutableStateOf("") }
    var role by rememberSaveable { mutableStateOf("STUDENT_FREELANCER") }
    var parentConsent by rememberSaveable { mutableStateOf(false) }
    var showPassword by rememberSaveable { mutableStateOf(false) }
    var passwordFocused by rememberSaveable { mutableStateOf(false) }
    var localError by rememberSaveable { mutableStateOf("") }

    BackHandler {
        localError = ""
        if (step > 1) {
            step -= 1
        } else {
            onBackToLogin()
        }
    }
    var usernameSuggestions by rememberSaveable { mutableStateOf(emptyList<String>()) }

    val context = LocalContext.current
    val dob = runCatching { LocalDate.parse(dobIso) }.getOrNull()
    val today = LocalDate.now()
    val calculatedAge = dob?.let { Period.between(it, today).years } ?: 0
    val isBlockedAge = dob != null && calculatedAge < 16
    val isMinor = dob != null && calculatedAge in 16..17

    val passwordCriteria = listOf(
        "8+ characters" to (password.length >= 8),
        "Uppercase (A-Z)" to password.any(Char::isUpperCase),
        "Lowercase (a-z)" to password.any(Char::isLowerCase),
        "Number (0-9)" to password.any(Char::isDigit),
        "Special symbol" to password.any { !it.isLetterOrDigit() }
    )

    val visibleError = localError.ifBlank { state.errorMessage.orEmpty() }
    val stepOneListState = rememberLazyListState()

    LaunchedEffect(passwordFocused, password) {
        if (passwordFocused) {
            kotlinx.coroutines.delay(120)
            stepOneListState.animateScrollToItem(
                index = 4,
                scrollOffset = -24
            )
        }
    }

    fun generateUsernameSuggestions() {
        val first = firstName.lowercase(Locale.US).filter { it.isLetterOrDigit() }
        val last = lastName.lowercase(Locale.US).filter { it.isLetterOrDigit() }
        val base = first.ifBlank { "user" }.take(10)
        val family = last.take(10)

        val aesthetics = listOf(
            "nova", "luna", "astral", "velvet", "neon",
            "pixel", "cosmic", "dream", "prism", "echo",
            "orbit", "ember", "aurora", "zenith", "midnight",
            "starlit", "phoenix", "glow", "vivid", "solstice"
        ).shuffled()

        val vibes = listOf(
            "verse", "wave", "spark", "bloom", "drift",
            "pulse", "realm", "frame", "byte", "cloud",
            "star", "studio", "lab", "quest", "core"
        ).shuffled()

        val themes = listOf(
            "gojo", "kakashi", "itachi", "levi", "luffy",
            "zoro", "tanjiro", "goku", "batman", "stark",
            "neo", "phantom", "shadow", "cyber", "vader",
            "thor", "wolverine", "ragnar"
        ).shuffled()

        val classic = listOf(
            if (family.isNotBlank()) "${base}_${family}" else "${base}_${vibes[0]}",
            "${base}_${vibes[1]}",
            "${base}_studio",
            "${base}_creative"
        ).distinct().shuffled().take(2)

        val fancy = listOf(
            "${base}_${aesthetics[0]}",
            "${aesthetics[1]}_${base}",
            "${themes[0]}_${base.take(6)}",
            "${aesthetics[2]}_${base}",
            "${base}_${aesthetics[3]}"
        ).distinct().shuffled().take(2)

        usernameSuggestions = (classic + fancy).distinct().take(4)
    }

    fun openDobPicker() {
        val initial = dob ?: today.minusYears(18)

        DatePickerDialog(
            context,
            { _, year, month, dayOfMonth ->
                dobIso = "%04d-%02d-%02d".format(
                    Locale.US,
                    year,
                    month + 1,
                    dayOfMonth
                )
                localError = ""
            },
            initial.year,
            initial.monthValue - 1,
            initial.dayOfMonth
        ).apply {
            datePicker.maxDate = System.currentTimeMillis()
        }.show()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        if (step == 1) {
            SignupStageOne(
                state = state,
                darkTheme = darkTheme,
                onToggleTheme = onToggleTheme,
                role = role,
                firstName = firstName,
                lastName = lastName,
                email = email,
                password = password,
                onRoleChange = { role = it },
                onFirstNameChange = { firstName = it },
                onLastNameChange = { lastName = it },
                onEmailChange = { email = it },
                onPasswordChange = { password = it },
                onContinue = { selectedRole ->
                    localError = ""
                    generateUsernameSuggestions()
                    if (username.isBlank() && usernameSuggestions.isNotEmpty()) {
                        username = usernameSuggestions.first()
                    }
                    role = selectedRole
                    step = 2
                },
                onBackToLogin = onBackToLogin
            )
        } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding()
                .padding(horizontal = 20.dp, vertical = 14.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextButton(
                    onClick = {
                        localError = ""
                        if (step > 1) {
                            step -= 1
                        } else {
                            onBackToLogin()
                        }
                    }
                ) {
                    Text(if (step > 1) "\u2190  Back" else "\u2190  Back to Login")
                }

                Spacer(Modifier.weight(1f))

                Text(
                    "Step $step of 3",
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }

            Text(
                when (step) {
                    1 -> "Create Your Free Account"
                    2 -> "Choose Your Username"
                    else -> "How will you use SkillLaunch?"
                },
                style = MaterialTheme.typography.headlineSmall,
                fontWeight = FontWeight.Bold
            )

            Spacer(Modifier.height(4.dp))

            Text(
                when (step) {
                    1 -> "Tell us a little about yourself and secure your account."
                    2 -> "Create the unique profile handle people will know you by."
                    else -> "Choose the experience that fits you best."
                },
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            SignupStepIndicator(step)

            if (visibleError.isNotBlank()) {
                Spacer(Modifier.height(12.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.errorContainer
                ) {
                    Text(
                        visibleError,
                        modifier = Modifier.padding(14.dp),
                        color = MaterialTheme.colorScheme.onErrorContainer,
                        style = MaterialTheme.typography.bodySmall,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(Modifier.height(8.dp))

            AnimatedContent(
                targetState = step,
                modifier = Modifier.weight(1f),
                label = "signup_step"
            ) { currentStep ->
                when (currentStep) {
                    1 -> {
                        LazyColumn(
                            state = stepOneListState,
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                                    OutlinedTextField(
                                        value = firstName,
                                        onValueChange = {
                                            firstName = it.filter { c ->
                                                c.isLetter() || c.isWhitespace()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        label = { Text("First name") },
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = lastName,
                                        onValueChange = {
                                            lastName = it.filter { c ->
                                                c.isLetter() || c.isWhitespace()
                                            }
                                        },
                                        modifier = Modifier.weight(1f),
                                        label = { Text("Last name") },
                                        singleLine = true
                                    )
                                }
                            }

                            item {
                                OutlinedTextField(
                                    value = middleName,
                                    onValueChange = {
                                        middleName = it.filter { c ->
                                            c.isLetter() || c.isWhitespace()
                                        }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Middle name (optional)") },
                                    singleLine = true
                                )
                            }

                            item {
                                OutlinedTextField(
                                    value = email,
                                    onValueChange = { email = it },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Email address") },
                                    singleLine = true,
                                    keyboardOptions = KeyboardOptions(
                                        keyboardType = KeyboardType.Email
                                    )
                                )
                            }

                            item {
                                Card(
                                    modifier = Modifier.fillMaxWidth(),
                                    colors = CardDefaults.cardColors(
                                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                                    )
                                ) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            "Date of Birth",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        Button(
                                            onClick = ::openDobPicker,
                                            modifier = Modifier.fillMaxWidth()
                                        ) {
                                            Text(
                                                dob?.let {
                                                    "%02d / %02d / %04d".format(
                                                        it.dayOfMonth,
                                                        it.monthValue,
                                                        it.year
                                                    )
                                                } ?: "Select your date of birth"
                                            )
                                        }

                                        if (dob != null) {
                                            Text(
                                                "Age: $calculatedAge years",
                                                fontWeight = FontWeight.Bold,
                                                color = when {
                                                    isBlockedAge -> MaterialTheme.colorScheme.error
                                                    isMinor -> Color(0xFFF59E0B)
                                                    else -> Color(0xFF10B981)
                                                }
                                            )
                                        }

                                        if (isBlockedAge) {
                                            Text(
                                                "You must be at least 16 years old to join SkillLaunch.",
                                                color = MaterialTheme.colorScheme.error,
                                                style = MaterialTheme.typography.bodySmall,
                                                fontWeight = FontWeight.SemiBold
                                            )
                                        }

                                        if (isMinor) {
                                            Row(
                                                verticalAlignment = Alignment.Top,
                                                modifier = Modifier.fillMaxWidth()
                                            ) {
                                                Checkbox(
                                                    checked = parentConsent,
                                                    onCheckedChange = {
                                                        parentConsent = it
                                                    }
                                                )

                                                Text(
                                                    "I confirm I have parental or guardian consent to work and receive payments through a parent-linked account.",
                                                    modifier = Modifier.padding(top = 12.dp),
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Card(modifier = Modifier.fillMaxWidth()) {
                                    Column(
                                        modifier = Modifier.padding(16.dp),
                                        verticalArrangement = Arrangement.spacedBy(10.dp)
                                    ) {
                                        Text(
                                            "Create Password",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.Bold
                                        )

                                        OutlinedTextField(
                                            value = password,
                                            onValueChange = { password = it },
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .onFocusChanged {
                                                    passwordFocused = it.isFocused
                                                },
                                            label = { Text("Password") },
                                            singleLine = true,
                                            visualTransformation = if (showPassword) {
                                                VisualTransformation.None
                                            } else {
                                                PasswordVisualTransformation()
                                            },
                                            keyboardOptions = KeyboardOptions(
                                                keyboardType = KeyboardType.Password
                                            )
                                        )

                                        TextButton(
                                            onClick = {
                                                showPassword = !showPassword
                                            }
                                        ) {
                                            Text(
                                                if (showPassword) "Hide password"
                                                else "Show password"
                                            )
                                        }

                                        passwordCriteria.forEach { (label, valid) ->
                                            Row(
                                                verticalAlignment = Alignment.CenterVertically,
                                                horizontalArrangement = Arrangement.spacedBy(8.dp)
                                            ) {
                                                Text(
                                                    if (valid) "\u2713" else "\u25CB",
                                                    color = if (valid) {
                                                        Color(0xFF10B981)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                    },
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontWeight = FontWeight.Bold
                                                )

                                                Text(
                                                    label,
                                                    color = if (valid) {
                                                        Color(0xFF10B981)
                                                    } else {
                                                        MaterialTheme.colorScheme.onSurfaceVariant
                                                    },
                                                    style = MaterialTheme.typography.bodySmall
                                                )
                                            }
                                        }
                                    }
                                }
                            }

                            item {
                                Button(
                                    onClick = {
                                        localError = when {
                                            firstName.trim().length < 3 ->
                                                "First name must contain at least 3 letters."
                                            lastName.trim().length < 3 ->
                                                "Last name must contain at least 3 letters."
                                            email.isBlank() ||
                                                !android.util.Patterns.EMAIL_ADDRESS
                                                    .matcher(email)
                                                    .matches() ->
                                                "Enter a valid email address."
                                            dob == null ->
                                                "Please select your date of birth."
                                            isBlockedAge ->
                                                "You must be at least 16 years old to register."
                                            isMinor && !parentConsent ->
                                                "Please confirm parental or guardian consent."
                                            !passwordCriteria.all { it.second } ->
                                                "Please satisfy all 5 password security requirements."
                                            else -> ""
                                        }

                                        if (localError.isBlank()) {
                                            generateUsernameSuggestions()
                                            step = 2
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp),
                                    enabled = !state.isLoading && !isBlockedAge
                                ) {
                                    Text(
                                        "Continue to Step 2",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }

                            item {
                                TextButton(
                                    onClick = onBackToLogin,
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Already have an account? Sign in")
                                }
                            }
                        }
                    }

                    2 -> {
                        LazyColumn(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            item {
                                OutlinedTextField(
                                    value = username,
                                    onValueChange = {
                                        username = it
                                            .lowercase(Locale.US)
                                            .filter { c ->
                                                c.isLetterOrDigit() || c == '_'
                                            }
                                    },
                                    modifier = Modifier.fillMaxWidth(),
                                    label = { Text("Username") },
                                    placeholder = {
                                        Text("your_unique_username")
                                    },
                                    singleLine = true
                                )

                                Spacer(Modifier.height(2.dp))

                                Text(
                                    "Minimum 3 characters. Lowercase letters, numbers and underscore are allowed.",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )

                                TextButton(
                                    onClick = ::generateUsernameSuggestions
                                ) {
                                    Text("Refresh Suggestions")
                                }

                                Text(
                                    "Choose a suggestion",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            itemsIndexed(usernameSuggestions) { index, suggestion ->
                                val fancy = index >= 2

                                Card(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clickable {
                                            username = suggestion
                                            localError = ""
                                        },
                                    colors = CardDefaults.cardColors(
                                        containerColor = if (username == suggestion) {
                                            MaterialTheme.colorScheme.primaryContainer
                                        } else {
                                            MaterialTheme.colorScheme.surface
                                        }
                                    )
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(16.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            if (fancy) "FANCY" else "CLASSIC",
                                            style = MaterialTheme.typography.labelSmall,
                                            fontWeight = FontWeight.Bold,
                                            color = if (fancy) {
                                                Color(0xFF8B5CF6)
                                            } else {
                                                MaterialTheme.colorScheme.primary
                                            }
                                        )

                                        Spacer(Modifier.width(12.dp))

                                        Text(
                                            "@$suggestion",
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                            }

                            item {
                                Button(
                                    onClick = {
                                        localError = when {
                                            username.trim().length < 3 ->
                                                "Please choose a username of at least 3 characters."
                                            !username.matches(Regex("[a-z0-9_]+")) ->
                                                "Username can contain only lowercase letters, numbers and underscore."
                                            else -> ""
                                        }

                                        if (localError.isBlank()) {
                                            step = 3
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(52.dp)
                                ) {
                                    Text(
                                        "Next: Select Account Type",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }

                    else -> {
                        Column(
                            modifier = Modifier.fillMaxSize(),
                            verticalArrangement = Arrangement.spacedBy(14.dp)
                        ) {
                            Text(
                                "Select the path you want to start with.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            RoleCard(
                                title = "I'm a Student Freelancer",
                                description = "Find projects, earn money, and build your verified portfolio.",
                                selected = role == "STUDENT_FREELANCER",
                                enabled = true,
                                accent = "STUDENT",
                                onClick = {
                                    role = "STUDENT_FREELANCER"
                                    localError = ""
                                }
                            )

                            RoleCard(
                                title = "I'm a Client",
                                description = "Hire talented student freelancers for your projects.",
                                selected = role == "CLIENT",
                                enabled = calculatedAge >= 18,
                                accent = "CLIENT",
                                onClick = {
                                    role = "CLIENT"
                                    localError = ""
                                }
                            )

                            if (calculatedAge in 16..17) {
                                Text(
                                    "Client accounts are available from age 18.",
                                    color = MaterialTheme.colorScheme.error,
                                    style = MaterialTheme.typography.bodySmall,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }

                            Spacer(Modifier.weight(1f))

                            Text(
                                "You can change your account type later in Settings.",
                                modifier = Modifier.fillMaxWidth(),
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )

                            Button(
                                onClick = {
                                    if (role.isBlank()) {
                                        localError = "Select how you want to use SkillLaunch."
                                    } else {
                                        localError = ""
                                        onSignup(
                                            firstName,
                                            middleName.ifBlank { null },
                                            lastName,
                                            username.trim(),
                                            email,
                                            password,
                                            role,
                                            calculatedAge
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(54.dp),
                                enabled = role.isNotBlank() && !state.isLoading
                            ) {
                                if (state.isLoading) {
                                    CircularProgressIndicator(
                                        modifier = Modifier.size(22.dp),
                                        strokeWidth = 2.dp
                                    )
                                } else {
                                    Text(
                                        "Create Account",
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
        }
    }
}

@Composable
private fun RoleCard(
    title: String,
    description: String,
    selected: Boolean,
    enabled: Boolean,
    accent: String,
    onClick: () -> Unit
) {
    val titleColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer
    } else if (enabled) {
        MaterialTheme.colorScheme.onSurface
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    val descriptionColor = if (selected) {
        MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.84f)
    } else {
        MaterialTheme.colorScheme.onSurfaceVariant
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = enabled, onClick = onClick),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(
            containerColor = if (selected) {
                MaterialTheme.colorScheme.primaryContainer
            } else {
                MaterialTheme.colorScheme.surface
            }
        )
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(46.dp),
                    shape = RoundedCornerShape(15.dp),
                    color = if (selected) {
                        MaterialTheme.colorScheme.primary
                    } else {
                        MaterialTheme.colorScheme.surfaceVariant
                    }
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            accent.take(1),
                            fontWeight = FontWeight.Black,
                            color = if (selected) {
                                MaterialTheme.colorScheme.onPrimary
                            } else {
                                MaterialTheme.colorScheme.primary
                            }
                        )
                    }
                }

                Spacer(Modifier.width(12.dp))

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = titleColor
                    )

                    if (!enabled) {
                        Text(
                            "Unavailable for this age",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.error,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                if (selected) {
                    Text(
                        "Selected",
                        style = MaterialTheme.typography.labelSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }

            Text(
                description,
                style = MaterialTheme.typography.bodyMedium,
                color = descriptionColor
            )
        }
    }
}



