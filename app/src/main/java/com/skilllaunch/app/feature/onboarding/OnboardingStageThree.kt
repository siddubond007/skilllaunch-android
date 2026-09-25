package com.skilllaunch.app.feature.onboarding

import android.app.DatePickerDialog
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skilllaunch.app.feature.auth.SkillLaunchBrand
import java.text.DateFormatSymbols
import java.util.Calendar

private val stageThreeAcademicStatuses = listOf(
    "High School",
    "Undergraduate",
    "Postgraduate",
    "Bootcamp / Cert",
    "Self-Taught",
    "Professional"
)

private val stageThreeAvailabilityOptions = listOf(
    "Part-time (< 20 hrs)",
    "Half-time (20-30 hrs)",
    "Full-time (40+ hrs)"
)

private data class StageThreeColors(
    val background: Color,
    val surface: Color,
    val accent: Color,
    val textPrimary: Color,
    val textMuted: Color,
    val border: Color,
    val accentText: Color,
    val error: Color
)

private fun stageThreeColors(darkTheme: Boolean) = if (darkTheme) {
    StageThreeColors(
        background = Color(0xFF1A1A1D),
        surface = Color(0xFF262629),
        accent = Color(0xFFD4C6FF),
        textPrimary = Color.White,
        textMuted = Color(0xFFAAA9AE),
        border = Color(0xFF454449),
        accentText = Color(0xFF17171A),
        error = Color(0xFFFF8A8A)
    )
} else {
    StageThreeColors(
        background = Color(0xFFF8F7FA),
        surface = Color.White,
        accent = Color(0xFFD4C6FF),
        textPrimary = Color(0xFF17171A),
        textMuted = Color(0xFF77767D),
        border = Color(0xFFD2D0D6),
        accentText = Color(0xFF17171A),
        error = Color(0xFFB91C1C)
    )
}

