package com.skilllaunch.app.feature.onboarding

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skilllaunch.app.feature.auth.SkillLaunchBrand

@OptIn(ExperimentalLayoutApi::class)
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
    val fieldBackground = if (darkTheme) Color(0xFF262629) else Color.White
    val textPrimary = if (darkTheme) Color.White else Color(0xFF17171A)
    val textMuted = if (darkTheme) Color(0xFF9B99A1) else Color(0xFF6F6D76)
    val borderColor = if (darkTheme) Color(0xFF3D3C42) else Color(0xFFE1DFE5)
    val lavender = Color(0xFFD4C6FF)
    val selectedText = Color(0xFF17171A)

    val skills = studentGigDomainSkills[primaryDomain].orEmpty()
    val normalizedSearch = skillSearch.trim()
    val filteredSkills = if (normalizedSearch.isBlank()) {
        skills
    } else {
        skills.filter { it.contains(normalizedSearch, ignoreCase = true) }
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

        OnboardingStepProgress(
            current = 2,
            total = 4,
            darkTheme = darkTheme,
            lavender = lavender
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    start = 48.dp,
                    end = 24.dp,
                    top = 18.dp,
                    bottom = 22.dp
                ),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                item {
                    Text(
                        text = "Step 2 of 4 • Skills",
                        color = textMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        BackArrowGlyph(tint = textMuted, modifier = Modifier.size(17.dp))
                        Spacer(modifier = Modifier.size(5.dp))
                        TextButton(
                            onClick = onBack,
                            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = "Back to previous step",
                                color = textMuted,
                                fontSize = 14.sp
                            )
                        }
                    }
                }

                item {
                    Text(
                        text = "Show, don't tell.",
                        modifier = Modifier.padding(top = 8.dp),
                        color = textPrimary,
                        style = TextStyle(
                            fontFamily = FontFamily.SansSerif,
                            fontSize = 36.sp,
                            lineHeight = 40.sp,
                            fontWeight = FontWeight.ExtraBold,
                            letterSpacing = (-1.0).sp
                        )
                    )
                }

                item {
                    Text(
                        text = "Pick the skills you use most and add a place where clients can see your work. Everything here can be edited later.",
                        color = textMuted,
                        fontSize = 17.sp,
                        lineHeight = 25.sp
                    )
                }

                item {
                    StageTwoSearchField(
                        value = skillSearch,
                        placeholder = "Search " + primaryDomain.ifBlank { "your" } + " skills.",
                        darkTheme = darkTheme,
                        background = fieldBackground,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onSkillSearchChange,
                        onClear = { onSkillSearchChange("") }
                    )
                }

                item {
                    Text(
                        text = "\${selectedSkills.size}/6 primary skills selected",
                        color = textMuted,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                item {
                    FlowRow(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        filteredSkills.forEach { skill ->
                            StageTwoSkillChip(
                                text = skill,
                                selected = selectedSkills.contains(skill),
                                darkTheme = darkTheme,
                                lavender = lavender,
                                selectedText = selectedText,
                                borderColor = borderColor,
                                textPrimary = textPrimary,
                                onClick = { onToggleSkill(skill) }
                            )
                        }
                    }
                }

                item { Spacer(modifier = Modifier.height(4.dp)) }

                item {
                    StageTwoLinkField(
                        value = githubUrl,
                        placeholder = "https://github.com/yourname",
                        kind = LinkKind.GITHUB,
                        darkTheme = darkTheme,
                        background = fieldBackground,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onGithubChange
                    )
                }

                item {
                    StageTwoLinkField(
                        value = youtubeUrl,
                        placeholder = "https://youtube.com/@yourname",
                        kind = LinkKind.VIDEO,
                        darkTheme = darkTheme,
                        background = fieldBackground,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onYoutubeChange
                    )
                }

                item {
                    StageTwoLinkField(
                        value = portfolioUrl,
                        placeholder = "https://your-portfolio-link",
                        kind = LinkKind.WEB,
                        darkTheme = darkTheme,
                        background = fieldBackground,
                        borderColor = borderColor,
                        textPrimary = textPrimary,
                        textMuted = textMuted,
                        onValueChange = onPortfolioChange
                    )
                }

                if (error.isNotBlank()) {
                    item {
                        Text(
                            text = error,
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
                    .background(borderColor)
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 48.dp, end = 24.dp, top = 16.dp, bottom = 12.dp)
            ) {
                Button(
                    onClick = onContinue,
                    enabled = !saving,
                    modifier = Modifier.fillMaxWidth().height(52.dp),
                    shape = RoundedCornerShape(24.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = lavender,
                        contentColor = selectedText,
                        disabledContainerColor = lavender.copy(alpha = 0.55f),
                        disabledContentColor = selectedText.copy(alpha = 0.55f)
                    )
                ) {
                    Text(text = "Continue  →", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }

        if (skipConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissSkip,
                title = { Text("Skip profile setup?") },
                text = { Text("Your progress will be saved. You can return to Profile later and finish the setup.") },
                confirmButton = {
                    TextButton(onClick = onConfirmSkip, enabled = !saving) { Text("Skip for now") }
                },
                dismissButton = {
                    TextButton(onClick = onDismissSkip) { Text("Keep setting up") }
                }
            )
        }
    }
}

@Composable
private fun OnboardingStepProgress(
    current: Int,
    total: Int,
    darkTheme: Boolean,
    lavender: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 48.dp),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(total) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .background(
                        if (index < current) lavender
                        else if (darkTheme) Color(0xFF3A393E) else Color(0xFFE2E1E5)
                    )
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
    borderColor: Color,
    textPrimary: Color,
    textMuted: Color,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(13.dp))
            .background(background).border(1.dp, borderColor, RoundedCornerShape(13.dp)).padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        SearchGlyph(tint = textMuted, modifier = Modifier.size(21.dp))
        Spacer(modifier = Modifier.size(12.dp))
        BasicTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1f),
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text, imeAction = ImeAction.Search),
            textStyle = TextStyle(color = textPrimary, fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (value.isBlank()) Text(text = placeholder, color = textMuted, fontSize = 16.sp, maxLines = 1)
                innerTextField()
            }
        )
        if (value.isNotBlank()) {
            TextButton(
                onClick = onClear,
                contentPadding = PaddingValues(0.dp),
                modifier = Modifier.size(34.dp)
            ) { CloseGlyph(tint = textMuted, modifier = Modifier.size(18.dp)) }
        }
    }
}

