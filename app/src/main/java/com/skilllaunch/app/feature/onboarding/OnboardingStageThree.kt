package com.skilllaunch.app.feature.onboarding

import android.app.DatePickerDialog
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

private data class StageThreeSemanticColors(
    val background: Color,
    val surface: Color,
    val primaryAccent: Color,
    val contrastOnPrimary: Color,
    val textPrimary: Color,
    val textSecondary: Color,
    val borderSubtle: Color,
    val error: Color
)

private fun stageThreeSemanticColors(darkTheme: Boolean) = if (darkTheme) {
    StageThreeSemanticColors(
        background = Color(0xFF1A1A1D),
        surface = Color(0xFF262629),
        primaryAccent = Color(0xFFD4C6FF),
        contrastOnPrimary = Color(0xFF17171A),
        textPrimary = Color.White,
        textSecondary = Color(0xFFAAA9AE),
        borderSubtle = Color(0xFF454449),
        error = Color(0xFFFF8A8A)
    )
} else {
    StageThreeSemanticColors(
        background = Color(0xFFF8F7FA),
        surface = Color.White,
        primaryAccent = Color(0xFF4338CA),
        contrastOnPrimary = Color.White,
        textPrimary = Color(0xFF17171A),
        textSecondary = Color(0xFF77767D),
        borderSubtle = Color(0xFFD2D0D6),
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
    val colors = stageThreeSemanticColors(darkTheme)
    val context = androidx.compose.ui.platform.LocalContext.current

    var graduationMonth by remember { mutableIntStateOf(Calendar.MAY) }

    val displayedGraduation = graduationYear.toIntOrNull()?.let { year ->
        val monthName = DateFormatSymbols().months[graduationMonth]
            .takeIf { it.isNotBlank() }
            ?: "May"
        "$monthName $year"
    } ?: "May 2027"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colors.background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        /*
         * Stage 2 onboarding scaffolding:
         * Profile setup | Skip for now
         * 4-segment progress indicator
         */
        StageThreeTopBar(
            textPrimary = colors.textPrimary,
            textSecondary = colors.textSecondary,
            saving = saving,
            onSkip = onSkip
        )

        Spacer(modifier = Modifier.height(16.dp))

        StageThreeProgress(
            activeColor = colors.primaryAccent,
            inactiveColor = colors.borderSubtle
        )

        Spacer(modifier = Modifier.height(16.dp))

        /*
         * Body intentionally remains non-scrollable so the footer can use the
         * same weight-based pinning pattern as the onboarding scaffolding.
         */
        Text(
            text = "What stage are you at?",
            color = colors.textPrimary,
            style = TextStyle(
                fontFamily = FontFamily.Serif,
                fontSize = 32.sp,
                lineHeight = 36.sp,
                fontWeight = FontWeight.ExtraBold,
                letterSpacing = (-0.6).sp
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "Help clients understand your current academic standing and when you’re available to work.",
            color = colors.textSecondary,
            fontSize = 15.sp,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        StageThreeSectionLabel(
            text = "ACADEMIC STATUS",
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        AcademicStatusGrid(
            selected = academicStatus,
            primaryAccent = colors.primaryAccent,
            contrastOnPrimary = colors.contrastOnPrimary,
            surface = colors.surface,
            textPrimary = colors.textPrimary,
            borderSubtle = colors.borderSubtle,
            enabled = !saving,
            onSelect = onAcademicStatusChange
        )

        Spacer(modifier = Modifier.height(18.dp))

        StageThreeSectionLabel(
            text = "EXPECTED GRADUATION",
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        GraduationInputShell(
            displayText = displayedGraduation,
            surface = colors.surface,
            textPrimary = colors.textPrimary,
            textSecondary = colors.textSecondary,
            borderSubtle = colors.borderSubtle,
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

        Spacer(modifier = Modifier.height(18.dp))

        StageThreeSectionLabel(
            text = "WORK AVAILABILITY",
            color = colors.textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        WorkAvailabilityCards(
            selected = availability,
            primaryAccent = colors.primaryAccent,
            surface = colors.surface,
            textPrimary = colors.textPrimary,
            borderSubtle = colors.borderSubtle,
            enabled = !saving,
            onSelect = onAvailabilityChange
        )

        if (error.isNotBlank()) {
            Text(
                text = error,
                modifier = Modifier.padding(top = 8.dp),
                color = colors.error,
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        /*
         * Stage 3 footer:
         * Step metadata + text back action, immediately above the CTA.
         */
        StageThreeBottomRow(
            textSecondary = colors.textSecondary,
            saving = saving,
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onContinue,
            enabled = !saving,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
                .padding(bottom = 0.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = colors.primaryAccent,
                contentColor = colors.contrastOnPrimary,
                disabledContainerColor = colors.primaryAccent.copy(alpha = 0.55f),
                disabledContentColor = colors.contrastOnPrimary.copy(alpha = 0.70f)
            ),
            contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
        ) {
            Text(
                text = "Continue  →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

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
private fun StageThreeTopBar(
    textPrimary: Color,
    textSecondary: Color,
    saving: Boolean,
    onSkip: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Profile setup",
            color = textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onSkip,
            enabled = !saving,
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
        ) {
            Text(
                text = "Skip for now",
                color = textSecondary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        }
    }
}

@Composable
private fun StageThreeProgress(
    activeColor: Color,
    inactiveColor: Color
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(5.dp)
    ) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(5.dp)
                    .clip(RoundedCornerShape(50))
                    .background(
                        if (index < 3) activeColor else inactiveColor
                    )
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
    primaryAccent: Color,
    contrastOnPrimary: Color,
    surface: Color,
    textPrimary: Color,
    borderSubtle: Color,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stageThreeAcademicStatuses.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { option ->
                    StageThreeAcademicChip(
                        text = option,
                        selected = selected == option,
                        primaryAccent = primaryAccent,
                        contrastOnPrimary = contrastOnPrimary,
                        surface = surface,
                        textPrimary = textPrimary,
                        borderSubtle = borderSubtle,
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
private fun StageThreeAcademicChip(
    text: String,
    selected: Boolean,
    primaryAccent: Color,
    contrastOnPrimary: Color,
    surface: Color,
    textPrimary: Color,
    borderSubtle: Color,
    enabled: Boolean,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(shape)
            .background(
                if (selected) primaryAccent else surface
            )
            .border(
                width = 1.dp,
                color = if (selected) primaryAccent else borderSubtle,
                shape = shape
            )
            .clickable(
                enabled = enabled,
                onClick = onClick
            )
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) {
                contrastOnPrimary
            } else {
                textPrimary
            },
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold,
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
    borderSubtle: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(surface)
            .border(
                width = 1.dp,
                color = borderSubtle,
                shape = RoundedCornerShape(16.dp)
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
    primaryAccent: Color,
    surface: Color,
    textPrimary: Color,
    borderSubtle: Color,
    enabled: Boolean,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        stageThreeAvailabilityOptions.forEach { option ->
            AvailabilityCard(
                text = option,
                selected = selected == option,
                primaryAccent = primaryAccent,
                surface = surface,
                textPrimary = textPrimary,
                borderSubtle = borderSubtle,
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
    primaryAccent: Color,
    surface: Color,
    textPrimary: Color,
    borderSubtle: Color,
    enabled: Boolean,
    onClick: () -> Unit
) {
    val shape = RoundedCornerShape(16.dp)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(shape)
            .background(
                if (selected) {
                    primaryAccent.copy(alpha = 0.10f)
                } else {
                    surface
                }
            )
            .border(
                width = 1.dp,
                color = if (selected) primaryAccent else borderSubtle,
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
                tint = primaryAccent,
                modifier = Modifier.size(22.dp)
            )
        }
    }
}

@Composable
private fun StageThreeBottomRow(
    textSecondary: Color,
    saving: Boolean,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = "Step 3 of 4 • Journey",
            color = textSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.SemiBold
        )

        Spacer(modifier = Modifier.weight(1f))

        TextButton(
            onClick = onBack,
            enabled = !saving,
            contentPadding = PaddingValues(horizontal = 0.dp, vertical = 2.dp)
        ) {
            Text(
                text = "← Back to previous step",
                color = textSecondary,
                fontSize = 13.sp,
                fontWeight = FontWeight.Medium
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