@Composable
internal fun OnboardingStageThree(
    academicStatus: String,
    graduationYear: String,
    availability: String,
    darkTheme: Boolean,
    error: String,
    saving: Boolean,
    skipConfirmation: Boolean,
    onBack: () -> Unit,
    onSkip: () -> Unit,
    onAcademicStatusChange: (String) -> Unit,
    onGraduationYearChange: (String) -> Unit,
    onAvailabilityChange: (String) -> Unit,
    onContinue: () -> Unit,
    onConfirmSkip: () -> Unit,
    onDismissSkip: () -> Unit
) {
    val colors = stageThreeColors(darkTheme)
    val context = androidx.compose.ui.platform.LocalContext.current
    var graduationMonth by remember { mutableIntStateOf(Calendar.MAY) }

    val displayedGraduation = graduationYear.toIntOrNull()?.let { year ->
        val monthName = DateFormatSymbols().months[graduationMonth]
            .takeIf { it.isNotBlank() }
            ?: "May"
        "$monthName $year"
    } ?: "Select month & year"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding()
    ) {
        /*
         * This chrome intentionally matches StudentSkillsSelection (Stage 2):
         * same 58dp row, same centered SkillLaunch brand, same back control,
         * same 24dp horizontal header padding, and the same 16dp rhythm around
         * the progress track.
         */
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp)
                .padding(horizontal = 24.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = onBack,
                enabled = !saving,
                modifier = Modifier.size(32.dp)
            ) {
                Text(
                    text = "<",
                    color = colors.textPrimary,
                    fontSize = 26.sp,
                    lineHeight = 26.sp,
                    fontWeight = FontWeight.Light
                )
            }

            Spacer(modifier = Modifier.weight(1f))

            SkillLaunchBrand(
                darkTheme = darkTheme,
                compact = true
            )

            Spacer(modifier = Modifier.weight(1f))
            Spacer(modifier = Modifier.size(32.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .clip(RoundedCornerShape(8.dp))
                .background(
                    if (darkTheme) Color(0xFF3A393E) else Color(0xFFD8D7DA)
                )
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth(0.75f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(colors.accent)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        /*
         * Same scroll container and content geometry as Stage 2.
         * Only the journey form content changes.
         */
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f)
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(
                    horizontal = 28.dp,
                    vertical = 12.dp
                ),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                item {
                    Text(
                        text = "What stage are you at?",
                        color = colors.textPrimary,
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
                        text = "Help clients understand your current standing and when you'll be ready for projects.",
                        color = colors.textMuted,
                        fontSize = 16.sp,
                        lineHeight = 21.sp
                    )
                }

                item {
                    StageThreeSectionLabel(
                        text = "CURRENT STATUS",
                        color = colors.textMuted
                    )
                }

                item {
                    Spacer(modifier = Modifier.height(1.dp))
                    AcademicStatusGrid(
                        selected = academicStatus,
                        accent = colors.accent,
                        accentText = colors.accentText,
                        surface = colors.surface,
                        textPrimary = colors.textPrimary,
                        border = colors.border,
                        enabled = !saving,
                        onSelect = onAcademicStatusChange
                    )
                }

                item {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = academicStatus in setOf(
                            "High School",
                            "Undergraduate",
                            "Postgraduate",
                            "Bootcamp / Cert"
                        ),
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        Column(
                            verticalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            StageThreeSectionLabel(
                                text = "EXPECTED GRADUATION",
                                color = colors.textMuted
                            )

                            GraduationInputShell(
                                displayText = displayedGraduation,
                                surface = colors.surface,
                                textPrimary = colors.textPrimary,
                                textSecondary = colors.textMuted,
                                border = colors.border,
                                enabled = !saving,
                                onClick = {
                                    val initialYear = graduationYear.toIntOrNull() ?: 2027

                                    DatePickerDialog(
                                        context,
                                        { _, year, month, _ ->
                                            graduationMonth = month
                                            onGraduationYearChange(year.toString())
                                        },
                                        initialYear,
                                        graduationMonth,
                                        1
                                    ).show()
                                }
                            )
                        }
                    }
                }

                item {
                    StageThreeSectionLabel(
                        text = "WORK AVAILABILITY",
                        color = colors.textMuted
                    )
                }

                item {
                    WorkAvailabilityCards(
                        selected = availability,
                        accent = colors.accent,
                        surface = colors.surface,
                        textPrimary = colors.textPrimary,
                        border = colors.border,
                        enabled = !saving,
                        onSelect = onAvailabilityChange
                    )
                }

                if (error.isNotBlank()) {
                    item {
                        Text(
                            text = error,
                            color = colors.error,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        /*
         * Exact Stage 2 CTA geometry: full width, 24dp outer padding,
         * pill-shaped button, anchored after the weighted content region.
         */
        Button(
            onClick = onContinue,
            enabled = !saving,
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            shape = RoundedCornerShape(28.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.accent,
                contentColor = colors.accentText,
                disabledContainerColor = colors.accent.copy(alpha = 0.55f),
                disabledContentColor = colors.accentText.copy(alpha = 0.65f)
            ),
            contentPadding = PaddingValues(
                horizontal = 24.dp,
                vertical = 0.dp
            )
        ) {
            Text(
                text = "Continue  →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        if (skipConfirmation) {
            AlertDialog(
                onDismissRequest = onDismissSkip,
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
                        onClick = onConfirmSkip,
                        enabled = !saving
                    ) {
                        Text("Skip for now")
                    }
                },
                dismissButton = {
                    TextButton(onClick = onDismissSkip) {
                        Text("Keep setting up")
                    }
                }
            )
        }
    }
}

@Composable
private fun StageThreeSectionLabel(
    text: String,
    color: Color
) {
    Text(
        text = text,
        color = color,
        fontSize = 10.sp,
        lineHeight = 12.sp,
        fontWeight = FontWeight.Bold,
        letterSpacing = 0.8.sp
    )
}

@Composable
private fun AcademicStatusGrid(
    selected: String,
    accent: Color,
    accentText: Color,
    surface: Color,
    textPrimary: Color,
    border: Color,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stageThreeAcademicStatuses.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                row.forEach { option ->
                    AcademicStatusChip(
                        text = option,
                        selected = selected == option,
                        accent = accent,
                        accentText = accentText,
                        surface = surface,
                        textPrimary = textPrimary,
                        border = border,
                        enabled = enabled,
                        onClick = { onSelect(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun AcademicStatusChip(
    text: String,
    selected: Boolean,
    accent: Color,
    accentText: Color,
    surface: Color,
    textPrimary: Color,
    border: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val shape = RoundedCornerShape(24.dp)

    Box(
        modifier = modifier
            .height(48.dp)
            .clip(shape)
            .background(if (selected) accent else surface)
            .border(
                width = 1.dp,
                color = if (selected) accent else border,
                shape = shape
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) accentText else textPrimary,
            fontSize = 13.sp,
            lineHeight = 16.sp,
            fontWeight = if (selected) FontWeight.SemiBold else FontWeight.Medium,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
    }
}

@Composable
private fun GraduationInputShell(
    displayText: String,
    surface: Color,
    textPrimary: Color,
    textSecondary: Color,
    border: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(surface)
            .border(
                width = 1.dp,
                color = border,
                shape = RoundedCornerShape(30.dp)
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarGlyph(
            tint = textSecondary,
            modifier = Modifier.size(21.dp)
        )

        Spacer(modifier = Modifier.size(12.dp))

        Text(
            text = displayText,
            color = textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun WorkAvailabilityCards(
    selected: String,
    accent: Color,
    surface: Color,
    textPrimary: Color,
    border: Color,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        stageThreeAvailabilityOptions.forEach { option ->
            AvailabilityCard(
                text = option,
                selected = selected == option,
                accent = accent,
                surface = surface,
                textPrimary = textPrimary,
                border = border,
                enabled = enabled,
                onClick = { onSelect(option) }
            )
        }
    }
}

@Composable
private fun AvailabilityCard(
    text: String,
    selected: Boolean,
    accent: Color,
    surface: Color,
    textPrimary: Color,
    border: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(18.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(shape)
            .background(
                if (selected) {
                    accent.copy(alpha = 0.10f)
                } else {
                    surface
                }
            )
            .border(
                width = 1.dp,
                color = if (selected) accent else border,
                shape = shape
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = text,
            modifier = Modifier.weight(1f),
            color = textPrimary,
            fontSize = 15.sp,
            fontWeight = FontWeight.SemiBold
        )

        if (selected) {
            RadioGlyph(
                tint = accent,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun CalendarGlyph(
    tint: Color,
    modifier: Modifier
) {
    Canvas(modifier) {
        val stroke = 1.8.dp.toPx()

        drawRoundRect(
            color = tint,
            topLeft = Offset(size.width * 0.16f, size.height * 0.24f),
            size = androidx.compose.ui.geometry.Size(
                size.width * 0.68f,
                size.height * 0.60f
            ),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(
                size.width * 0.08f,
                size.width * 0.08f
            ),
            style = Stroke(width = stroke)
        )

        drawLine(
            color = tint,
            start = Offset(size.width * 0.28f, size.height * 0.13f),
            end = Offset(size.width * 0.28f, size.height * 0.34f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        drawLine(
            color = tint,
            start = Offset(size.width * 0.72f, size.height * 0.13f),
            end = Offset(size.width * 0.72f, size.height * 0.34f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )

        drawLine(
            color = tint,
            start = Offset(size.width * 0.18f, size.height * 0.38f),
            end = Offset(size.width * 0.82f, size.height * 0.38f),
            strokeWidth = stroke
        )
    }
}

@Composable
private fun RadioGlyph(
    tint: Color,
    modifier: Modifier
) {
    Canvas(modifier) {
        drawCircle(
            color = tint,
            radius = size.minDimension * 0.34f,
            center = Offset(size.width * 0.50f, size.height * 0.50f),
            style = Stroke(width = 1.8.dp.toPx())
        )

        drawCircle(
            color = tint,
            radius = size.minDimension * 0.16f,
            center = Offset(size.width * 0.50f, size.height * 0.50f)
        )
    }
}