@Composable
private fun StageTwoSkillChip(
    text: String,
    selected: Boolean,
    darkTheme: Boolean,
    lavender: Color,
    selectedText: Color,
    borderColor: Color,
    textPrimary: Color,
    onClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) lavender else if (darkTheme) Color(0xFF262629) else Color.White)
            .border(1.dp, if (selected) lavender else borderColor, RoundedCornerShape(22.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 17.dp, vertical = 9.dp)
    ) {
        Text(
            text = text,
            color = if (selected) selectedText else textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

private enum class LinkKind { GITHUB, VIDEO, WEB }

@Composable
private fun StageTwoLinkField(
    value: String,
    placeholder: String,
    kind: LinkKind,
    darkTheme: Boolean,
    background: Color,
    borderColor: Color,
    textPrimary: Color,
    textMuted: Color,
    onValueChange: (String) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth().height(56.dp).clip(RoundedCornerShape(13.dp))
            .background(background).border(1.dp, borderColor, RoundedCornerShape(13.dp)).padding(horizontal = 16.dp),
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
            textStyle = TextStyle(color = textPrimary, fontSize = 16.sp),
            decorationBox = { innerTextField ->
                if (value.isBlank()) Text(text = placeholder, color = textMuted, fontSize = 16.sp, maxLines = 1)
                innerTextField()
            }
        )
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
                val p = Path().apply { moveTo(size.width * 0.43f, size.height * 0.34f); lineTo(size.width * 0.43f, size.height * 0.66f); lineTo(size.width * 0.70f, size.height * 0.50f); close() }
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