package com.skilllaunch.app.feature.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skilllaunch.app.feature.auth.SkillLaunchBrand

@Composable
internal fun StudentSkillsSelection(
    primaryDomain: String,
    selectedSkills: List<String>,
    skillSearch: String,
    githubUrl: String,
    youtubeUrl: String,
    portfolioUrl: String,
    darkTheme: Boolean,
    saving: Boolean,
    skipConfirmation: Boolean,
    error: String,
    onBack: () -> Unit,
    onSkillSearchChange: (String) -> Unit,
    onToggleSkill: (String) -> Unit,
    onGithubChange: (String) -> Unit,
    onYoutubeChange: (String) -> Unit,
    onPortfolioChange: (String) -> Unit,
    onContinue: () -> Unit,
    onConfirmSkip: () -> Unit,
    onDismissSkip: () -> Unit
) {
    val pageBackground = if (darkTheme) Color(0xFF1A1A1D) else Color(0xFFF8F7FA)
    val searchBackground = if (darkTheme) Color(0xFF262629) else Color(0xFFE8E7EA)
    val cardBackground = if (darkTheme) Color(0xFF303034) else Color(0xFFE0DFE2)
    val selectedBackground = if (darkTheme) Color(0xFF34343A) else Color(0xFFF8F6FC)
    val textPrimary = if (darkTheme) Color.White else Color(0xFF17171A)
    val textMuted = if (darkTheme) Color(0xFFAAA9AE) else Color(0xFF77767D)
    val divider = if (darkTheme) Color.White.copy(alpha = 0.08f) else Color.Black.copy(alpha = 0.08f)
    val lavender = Color(0xFFD4C6FF)

    val allSkills = STUDENT_ONBOARDING_SKILLS_BY_DOMAIN[primaryDomain].orEmpty()
    val normalizedSearch = skillSearch.trim()
    val filteredSkills = if (normalizedSearch.isBlank()) {
        allSkills
    } else {
        allSkills.filter { skill ->
            skill.title.contains(normalizedSearch, ignoreCase = true)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackground)
            .safeDrawingPadding()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.size(48.dp)
            ) {
                BackArrowGlyph(tint = textPrimary, modifier = Modifier.size(22.dp))
            }

            Spacer(modifier = Modifier.weight(1f))
            SkillLaunchBrand(darkTheme = darkTheme, compact = true)
            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(48.dp))
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(if (darkTheme) Color(0xFF3A393E) else Color(0xFFD8D7DA))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.5f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(lavender)
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 28.dp,
                    vertical = 14.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "What are you great at?",
                        modifier = Modifier.padding(top = 2.dp),
                        color = textPrimary,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 40.sp,
                            lineHeight = 43.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-0.9).sp
                        )
                    )
                }

                item {
                    Text(
                        text = "Choose up to 6 skills from your selected domain. These are the real SkillLaunch marketplace subcategories used to describe services.",
                        color = textMuted,
                        fontSize = 16.sp,
                        lineHeight = 21.sp
                    )
                }

                item {
                    Text(
                        text = primaryDomain.uppercase(),
                        color = textMuted,
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                item {
                    StageTwoSearchField(
                        value = skillSearch,
                        placeholder = "Search skills (e.g. Android Development)...",
                        darkTheme = darkTheme,
                        background = searchBackground,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onSkillSearchChange,
                        onClear = { onSkillSearchChange("") }
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "${selectedSkills.size}/6 selected",
                            color = if (selectedSkills.size >= 6) lavender else textMuted,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "Select the areas you can actually deliver.",
                            color = textMuted,
                            fontSize = 12.sp
                        )
                    }
                }

                filteredSkills.chunked(2).forEach { row ->
                    item(key = row.joinToString("|") { it.id }) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(12.dp)
                        ) {
                            row.forEach { skill ->
                                StageTwoSkillCard(
                                    skill = skill,
                                    selected = selectedSkills.contains(skill.title),
                                    darkTheme = darkTheme,
                                    cardBackground = cardBackground,
                                    selectedBackground = selectedBackground,
                                    textPrimary = textPrimary,
                                    textMuted = textMuted,
                                    lavender = lavender,
                                    onClick = { onToggleSkill(skill.title) },
                                    modifier = Modifier.weight(1f)
                                )
                            }
                            if (row.size == 1) Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }

                if (filteredSkills.isEmpty()) {
                    item {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 28.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "No skills found. Try a broader search.",
                                color = textMuted,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                item {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Show your work (optional)",
                        color = textPrimary,
                        style = TextStyle(
                            fontFamily = FontFamily.Serif,
                            fontSize = 25.sp,
                            lineHeight = 29.sp,
                            fontWeight = FontWeight.Bold
                        )
                    )
                    Text(
                        text = "Add links that help clients see proof of what you can do.",
                        modifier = Modifier.padding(top = 4.dp),
                        color = textMuted,
                        fontSize = 14.sp,
                        lineHeight = 19.sp
                    )
                }

                item {
                    StageTwoLinkField(
                        label = "GitHub",
                        value = githubUrl,
                        placeholder = "https://github.com/yourname",
                        darkTheme = darkTheme,
                        background = searchBackground,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onGithubChange,
                        kind = LinkKind.GITHUB
                    )
                }

                item {
                    StageTwoLinkField(
                        label = "YouTube / Vimeo",
                        value = youtubeUrl,
                        placeholder = "https://youtube.com/@yourname",
                        darkTheme = darkTheme,
                        background = searchBackground,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onYoutubeChange,
                        kind = LinkKind.VIDEO
                    )
                }

                item {
                    StageTwoLinkField(
                        label = "Portfolio / Drive",
                        value = portfolioUrl,
                        placeholder = "https://your-portfolio-link",
                        darkTheme = darkTheme,
                        background = searchBackground,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onPortfolioChange,
                        kind = LinkKind.WEB
                    )
                }

                if (error.isNotBlank()) {
                    item {
                        Text(
                            text = error,
                            modifier = Modifier.padding(top = 2.dp),
                            color = Color(0xFFD95C5C),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        Column(modifier = Modifier.navigationBarsPadding()) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(1.dp)
                    .background(divider)
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 20.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Button(
                    onClick = onContinue,
                    enabled = !saving,
                    modifier = Modifier
                        .fillMaxWidth(0.66f)
                        .height(52.dp),
                    shape = RoundedCornerShape(28.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lavender,
                        contentColor = Color(0xFF17171A),
                        disabledContainerColor = lavender.copy(alpha = 0.55f),
                        disabledContentColor = Color(0xFF17171A).copy(alpha = 0.65f)
                    )
                ) {
                    Text(
                        text = "Continue  →",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        if (skipConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissSkip,
                title = { Text("Skip profile setup?") },
                text = {
                    Text("Your progress will be saved. You can return to Profile later and finish the setup.")
                },
                confirmButton = {
                    TextButton(
                        onClick = onConfirmSkip,
                        enabled = !saving
                    ) { Text("Skip for now") }
                },
                dismissButton = {
                    TextButton(onClick = onDismissSkip) { Text("Keep setting up") }
                }
            )
        }
    }
}

@Composable
private fun StageTwoSearchField(
    value: String,
    placeholder: String,
    darkTheme: Boolean,
    background: Color,
    textPrimary: Color,
    textMuted: Color,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(background)
            .padding(horizontal = 17.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchGlyph(tint = textMuted, modifier = Modifier.size(21.dp))
        Spacer(modifier = Modifier.size(11.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            textStyle = TextStyle(color = textPrimary, fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (value.isBlank()) {
                    Text(text = placeholder, color = textMuted, fontSize = 15.sp, maxLines = 1)
                }
                innerTextField()
            }
        )
        if (value.isNotBlank()) {
            IconButton(onClick = onClear, modifier = Modifier.size(32.dp)) {
                CloseGlyph(tint = textMuted, modifier = Modifier.size(17.dp))
            }
        }
    }
}

@Composable
private fun StageTwoSkillCard(
    skill: StudentOnboardingSkill,
    selected: Boolean,
    darkTheme: Boolean,
    cardBackground: Color,
    selectedBackground: Color,
    textPrimary: Color,
    textMuted: Color,
    lavender: Color,
    onClick: () -> Unit,
    modifier: Modifier
) {
    Column(
        modifier = modifier
            .height(92.dp)
            .clip(RoundedCornerShape(20.dp))
            .background(if (selected) selectedBackground else cardBackground)
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = if (selected) lavender else Color.Transparent,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 13.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = skill.title,
                modifier = Modifier.weight(1f),
                color = textPrimary,
                fontSize = 14.sp,
                lineHeight = 18.sp,
                fontWeight = FontWeight.Bold
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .size(24.dp)
                        .shadow(5.dp, CircleShape)
                        .clip(CircleShape)
                        .background(if (darkTheme) Color(0xFF1A1A1D) else Color.White)
                        .border(1.dp, lavender, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "✓",
                        color = if (darkTheme) Color.White else Color(0xFF6F56D9),
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
        Text(
            text = "Marketplace skill",
            modifier = Modifier.fillMaxWidth().padding(top = 4.dp),
            color = textMuted,
            fontSize = 9.sp,
            fontWeight = FontWeight.SemiBold,
            textAlign = TextAlign.Start
        )
    }
}

private enum class LinkKind { GITHUB, VIDEO, WEB }

@Composable
private fun StageTwoLinkField(
    label: String,
    value: String,
    placeholder: String,
    darkTheme: Boolean,
    background: Color,
    textPrimary: Color,
    textMuted: Color,
    onValueChange: (String) -> Unit,
    kind: LinkKind
) {
    Column(verticalArrangement = Arrangement.spacedBy(6.dp)) {
        Text(
            text = label,
            color = textMuted,
            fontSize = 12.sp,
            fontWeight = FontWeight.Bold
        )
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(22.dp))
                .background(background)
                .padding(horizontal = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            LinkGlyph(kind = kind, tint = textMuted, modifier = Modifier.size(21.dp))
            Spacer(modifier = Modifier.size(10.dp))
            BasicTextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.weight(1f),
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Uri, imeAction = ImeAction.Next),
                textStyle = TextStyle(color = textPrimary, fontSize = 15.sp),
                decorationBox = { innerTextField ->
                    if (value.isBlank()) Text(text = placeholder, color = textMuted, fontSize = 15.sp, maxLines = 1)
                    innerTextField()
                }
            )
        }
    }
}

@Composable
private fun SearchGlyph(tint: Color, modifier: Modifier) {
    Canvas(modifier) {
        val stroke = 1.8.dp.toPx()
        drawCircle(color = tint, radius = size.minDimension * 0.31f, center = Offset(size.width * 0.42f, size.height * 0.42f), style = Stroke(width = stroke))
        drawLine(color = tint, start = Offset(size.width * 0.66f, size.height * 0.66f), end = Offset(size.width * 0.90f, size.height * 0.90f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
private fun CloseGlyph(tint: Color, modifier: Modifier) {
    Canvas(modifier) {
        val stroke = 1.8.dp.toPx()
        drawLine(color = tint, start = Offset(size.width * 0.25f, size.height * 0.25f), end = Offset(size.width * 0.75f, size.height * 0.75f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(size.width * 0.75f, size.height * 0.25f), end = Offset(size.width * 0.25f, size.height * 0.75f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
private fun BackArrowGlyph(tint: Color, modifier: Modifier) {
    Canvas(modifier) {
        val stroke = 1.9.dp.toPx()
        drawLine(color = tint, start = Offset(size.width * 0.78f, size.height * 0.50f), end = Offset(size.width * 0.24f, size.height * 0.50f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(size.width * 0.24f, size.height * 0.50f), end = Offset(size.width * 0.48f, size.height * 0.23f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(size.width * 0.24f, size.height * 0.50f), end = Offset(size.width * 0.48f, size.height * 0.77f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
private fun LinkGlyph(kind: LinkKind, tint: Color, modifier: Modifier) {
    Canvas(modifier) {
        val stroke = 1.7.dp.toPx()
        when (kind) {
            LinkKind.GITHUB -> {
                drawCircle(color = tint, radius = size.minDimension * 0.29f, center = Offset(size.width * 0.50f, size.height * 0.48f), style = Stroke(width = stroke))
                drawLine(color = tint, start = Offset(size.width * 0.24f, size.height * 0.72f), end = Offset(size.width * 0.36f, size.height * 0.57f), strokeWidth = stroke, cap = StrokeCap.Round)
                drawLine(color = tint, start = Offset(size.width * 0.76f, size.height * 0.72f), end = Offset(size.width * 0.64f, size.height * 0.57f), strokeWidth = stroke, cap = StrokeCap.Round)
            }
            LinkKind.VIDEO -> {
                drawRoundRect(color = tint, topLeft = Offset(size.width * 0.10f, size.height * 0.22f), size = androidx.compose.ui.geometry.Size(size.width * 0.80f, size.height * 0.56f), cornerRadius = androidx.compose.ui.geometry.CornerRadius(size.width * 0.12f, size.height * 0.12f), style = Stroke(width = stroke))
                val p = Path().apply {
                    moveTo(size.width * 0.43f, size.height * 0.34f)
                    lineTo(size.width * 0.43f, size.height * 0.66f)
                    lineTo(size.width * 0.70f, size.height * 0.50f)
                    close()
                }
                drawPath(p, color = tint, style = Stroke(width = stroke))
            }
            LinkKind.WEB -> {
                drawCircle(color = tint, radius = size.minDimension * 0.39f, center = Offset(size.width * 0.50f, size.height * 0.50f), style = Stroke(width = stroke))
                drawOval(color = tint, topLeft = Offset(size.width * 0.33f, size.height * 0.11f), size = androidx.compose.ui.geometry.Size(size.width * 0.34f, size.height * 0.78f), style = Stroke(width = stroke))
                drawLine(color = tint, start = Offset(size.width * 0.13f, size.height * 0.50f), end = Offset(size.width * 0.87f, size.height * 0.50f), strokeWidth = stroke)
            }
        }
    }
}