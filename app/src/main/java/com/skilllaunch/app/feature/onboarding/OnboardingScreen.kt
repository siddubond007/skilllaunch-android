package com.skilllaunch.app.feature.onboarding

import androidx.activity.compose.BackHandler
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.collectIsFocusedAsState

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
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
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
import androidx.compose.ui.platform.LocalContext
import com.skilllaunch.app.data.model.auth.AuthUser
import com.skilllaunch.app.data.model.profile.OnboardingData
import com.skilllaunch.app.data.model.profile.ProfileUpdateRequest
import com.skilllaunch.app.data.repository.profile.ProfileRepository
import com.skilllaunch.app.feature.auth.AuthBackground
import com.skilllaunch.app.feature.auth.AuthField
import com.skilllaunch.app.feature.auth.AuthFieldIcon
import com.skilllaunch.app.feature.auth.AuthPrimaryButton
import com.skilllaunch.app.feature.auth.SkillLaunchBrand
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
    var customSkill by rememberSaveable { mutableStateOf("") }
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
    var resumeFileName by rememberSaveable { mutableStateOf("") }
    var resumeUploaded by rememberSaveable { mutableStateOf(false) }
    var resumeUploading by rememberSaveable { mutableStateOf(false) }

    var clientType by rememberSaveable { mutableStateOf("") }
    var hiringCategories by rememberSaveable { mutableStateOf(emptyList<String>()) }
    var hiringIntent by rememberSaveable { mutableStateOf("") }
    var projectScope by rememberSaveable { mutableStateOf("") }
    var companyOrProjectName by rememberSaveable { mutableStateOf("") }

    var saving by remember { mutableStateOf(false) }
    var loadingInitialProfile by remember { mutableStateOf(true) }
    var showSkipConfirmation by remember { mutableStateOf(false) }
    var error by remember { mutableStateOf("") }

    val context = LocalContext.current
    val resumePickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.OpenDocument()
    ) { uri ->
        if (uri != null) {
            resumeUploading = true
            error = ""
            scope.launch {
                repository.uploadResume(context, uri)
                    .onSuccess { response ->
                        resumeFileName = response.fileName.orEmpty().ifBlank { "Resume uploaded" }
                        resumeUploaded = true
                        resumeUploading = false
                    }
                    .onFailure { exception ->
                        resumeUploading = false
                        resumeUploaded = false
                        error = exception.message ?: "Unable to upload your resume."
                    }
            }
        }
    }

    val isStudent = user.role == "STUDENT_FREELANCER"

    LaunchedEffect(user.id) {
        repository.getMyProfile()
            .onSuccess { profileUser ->
                val profile = profileUser.profile
                val data = profile?.onboardingData

                if (isStudent) {
                    val savedDomain = data?.primaryDomain.orEmpty()
                    if (savedDomain.isNotBlank() && (studentGigDomainNames.contains(savedDomain) || studentDomains.containsKey(savedDomain))) {
                        primaryDomain = savedDomain
                    } else if (savedDomain.isNotBlank()) {
                        primaryDomain = "Other"
                        customSkill = savedDomain
                    }
                    val validSkillTitles = STUDENT_ONBOARDING_SKILLS_BY_DOMAIN[savedDomain]
                        .orEmpty()
                        .map { it.title }
                        .toSet()
                    selectedSkills = data?.selectedSkills.orEmpty().filter { skill ->
                        savedDomain == "Other" || validSkillTitles.contains(skill)
                    }
                    githubUrl = data?.githubUrl.orEmpty()
                    youtubeUrl = data?.youtubeUrl.orEmpty()
                    portfolioUrl = data?.portfolioUrl.orEmpty()
                    val savedAcademicStatus = data?.academicStatus.orEmpty()
                    val stageThreeStatuses = setOf(
                        "Freshman",
                        "Sophomore",
                        "Junior",
                        "Senior",
                        "Graduated",
                        "Self-Taught"
                    )
                    academicStatus = savedAcademicStatus.takeIf { it in stageThreeStatuses }.orEmpty()
                    graduationYear = data?.graduationYear?.toString().orEmpty()
                    availability = data?.availability.orEmpty()
                    tagline = profile?.tagline.orEmpty()
                    bio = profile?.bio.orEmpty()
                } else {
                    clientType = data?.clientType.orEmpty()
                    hiringCategories = data?.hiringCategories.orEmpty()
                    hiringIntent = data?.hiringIntent.orEmpty()
                    projectScope = data?.projectScope.orEmpty()
                    companyOrProjectName = data?.companyOrProjectName.orEmpty()
                    tagline = profile?.bio.orEmpty()
                }

                resumeFileName = profile?.resumeFileName.orEmpty()
                resumeUploaded = !profile?.resumeUrl.isNullOrBlank()
                loadingInitialProfile = false
            }
            .onFailure {
                loadingInitialProfile = false
            }
    }

    fun finishWithSkip() {
        skipOnboarding(
            scope = scope,
            repository = repository,
            user = user,
            primaryDomain = primaryDomain,
            customSkill = customSkill,
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
        when {
            showSkipConfirmation -> showSkipConfirmation = false
            loadingInitialProfile -> error = "Please wait while your profile setup is loading."
            resumeUploading -> error = "Please wait for the resume upload to finish."
            step > 1 -> {
                step -= 1
                error = ""
            }
            else -> showSkipConfirmation = true
        }
    }

    if (isStudent && step == 1) {
        StudentDomainSelection(
            selectedDomain = primaryDomain,
            darkTheme = darkTheme,
            error = error,
            saving = saving,
            skipConfirmation = showSkipConfirmation,
            onBack = { showSkipConfirmation = true },
            onSelectDomain = {
                primaryDomain = it
                selectedSkills = emptyList()
                skillSearch = ""
                customSkill = ""
                error = ""
            },
            onContinue = {
                if (primaryDomain.isBlank()) {
                    error = "Choose your primary domain to continue."
                } else {
                    error = ""
                    step = 2
                }
            },
            onConfirmSkip = {
                showSkipConfirmation = false
                finishWithSkip()
            },
            onDismissSkip = { showSkipConfirmation = false }
        )
        return
    }

    if (isStudent && step == 2) {
        StudentSkillsSelection(
            primaryDomain = primaryDomain,
            selectedSkills = selectedSkills,
            skillSearch = skillSearch,
            githubUrl = githubUrl,
            youtubeUrl = youtubeUrl,
            portfolioUrl = portfolioUrl,
            darkTheme = darkTheme,
            saving = saving,
            skipConfirmation = showSkipConfirmation,
            error = error,
            onBack = {
                step = 1
                error = ""
            },
            onSkillSearchChange = {
                skillSearch = it
                error = ""
            },
            onToggleSkill = { skill ->
                selectedSkills = toggleMulti(selectedSkills, skill, 6)
                error = ""
            },
            onGithubChange = {
                githubUrl = it.take(250)
                error = ""
            },
            onYoutubeChange = {
                youtubeUrl = it.take(250)
                error = ""
            },
            onPortfolioChange = {
                portfolioUrl = it.take(250)
                error = ""
            },
            onContinue = {
                if (selectedSkills.isEmpty()) {
                    error = "Choose at least one primary skill to continue."
                } else {
                    error = ""
                    step = 3
                }
            },
            onConfirmSkip = {
                showSkipConfirmation = false
                finishWithSkip()
            },
            onDismissSkip = {
                showSkipConfirmation = false
            }
        )
        return
    }

    if (isStudent && step == 3) {
        OnboardingStageThree(
            academicStatus = academicStatus,
            graduationYear = graduationYear,
            availability = availability,
            darkTheme = darkTheme,
            error = error,
            saving = saving,
            onBack = {
                step = 2
                error = ""
            },
            onAcademicStatusChange = {
                academicStatus = it
                if (it == "Graduated" || it == "Self-Taught") {
                    graduationYear = ""
                }
                error = ""
            },
            onGraduationYearChange = {
                graduationYear = it
                error = ""
            },
            onAvailabilityChange = {
                availability = it
                error = ""
            },
            onContinue = {
                val validationMessage = when {
                    academicStatus.isBlank() ->
                        "Choose your academic status to continue."
                    availability.isBlank() ->
                        "Choose your work availability to continue."
                    else -> ""
                }

                if (validationMessage.isNotBlank()) {
                    error = validationMessage
                } else {
                    error = ""
                    step = 4
                }
            }
        )
        return
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
                    onClick = { showSkipConfirmation = true },
                    enabled = !saving && !resumeUploading && !loadingInitialProfile
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

            Spacer(modifier = Modifier.height(4.dp))

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
                                descriptionMap = studentDomainDescriptions,
                                iconMap = studentDomainIcons,
                                onSelect = {
                                    primaryDomain = it
                                    selectedSkills = emptyList()
                                    skillSearch = ""
                                    error = ""
                                }
                            )

                            if (primaryDomain == "Other") {
                                Column(
                                    modifier = Modifier.padding(top = 2.dp),
                                    verticalArrangement = Arrangement.spacedBy(7.dp)
                                ) {
                                    Text(
                                        text = "What skill do you offer?",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.labelLarge,
                                        fontWeight = FontWeight.Bold
                                    )
                                    AuthField(
                                        label = "Custom skill",
                                        value = customSkill,
                                        onValueChange = {
                                            customSkill = it.take(50)
                                            error = ""
                                        },
                                        placeholder = "e.g. CAD drafting, voice acting, Excel dashboards",
                                        keyboardOptions = KeyboardOptions(
                                            keyboardType = KeyboardType.Text,
                                            imeAction = ImeAction.Done
                                        ),
                                        leadingIcon = AuthFieldIcon.User
                                    )
                                    Text(
                                        text = "This becomes your primary profile skill instead of leaving your profile as “Other”.",
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        style = MaterialTheme.typography.bodySmall
                                    )
                                }
                            }
                        }

                        isStudent && step == 4 -> item {
                            SectionHeader(
                                emoji = "✨",
                                title = "Make your profile yours.",
                                subtitle = "Add a strong headline, a short intro, and optionally attach your resume."
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

                            Spacer(modifier = Modifier.height(6.dp))

                            ResumeUploadCard(
                                fileName = resumeFileName,
                                uploaded = resumeUploaded,
                                uploading = resumeUploading,
                                onUpload = {
                                    error = ""
                                    resumePickerLauncher.launch(
                                        arrayOf(
                                            "application/pdf",
                                            "application/msword",
                                            "application/vnd.openxmlformats-officedocument.wordprocessingml.document"
                                        )
                                    )
                                }
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
                                descriptionMap = clientTypeDescriptions,
                                iconMap = clientTypeIcons,
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

                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            val isLastStep = step == if (isStudent) 4 else 3

            if (showSkipConfirmation) {
                AlertDialog(
                    onDismissRequest = { showSkipConfirmation = false },
                    title = {
                        Text("Skip profile setup?")
                    },
                    text = {
                        Text(
                            "Your progress will be saved. You can return to Profile later and finish the setup."
                        )
                    },
                    confirmButton = {
                        TextButton(
                            onClick = {
                                showSkipConfirmation = false
                                finishWithSkip()
                            },
                            enabled = !saving && !loadingInitialProfile && !resumeUploading
                        ) {
                            Text("Skip for now")
                        }
                    },
                    dismissButton = {
                        TextButton(
                            onClick = { showSkipConfirmation = false }
                        ) {
                            Text("Keep setting up")
                        }
                    }
                )
            }

            AuthPrimaryButton(
                text = when {
                    isLastStep && isStudent -> "Find My First Project"
                    isLastStep -> "Explore Talent"
                    else -> "Continue"
                },
                enabled = true,
                loading = saving
            ) {
                if (isLastStep) {
                    validateAndSave(
                        scope = scope,
                        repository = repository,
                        user = user,
                        primaryDomain = primaryDomain,
                        customSkill = customSkill,
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
                    val validationMessage = when {
                        isStudent && step == 1 && primaryDomain.isBlank() ->
                            "Choose your main focus to continue."
                        isStudent && step == 1 && primaryDomain == "Other" && customSkill.trim().length < 2 ->
                            "Add the skill you offer so your profile has a clear focus."
                        isStudent && step == 2 && selectedSkills.isEmpty() ->
                            "Choose at least one primary skill to continue."
                        isStudent && step == 3 && academicStatus.isBlank() ->
                            "Choose your current academic status first."
                        isStudent && step == 3 && availability.isBlank() ->
                            "Choose when you can work so we can match you appropriately."
                        !isStudent && step == 1 && clientType.isBlank() ->
                            "Choose the client type to continue."
                        !isStudent && step == 2 && hiringCategories.isEmpty() ->
                            "Choose at least one talent category."
                        !isStudent && step == 2 && hiringIntent.isBlank() ->
                            "Choose your hiring goal."
                        !isStudent && step == 2 && projectScope.isBlank() ->
                            "Choose the project scope."
                        !isStudent && step == 3 && companyOrProjectName.isBlank() ->
                            "Add a company or project name."
                        else -> ""
                    }

                    if (validationMessage.isNotBlank()) {
                        error = validationMessage
                        return@AuthPrimaryButton
                    }

                    if (isStudent && step == 1 && primaryDomain == "Other") {
                        selectedSkills = listOf(customSkill.trim())
                    }

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
    descriptionMap: Map<String, String> = emptyMap(),
    iconMap: Map<String, String> = emptyMap(),
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
                        description = descriptionMap[option].orEmpty(),
                        icon = iconMap[option] ?: "✦",
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
            .height(76.dp)
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
                fontSize = 19.sp,
                fontWeight = FontWeight.Bold
            )
            Column(verticalArrangement = Arrangement.spacedBy(3.dp)) {
                Text(
                    text = text,
                    color = if (selected) Color.White else MaterialTheme.colorScheme.onSurface,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.ExtraBold,
                    maxLines = 1
                )
                Text(
                    text = description,
                    color = if (selected) {
                        Color.White.copy(alpha = 0.82f)
                    } else {
                        MaterialTheme.colorScheme.onSurfaceVariant
                    },
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 10.sp),
                    maxLines = 1
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
    val interactionSource = remember { androidx.compose.foundation.interaction.MutableInteractionSource() }
    val focused = interactionSource.collectIsFocusedAsState().value

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
            interactionSource = interactionSource,
            cursorBrush = androidx.compose.ui.graphics.SolidColor(
                MaterialTheme.colorScheme.onSurface
            ),
            textStyle = TextStyle(
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                lineHeight = 22.sp,
                fontWeight = FontWeight.Medium
            ),
            decorationBox = { inner ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(RoundedCornerShape(16.dp))
                        .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.48f))
                        .border(
                            1.dp,
                            if (focused) {
                                MaterialTheme.colorScheme.primary.copy(alpha = 0.72f)
                            } else {
                                MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
                            },
                            RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    contentAlignment = Alignment.TopStart
                ) {
                    if (value.isBlank()) {
                        Text(
                            text = placeholder,
                            color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.52f),
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
    customSkill: String,
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
        if (primaryDomain == "Other" && customSkill.trim().length < 2) {
            setError("Tell us the skill you want to offer so your profile has a real focus.")
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
        primaryDomain = if (primaryDomain == "Other") {
            customSkill.trim().ifBlank { null }
        } else {
            primaryDomain.ifBlank { null }
        },
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
        category = if (isStudent) {
            if (primaryDomain == "Other") customSkill.trim().ifBlank { null } else primaryDomain
        } else {
            hiringCategories.firstOrNull()
        },
        skills = if (isStudent) selectedSkills else null,
        responseTimeExpectation = if (isStudent) availability else projectScope,
        githubUrl = githubUrl.trim().ifBlank { null },
        youtubeUrl = youtubeUrl.trim().ifBlank { null },
        drivePortfolio = portfolioUrl.trim().ifBlank { null },
        onboardingCompleted = true,
        onboardingStatus = "COMPLETED",
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
    customSkill: String,
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
        primaryDomain = if (primaryDomain == "Other") {
            customSkill.trim().ifBlank { null }
        } else {
            primaryDomain.ifBlank { null }
        },
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
        tagline = if (user.role == "STUDENT_FREELANCER") {
            tagline.trim().ifBlank { null }
        } else {
            companyOrProjectName.trim().ifBlank { null }
        },
        bio = if (user.role == "STUDENT_FREELANCER") {
            bio.trim().ifBlank { null }
        } else {
            tagline.trim().ifBlank { null }
        },
        category = if (user.role == "STUDENT_FREELANCER") {
            if (primaryDomain == "Other") {
                customSkill.trim().ifBlank { null }
            } else {
                primaryDomain.trim().ifBlank { null }
            }
        } else {
            hiringCategories.firstOrNull()
        },
        skills = when {
            selectedSkills.isNotEmpty() -> selectedSkills
            primaryDomain == "Other" && customSkill.trim().isNotBlank() ->
                listOf(customSkill.trim())
            else -> null
        },
        responseTimeExpectation = if (user.role == "STUDENT_FREELANCER") {
            availability.trim().ifBlank { null }
        } else {
            projectScope.trim().ifBlank { null }
        },
        githubUrl = githubUrl.trim().ifBlank { null },
        youtubeUrl = youtubeUrl.trim().ifBlank { null },
        drivePortfolio = portfolioUrl.trim().ifBlank { null },
        onboardingCompleted = false,
        onboardingStatus = "SKIPPED",
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
                        ?: "We couldn't save your progress right now. Try again."
                )
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
    "High School Student",
    "Undergraduate Student",
    "Postgraduate Student",
    "Recently Graduated",
    "Graduate / Early Career",
    "Self-taught / Career Switcher"
)

private fun graduationYearLabel(status: String): String = when (status) {
    "High School Student",
    "Undergraduate Student",
    "Postgraduate Student" -> "Expected graduation year"
    else -> "Graduation year"
}

private fun graduationYearPlaceholder(status: String): String = when (status) {
    "High School Student" -> "2027"
    "Undergraduate Student" -> "2028"
    "Postgraduate Student" -> "2027"
    else -> "2025"
}

private val availabilityOptions = listOf(
    "Small tasks",
    "Part-time projects",
    "Full projects",
    "Evenings / Weekends",
    "Flexible / Open"
)

private val clientTypes = listOf(
    "Solo Founder / Individual",
    "Early-stage Startup",
    "Small Business",
    "Company",
    "Academic / Research",
    "Non-profit / Organization"
)

private val clientTypeIcons = mapOf(
    "Solo Founder / Individual" to "👤",
    "Early-stage Startup" to "🚀",
    "Small Business" to "🏪",
    "Company" to "🏢",
    "Academic / Research" to "🎓",
    "Non-profit / Organization" to "🤝"
)

private val clientTypeDescriptions = mapOf(
    "Solo Founder / Individual" to "Personal or founder-led work",
    "Early-stage Startup" to "Fast-moving new products",
    "Small Business" to "Growing local or online business",
    "Company" to "Established team or organization",
    "Academic / Research" to "Research or education projects",
    "Non-profit / Organization" to "Mission-driven projects"
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


@Composable
private fun ResumeUploadCard(
    fileName: String,
    uploaded: Boolean,
    uploading: Boolean,
    onUpload: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(shape)
            .background(MaterialTheme.colorScheme.surface.copy(alpha = 0.42f))
            .border(
                BorderStroke(
                    1.dp,
                    if (uploaded) {
                        MaterialTheme.colorScheme.primary.copy(alpha = 0.40f)
                    } else {
                        MaterialTheme.colorScheme.onSurface.copy(alpha = 0.10f)
                    }
                ),
                shape
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = if (uploaded) "✓" else "📄",
                fontSize = 22.sp,
                color = if (uploaded) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.size(10.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = if (uploaded) "Resume attached" else "Add your resume (optional)",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.ExtraBold
                )
                Text(
                    text = if (uploaded && fileName.isNotBlank()) fileName
                    else "PDF, DOC or DOCX • maximum 10 MB",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 2
                )
            }
            TextButton(
                onClick = onUpload,
                enabled = !uploading
            ) {
                Text(if (uploading) "Uploading…" else if (uploaded) "Replace" else "Upload")
            }
        }

        Text(
            text = "Your skills and portfolio are still the main profile signals. The resume is an optional supporting document.",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            style = MaterialTheme.typography.bodySmall
        )
    }
}