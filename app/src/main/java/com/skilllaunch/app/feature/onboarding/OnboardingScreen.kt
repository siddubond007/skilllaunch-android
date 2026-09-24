package com.skilllaunch.app.feature.onboarding

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skilllaunch.app.data.model.auth.AuthUser
import com.skilllaunch.app.data.model.profile.OnboardingData
import com.skilllaunch.app.data.model.profile.ProfileUpdateRequest
import com.skilllaunch.app.data.repository.profile.ProfileRepository
import com.skilllaunch.app.feature.auth.AuthBackground
import com.skilllaunch.app.feature.auth.AuthField
import com.skilllaunch.app.feature.auth.AuthFieldIcon
import com.skilllaunch.app.feature.auth.AuthPrimaryButton
import com.skilllaunch.app.feature.auth.SkillLaunchBrand
import com.skilllaunch.app.feature.auth.ThemeToggle
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun OnboardingScreen(
    user: AuthUser,
    repository: ProfileRepository,
    darkTheme: Boolean,
    onToggleTheme: () -> Unit,
    onFinished: () -> Unit
) {
    val scope = rememberCoroutineScope()

    var step by rememberSaveable { mutableIntStateOf(1) }
    var primaryDomain by rememberSaveable { mutableStateOf("") }
    var selectedSkills by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var skillSearch by rememberSaveable { mutableStateOf("") }
    var githubUrl by rememberSaveable { mutableStateOf("") }
    var youtubeUrl by rememberSaveable { mutableStateOf("") }
    var portfolioUrl by rememberSaveable { mutableStateOf("") }
    var academicStatus by rememberSaveable { mutableStateOf("") }
    var graduationYear by rememberSaveable { mutableStateOf("") }
    var availability by rememberSaveable { mutableStateOf("") }
    var tagline by rememberSaveable { mutableStateOf("") }
    var bio by rememberSaveable { mutableStateOf("") }

    var clientType by rememberSaveable { mutableStateOf("") }
    var hiringCategories by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var hiringIntent by rememberSaveable { mutableStateOf("") }
    var projectScope by rememberSaveable { mutableStateOf("") }
    var companyOrProjectName by rememberSaveable { mutableStateOf("") }

    var saving by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val isStudent = user.role == "STUDENT_FREELANCER"

    fun finishWithSkip() {
        skipOnboarding(
            scope = scope,
            repository = repository,
            user = user,
            primaryDomain = primaryDomain,
            selectedSkills = selectedSkills,
            githubUrl = githubUrl,
            youtubeUrl = youtubeUrl,
            portfolioUrl = portfolioUrl,
            academicStatus = academicStatus,
            graduationYear = graduationYear,
            availability = availability,
            tagline = tagline,
            bio = bio,
            clientType = clientType,
            hiringCategories = hiringCategories,
            hiringIntent = hiringIntent,
            projectScope = projectScope,
            companyOrProjectName = companyOrProjectName,
            setSaving = { saving = it },
            setError = { error = it },
            onFinished = onFinished
        )
    }

    BackHandler {
        if (step > 1) {
            step -= 1
        } else {
            finishWithSkip()
        }
    }

    AuthBackground(darkTheme = darkTheme) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .safeDrawingPadding()
                .imePadding()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                SkillLaunchBrand(darkTheme = darkTheme, compact = true)
                Spacer(modifier = Modifier.weight(1f))
                ThemeToggle(
                    darkTheme = darkTheme,
                    onToggleTheme = onToggleTheme
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Profile setup",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.weight(1f))
                TextButton(
                    onClick = ::finishWithSkip,
                    enabled = !saving
                ) {
                    Text("Skip for now")
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            OnboardingProgress(
                current = step,
                total = if (isStudent) 4 else 3
            )

            Text(
                text = "Step " + step + " of " + (if (isStudent) 4 else 3) + " • " +
                    if (isStudent) studentStepLabels[step - 1] else clientStepLabels[step - 1],
                modifier = Modifier.padding(top = 7.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.labelSmall,
                fontWeight = FontWeight.SemiBold
            )

            AnimatedVisibility(
                visible = step > 1,
                enter = fadeIn() + slideInVertically(initialOffsetY = { -it / 2 }),
                exit = fadeOut() + slideOutVertically(targetOffsetY = { -it / 2 })
            ) {
                TextButton(
                    onClick = {
                        step -= 1
                        error = ""
                    },
                    contentPadding = androidx.compose.foundation.layout.PaddingValues(
                        horizontal = 0.dp,
                        vertical = 2.dp
                    )
                ) {
                    Text(
                        text = "← Back to previous step",
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f),
                contentAlignment = Alignment.TopCenter
            ) {
                LazyColumn(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(14.dp)
                ) {
                    when {
                        isStudent && step == 1 -> item {
                            SectionHeader(
                                emoji = "⚡",
                                title = "What's your superpower?",
                                subtitle = "Choose your main focus so SkillLaunch can match you with the right projects."
                            )
                            SelectionGrid(
                                options = studentDomains.keys.toList(),
                                selected = primaryDomain,
                                onSelect = {
                                    primaryDomain = it
                                    selectedSkills = emptyList()
                                    skillSearch = ""
                                    error = ""
                                }
                            )
                        }

                        isStudent && step == 2 -> item {
                            SectionHeader(
                                emoji = "🚀",
                                title = "Show, don't tell.",
                                subtitle = "Pick the skills you use most and add a place where clients can see your work. Everything here can be edited later."
                            )
                            AuthField(
                                label = "Search skills",
                                value = skillSearch,
                                onValueChange = {
                                    skillSearch = it
                                    error = ""
                                },
                                placeholder = "Search " + primaryDomain.ifBlank { "your domain" } + " skills",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Text,
                                    imeAction = ImeAction.Next
                                ),
                                leadingIcon = AuthFieldIcon.Search
                            )
                            Spacer(modifier = Modifier.height(5.dp))
                            Text(
                                text = selectedSkills.size.toString() + "/6 primary skills selected",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelSmall,
                                fontWeight = FontWeight.SemiBold
                            )
                            SelectionChipGroup(
                                options = (studentDomains[primaryDomain] ?: emptyList()).filter {
                                    it.contains(skillSearch.trim(), ignoreCase = true)
                                },
                                selected = selectedSkills,
                                onToggle = { skill ->
                                    selectedSkills = toggleMulti(selectedSkills, skill, 6)
                                    error = ""
                                }
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            AuthField(
                                label = "GitHub",
                                value = githubUrl,
                                onValueChange = { githubUrl = it; error = "" },
                                placeholder = "https://github.com/yourname",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Uri,
                                    imeAction = ImeAction.Next
                                ),
                                leadingIcon = AuthFieldIcon.User
                            )
                            AuthField(
                                label = "YouTube / Vimeo",
                                value = youtubeUrl,
                                onValueChange = { youtubeUrl = it; error = "" },
                                placeholder = "https://youtube.com/@yourname",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Uri,
                                    imeAction = ImeAction.Next
                                ),
                                leadingIcon = AuthFieldIcon.User
                            )
                            AuthField(
                                label = "Portfolio / Drive",
                                value = portfolioUrl,
                                onValueChange = { portfolioUrl = it; error = "" },
                                placeholder = "https://your-portfolio-link",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Uri,
                                    imeAction = ImeAction.Done
                                ),
                                leadingIcon = AuthFieldIcon.User
                            )
                        }

                        isStudent && step == 3 -> item {
                            SectionHeader(
                                emoji = "🎓",
                                title = "Where are you in your journey?",
                                subtitle = "Tell clients what stage you're at and when you're generally available."
                            )
                            SelectionChipGroup(
                                options = academicStatuses,
                                selected = listOf(academicStatus),
                                onToggle = { value ->
                                    academicStatus = value
                                    error = ""
                                },
                                singleSelect = true
                            )
                            AuthField(
                                label = "Expected graduation year",
                                value = graduationYear,
                                onValueChange = { value ->
                                    graduationYear = value.filter(Char::isDigit).take(4)
                                    error = ""
                                },
                                placeholder = "2028",
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Number,
                                    imeAction = ImeAction.Next
                                ),
                                leadingIcon = AuthFieldIcon.Check
                            )
                            Text(
                                text = "Optional for self-taught or recently graduated users.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                            Text(
                                text = "Availability",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            SelectionChipGroup(
                                options = availabilityOptions,
                                selected = listOf(availability),
                                onToggle = { value ->
                                    availability = value
                                    error = ""
                                },
                                singleSelect = true
                            )
                        }

                        isStudent && step == 4 -> item {
                            SectionHeader(
                                emoji = "✨",
                                title = "Put a little personality in your profile.",
                                subtitle = "A short headline and micro-bio help clients understand you quickly."
                            )
                            AuthField(
                                label = "Headline",
                                value = tagline,
                                onValueChange = { value ->
                                    tagline = value.take(70)
                                    error = ""
                                },
                                placeholder = "AI & ML student building useful products",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                leadingIcon = AuthFieldIcon.User
                            )
                            OnboardingTextArea(
                                label = "Micro-bio",
                                value = bio,
                                onValueChange = { value ->
                                    bio = value.take(150)
                                    error = ""
                                },
                                placeholder = "CS student who loves turning ideas into working apps."
                            )
                            Text(
                                text = "${bio.length}/150",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        !isStudent && step == 1 -> item {
                            SectionHeader(
                                emoji = "💼",
                                title = "Who's hiring today?",
                                subtitle = "Tell us what kind of client you are so we can shape your hiring experience."
                            )
                            SelectionGrid(
                                options = clientTypes,
                                selected = clientType,
                                onSelect = {
                                    clientType = it
                                    error = ""
                                }
                            )
                        }

                        !isStudent && step == 2 -> item {
                            SectionHeader(
                                emoji = "🎯",
                                title = "What do you need built?",
                                subtitle = "Choose the talent you are looking for and the kind of work you expect."
                            )
                            SelectionChipGroup(
                                options = clientCategories,
                                selected = hiringCategories,
                                onToggle = {
                                    hiringCategories = toggleMulti(hiringCategories, it, 6)
                                    error = ""
                                }
                            )
                            Text(
                                text = "What best describes your goal?",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            SelectionChipGroup(
                                options = hiringIntentOptions,
                                selected = listOf(hiringIntent),
                                onToggle = {
                                    hiringIntent = it
                                    error = ""
                                },
                                singleSelect = true
                            )
                            Text(
                                text = "Project scope",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold
                            )
                            SelectionChipGroup(
                                options = projectScopeOptions,
                                selected = listOf(projectScope),
                                onToggle = {
                                    projectScope = it
                                    error = ""
                                },
                                singleSelect = true
                            )
                        }

                        !isStudent && step == 3 -> item {
                            SectionHeader(
                                emoji = "🌱",
                                title = "Tell students who they're building for.",
                                subtitle = "A simple identity makes your first project feel more trustworthy without slowing you down."
                            )
                            AuthField(
                                label = "Company / Project name",
                                value = companyOrProjectName,
                                onValueChange = { value ->
                                    companyOrProjectName = value.take(60)
                                    error = ""
                                },
                                placeholder = "SkillLaunch Labs",
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
                                leadingIcon = AuthFieldIcon.User
                            )
                            OnboardingTextArea(
                                label = "Short tagline",
                                value = tagline,
                                onValueChange = { value ->
                                    tagline = value.take(100)
                                    error = ""
                                },
                                placeholder = "We're building tools that help students get real-world experience."
                            )
                            Text(
                                text = "${tagline.length}/100",
                                modifier = Modifier.fillMaxWidth(),
                                textAlign = androidx.compose.ui.text.style.TextAlign.End,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }
                    }

                    if (error.isNotBlank()) {
                        item {
                            Text(
                                text = error,
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val isLastStep = step == if (isStudent) 4 else 3
            val buttonEnabled = when {
                saving -> false
                isStudent && step == 1 -> primaryDomain.isNotBlank()
                isStudent && step == 2 -> selectedSkills.isNotEmpty()
                isStudent && step == 3 -> academicStatus.isNotBlank() && availability.isNotBlank()
                isStudent && step == 4 -> true
                !isStudent && step == 1 -> clientType.isNotBlank()
                !isStudent && step == 2 -> hiringCategories.isNotEmpty() && hiringIntent.isNotBlank() && projectScope.isNotBlank()
                else -> companyOrProjectName.isNotBlank()
            }

            AuthPrimaryButton(
                text = when {
                    isLastStep && isStudent -> "Find My First Project"
                    isLastStep -> "Explore Talent"
                    else -> "Continue"
                },
                enabled = buttonEnabled,
                loading = saving
            ) {
                if (isLastStep) {
                    validateAndSave(
                        scope = scope,
                        repository = repository,
                        user = user,
                        primaryDomain = primaryDomain,
                        selectedSkills = selectedSkills,
                        githubUrl = githubUrl,
                        youtubeUrl = youtubeUrl,
                        portfolioUrl = portfolioUrl,
                        academicStatus = academicStatus,
                        graduationYear = graduationYear,
                        availability = availability,
                        tagline = tagline,
                        bio = bio,
                        clientType = clientType,
                        hiringCategories = hiringCategories,
                        hiringIntent = hiringIntent,
                        projectScope = projectScope,
                        companyOrProjectName = companyOrProjectName,
                        setSaving = { saving = it },
                        setError = { error = it },
                        onFinished = onFinished,
                        isStudent = isStudent
                    )
                } else {
                    error = ""
                    step += 1
                }
            }
        }
    }
}

@Composable
private fun OnboardingProgress(current: Int, total: Int) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(
                        if (index < current) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
                    )
            )
        }
    }
}

@Composable
private fun SectionHeader(
    emoji: String,
    title: String,
    subtitle: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(text = emoji, fontSize = 25.sp)
        Text(
            text = title,
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.ExtraBold
        )
        Text(
            text = subtitle,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
private fun SelectionGrid(
    options: List<String>,
    selected: String,
    onSelect: (String) -> Unit
) {
    Column(verticalArrangement = Arrangement.spacedBy(9.dp)) {
        options.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(9.dp)
            ) {
                row.forEach { option ->
                    ChoiceCard(
                        text = option,
                        description = studentDomainDescriptions[option].orEmpty(),
                        icon = studentDomainIcons[option] ?: "✦",
                        selected = selected == option,
                        modifier = Modifier.weight(1f),
                        onClick = { onSelect(option) }
                    )
                }
                if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SelectionChipGroup(
    options: List<String>,
    selected: List<String>,
    onToggle: (String) -> Unit,
    singleSelect: Boolean = false
) {
    FlowRow(
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        options.forEach { option ->
            FilterChip(
                selected = selected.contains(option),
                onClick = {
                    if (singleSelect && selected.contains(option)) {
                        onToggle("")
                    } else {
                        onToggle(option)
                    }
                },
                label = { Text(option) }
            )
        }
    }
}

@Composable
private fun ChoiceCard(
    text: String,
    description: String,
    icon: String,
    selected: Boolean,
    modifier: Modifier,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)
    Box(
        modifier = modifier
            .height(92.dp)
            .clip(shape)
            .then(
                if (selected) {
                    Modifier.background(
                        brush = Brush.horizontalGradient(
                            listOf(Color(0xFF5845E9), Color(0xFF8B3BEB))
                        )
                    )
                } else {
                    Modifier.background(
                        color = MaterialTheme.colorScheme.surface.copy(alpha = 0.48f)
                    )
                }
            )
            .border(
                BorderStroke(
                    1.dp,
                    if (selected) Color.Transparent
                    else MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f)
                ),
                shape
            )
            .clickable(onClick = onClick)
            .padding(14.dp),
        contentAlignment = Alignment.CenterStart
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = if (selected) "✓" else icon,
                color = if (selected) Color.White else MaterialTheme.colorScheme.primary,
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = text,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelLarge,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = description,
                    color = if (selected) {
                        Color.White.copy(alpha = 0.82f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
        }
    }
}

@Composable
private fun OnboardingTextArea(
    label: String,
    value: String,
    onValueChange: (String) -> Unit,
    placeholder: String
) {
    Column(verticalArrangement = Arrangement.spacedBy(7.dp)) {
        Text(
            text = label,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.labelMedium,
            fontWeight = FontWeight.SemiBold
        )
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier
                .fillMaxWidth()
                .height(105.dp),
            minLines = 4,
            maxLines = 4,
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 15.sp,
                lineHeight = 21.sp
            ),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.48f))
                        .border(
                            1.dp,
                            MaterialTheme.colorScheme.onSurface.copy(alpha = 0.09f),
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.64f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    inner()
                }
            }
        )
    }
}

private fun toggleMulti(
    selected: List<String>,
    value: String,
    max: Int
): List<String> {
    return if (selected.contains(value)) {
        selected - value
    } else if (selected.size < max && value.isNotBlank()) {
        selected + value
    } else {
        selected
    }
}

private fun validateAndSave(
    scope: CoroutineScope,
    repository: ProfileRepository,
    user: AuthUser,
    primaryDomain: String,
    selectedSkills: List<String>,
    githubUrl: String,
    youtubeUrl: String,
    portfolioUrl: String,
    academicStatus: String,
    graduationYear: String,
    availability: String,
    tagline: String,
    bio: String,
    clientType: String,
    hiringCategories: List<String>,
    hiringIntent: String,
    projectScope: String,
    companyOrProjectName: String,
    setSaving: (Boolean) -> Unit,
    setError: (String) -> Unit,
    onFinished: () -> Unit,
    isStudent: Boolean
) {
    if (isStudent) {
        if (primaryDomain.isBlank()) {
            setError("Choose your main focus to continue.")
            return
        }
        if (selectedSkills.isEmpty()) {
            setError("Choose at least one skill.")
            return
        }
        if (academicStatus.isBlank() || availability.isBlank()) {
            setError("Choose your academic status and availability.")
            return
        }
    } else {
        if (clientType.isBlank() || hiringCategories.isEmpty() || hiringIntent.isBlank() || projectScope.isBlank()) {
            setError("Complete your hiring preferences first.")
            return
        }
        if (companyOrProjectName.isBlank()) {
            setError("Add a company or project name.")
            return
        }
    }

    val data = OnboardingData(
        role = if (isStudent) "STUDENT_FREELANCER" else "CLIENT",
        primaryDomain = primaryDomain.ifBlank { null },
        selectedSkills = selectedSkills,
        githubUrl = githubUrl.trim().ifBlank { null },
        youtubeUrl = youtubeUrl.trim().ifBlank { null },
        portfolioUrl = portfolioUrl.trim().ifBlank { null },
        academicStatus = academicStatus.ifBlank { null },
        graduationYear = graduationYear.toIntOrNull(),
        availability = availability.ifBlank { null },
        clientType = clientType.ifBlank { null },
        hiringCategories = hiringCategories,
        hiringIntent = hiringIntent.ifBlank { null },
        projectScope = projectScope.ifBlank { null },
        companyOrProjectName = companyOrProjectName.trim().ifBlank { null }
    )

    val request = ProfileUpdateRequest(
        tagline = if (isStudent) tagline.trim().ifBlank { null } else companyOrProjectName.trim().ifBlank { null },
        bio = if (isStudent) bio.trim().ifBlank { null } else tagline.trim().ifBlank { null },
        category = if (isStudent) primaryDomain else hiringCategories.firstOrNull(),
        skills = if (isStudent) selectedSkills else null,
        responseTimeExpectation = if (isStudent) availability else projectScope,
        githubUrl = githubUrl.trim().ifBlank { null },
        youtubeUrl = youtubeUrl.trim().ifBlank { null },
        drivePortfolio = portfolioUrl.trim().ifBlank { null },
        onboardingCompleted = true,
        onboardingData = data
    )

    setSaving(true)
    setError("")

    scope.launch {
        repository.updateProfile(request)
            .onSuccess {
                setSaving(false)
                onFinished()
            }
            .onFailure { exception ->
                setSaving(false)
                setError(
                    exception.message
                        ?: "Unable to save your setup right now. You can skip and finish it later."
                )
            }
    }
}

private fun skipOnboarding(
    scope: CoroutineScope,
    repository: ProfileRepository,
    user: AuthUser,
    primaryDomain: String,
    selectedSkills: List<String>,
    githubUrl: String,
    youtubeUrl: String,
    portfolioUrl: String,
    academicStatus: String,
    graduationYear: String,
    availability: String,
    tagline: String,
    bio: String,
    clientType: String,
    hiringCategories: List<String>,
    hiringIntent: String,
    projectScope: String,
    companyOrProjectName: String,
    setSaving: (Boolean) -> Unit,
    setError: (String) -> Unit,
    onFinished: () -> Unit
) {
    val data = OnboardingData(
        role = user.role ?: "STUDENT_FREELANCER",
        primaryDomain = primaryDomain.ifBlank { null },
        selectedSkills = selectedSkills,
        githubUrl = githubUrl.trim().ifBlank { null },
        youtubeUrl = youtubeUrl.trim().ifBlank { null },
        portfolioUrl = portfolioUrl.trim().ifBlank { null },
        academicStatus = academicStatus.ifBlank { null },
        graduationYear = graduationYear.toIntOrNull(),
        availability = availability.ifBlank { null },
        clientType = clientType.ifBlank { null },
        hiringCategories = hiringCategories,
        hiringIntent = hiringIntent.ifBlank { null },
        projectScope = projectScope.ifBlank { null },
        companyOrProjectName = companyOrProjectName.trim().ifBlank { null }
    )

    val request = ProfileUpdateRequest(
        githubUrl = githubUrl.trim().ifBlank { null },
        youtubeUrl = youtubeUrl.trim().ifBlank { null },
        drivePortfolio = portfolioUrl.trim().ifBlank { null },
        onboardingCompleted = true,
        onboardingData = data
    )

    setSaving(true)
    setError("")

    scope.launch {
        repository.updateProfile(request)
            .onSuccess {
                setSaving(false)
                onFinished()
            }
            .onFailure {
                setSaving(false)
                setError("We couldn't save that right now. Try again.")
            }
    }
}

private val studentDomains = linkedMapOf(
    "Web Development" to listOf(
        "Frontend", "Backend", "Full Stack", "React", "Next.js", "Vue.js",
        "Angular", "Node.js", "Express", "REST APIs", "E-commerce", "Web Performance"
    ),
    "Mobile Development" to listOf(
        "Android", "Kotlin", "Java", "iOS", "Swift", "Flutter",
        "React Native", "Mobile UI", "Jetpack Compose", "App APIs", "Firebase", "App Testing"
    ),
    "Software & APIs" to listOf(
        "Java", "Python", "C", "C++", "C#", "Go", "Rust",
        "REST APIs", "GraphQL", "Desktop Apps", "Automation", "Scripting"
    ),
    "Data & AI" to listOf(
        "Machine Learning", "Deep Learning", "Data Analysis", "Generative AI", "NLP",
        "Computer Vision", "Python", "Pandas", "NumPy", "TensorFlow", "PyTorch", "Data Visualization"
    ),
    "Cybersecurity" to listOf(
        "Web Security", "Network Security", "Ethical Hacking", "Penetration Testing",
        "SOC", "SIEM", "Security Testing", "Vulnerability Assessment", "OSINT", "Cloud Security", "Linux", "Digital Forensics"
    ),
    "Cloud & DevOps" to listOf(
        "AWS", "Azure", "Google Cloud", "Docker", "Kubernetes", "CI/CD",
        "GitHub Actions", "Terraform", "Linux", "Nginx", "Monitoring", "Cloud Architecture"
    ),
    "UI/UX Design" to listOf(
        "UI Design", "UX Research", "Figma", "Prototyping", "Wireframing", "Design Systems",
        "Mobile UX", "Web UX", "Interaction Design", "Usability Testing", "Design Audits", "Accessibility"
    ),
    "Graphic & Brand Design" to listOf(
        "Logos", "Branding", "Social Media", "Illustration", "Print Design", "Presentation Design",
        "Canva", "Photoshop", "Illustrator", "Posters", "Thumbnails", "Brand Guidelines"
    ),
    "Video & Motion" to listOf(
        "Video Editing", "Short-form", "YouTube", "Motion Graphics", "Color Grading",
        "Reels", "DaVinci Resolve", "Premiere Pro", "After Effects", "Subtitles", "Storyboarding", "Podcast Editing"
    ),
    "Writing & Content" to listOf(
        "Content Writing", "Copywriting", "Technical Writing", "Blogging", "Proofreading",
        "Script Writing", "Documentation", "Editing", "Research Writing", "Product Descriptions", "Ghostwriting", "Resume Writing"
    ),
    "Marketing & SEO" to listOf(
        "SEO", "Social Media", "Email Marketing", "Content Strategy", "Google Ads",
        "Meta Ads", "Keyword Research", "Analytics", "Influencer Marketing", "Lead Generation", "Campaign Planning", "Community Management"
    ),
    "Business & Research" to listOf(
        "Business Analysis", "Market Research", "Presentations", "Data Research", "Strategy",
        "Documentation", "Competitor Research", "Business Plans", "Financial Research", "Process Mapping", "Operations", "Reports"
    ),
    "Education & Tutoring" to listOf(
        "Programming", "Mathematics", "Science", "English", "Languages", "Academic Help",
        "Test Preparation", "Computer Science", "Study Planning", "Presentation Coaching", "Assignment Guidance", "Tutoring"
    ),
    "Photography & Creative" to listOf(
        "Photography", "Photo Editing", "Retouching", "Product Photos", "Creative Direction",
        "Canva", "Lightroom", "Portraits", "Event Photography", "Photo Manipulation", "Background Removal", "Color Correction"
    ),
    "Game Development" to listOf(
        "Unity", "Unreal Engine", "Game Design", "3D Assets", "Gameplay",
        "Level Design", "C#", "Blender", "2D Games", "3D Games", "Shaders", "Game UI"
    ),
    "Other" to listOf(
        "Virtual Assistance", "Data Entry", "Transcription", "Research", "Presentation Work",
        "Other Skills", "Customer Support", "Spreadsheet Work", "Web Research", "File Conversion", "Typing", "Administrative Support"
    )
)

private val studentDomainIcons = mapOf(
    "Web Development" to "🌐",
    "Mobile Development" to "📱",
    "Software & APIs" to "⚙️",
    "Data & AI" to "🧠",
    "Cybersecurity" to "🛡️",
    "Cloud & DevOps" to "☁️",
    "UI/UX Design" to "🎨",
    "Graphic & Brand Design" to "✨",
    "Video & Motion" to "🎬",
    "Writing & Content" to "✍️",
    "Marketing & SEO" to "📣",
    "Business & Research" to "📊",
    "Education & Tutoring" to "🎓",
    "Photography & Creative" to "📷",
    "Game Development" to "🎮",
    "Other" to "✦"
)

private val studentDomainDescriptions = mapOf(
    "Web Development" to "Sites, apps, storefronts",
    "Mobile Development" to "Android, iOS, cross-platform",
    "Software & APIs" to "Apps, automation, integrations",
    "Data & AI" to "ML, GenAI, analytics",
    "Cybersecurity" to "Security, testing, defense",
    "Cloud & DevOps" to "Cloud, containers, CI/CD",
    "UI/UX Design" to "Interfaces, research, prototypes",
    "Graphic & Brand Design" to "Branding, visuals, presentations",
    "Video & Motion" to "Editing, reels, motion graphics",
    "Writing & Content" to "Articles, copy, scripts",
    "Marketing & SEO" to "SEO, social, campaigns",
    "Business & Research" to "Analysis, research, strategy",
    "Education & Tutoring" to "Subjects, coding, languages",
    "Photography & Creative" to "Photos, editing, creative work",
    "Game Development" to "Games, gameplay, 3D",
    "Other" to "Skills outside these categories"
)

private val studentStepLabels = listOf("Focus", "Skills", "Journey", "Profile")
private val clientStepLabels = listOf("Client type", "Hiring needs", "Identity")

private val academicStatuses = listOf(
    "High School",
    "Undergraduate",
    "Postgraduate",
    "Recently Graduated",
    "Self-taught / Early Career"
)

private val availabilityOptions = listOf(
    "Small tasks",
    "Part-time projects",
    "Large projects",
    "Evenings / Weekends",
    "Just browsing"
)

private val clientTypes = listOf(
    "Solo Founder / Individual",
    "Early-stage Startup",
    "Small Business",
    "Company",
    "Academic / Research",
    "Non-profit / Organization"
)

private val clientCategories = listOf(
    "Web Development",
    "Mobile Development",
    "UI/UX Design",
    "Graphic Design",
    "AI / ML",
    "Data",
    "Writing",
    "Marketing",
    "Video / Audio",
    "Other"
)

private val hiringIntentOptions = listOf(
    "One-time project",
    "Ongoing support",
    "Build a product",
    "Quick task"
)

private val projectScopeOptions = listOf(
    "Under 1 week",
    "1–4 weeks",
    "1–3 months",
    "3+ months"
)
