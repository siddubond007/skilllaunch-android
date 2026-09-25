package com.skilllaunch.app.feature.onboarding

import android.app.DatePickerDialog
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
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
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
import androidx.compose.ui.graphics.Canvas
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import com.skilllaunch.app.feature.auth.SkillLaunchBrand
import java.util.Calendar

private val academicStatusOptions = listOf(
    "Freshman",
    "Sophomore",
    "Junior",
    "Senior",
    "Graduated",
    "Self-Taught"
)

private val availabilityOptions = listOf(
    "Part-time",
    "Half-time",
    "Full-time"
)

@Composable
internal fun OnboardingStageThree(
    academicStatus: String,
    graduationYear: String,
    availability: String,
    darkTheme: Boolean,
    error: String,
    saving: Boolean,
    onBack: () -> Unit,
    onAcademicStatusChange: (String) -> Unit,
    onGraduationYearChange: (String) -> Unit,
    onAvailabilityChange: (String) -> Unit,
    onContinue: () -> Unit
) {
    val background = if (darkTheme) Color(0xFF1A1A1D) else Color(0xFFF7F7FB)
    val surface = if (darkTheme) Color(0xFF262629) else Color.White
    val primaryAccent = if (darkTheme) Color(0xFFD4C6FF) else Color(0xFF4338CA)
    val textPrimary = if (darkTheme) Color.White else Color(0xFF0F172A)
    val textSecondary = if (darkTheme) Color(0xFFA0A0A5) else Color(0xFF64748B)
    val borderSubtle = if (darkTheme) Color(0xFF3F3F46) else Color(0xFFE2E8F0)
    val selectedText = if (darkTheme) Color(0xFF17171A) else Color.White

    var graduationMonth by remember { mutableIntStateOf(Calendar.MAY) }

    val displayedGraduation = graduationYear.toIntOrNull()?.let { year ->
        val monthName = java.text.DateFormatSymbols().months[graduationMonth]
            .takeIf { it.isNotBlank() }
            ?: "May"
        "$monthName $year"
    } ?: "May 2027"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(background)
            .systemBarsPadding()
            .padding(horizontal = 24.dp)
    ) {
        StageThreeTopBar(
            darkTheme = darkTheme,
            textPrimary = textPrimary,
            onBack = onBack
        )

        Spacer(modifier = Modifier.height(16.dp))

        StageThreeProgress(
            darkTheme = darkTheme,
            activeColor = primaryAccent
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Where are you in your journey?",
            color = textPrimary,
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
            text = "Help clients understand your current academic standing and when you are available to work.",
            color = textSecondary,
            fontSize = 15.sp,
            lineHeight = 21.sp
        )

        Spacer(modifier = Modifier.height(22.dp))

        StageThreeSectionLabel(
            text = "ACADEMIC STATUS",
            color = textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        AcademicStatusGrid(
            selected = academicStatus,
            darkTheme = darkTheme,
            surface = surface,
            primaryAccent = primaryAccent,
            textPrimary = textPrimary,
            selectedText = selectedText,
            borderSubtle = borderSubtle,
            onSelect = onAcademicStatusChange
        )

        Spacer(modifier = Modifier.height(20.dp))

        StageThreeSectionLabel(
            text = "EXPECTED GRADUATION",
            color = textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        GraduationInputShell(
            displayText = displayedGraduation,
            darkTheme = darkTheme,
            surface = surface,
            textPrimary = textPrimary,
            borderSubtle = borderSubtle,
            onClick = {
                val calendar = Calendar.getInstance()
                val initialYear = graduationYear.toIntOrNull() ?: 2027
                DatePickerDialog(
                    LocalContext.current,
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

        Spacer(modifier = Modifier.height(20.dp))

        StageThreeSectionLabel(
            text = "WORK AVAILABILITY",
            color = textSecondary
        )

        Spacer(modifier = Modifier.height(10.dp))

        availabilityOptions.forEach { option ->
            AvailabilityCard(
                text = option,
                selected = availability == option,
                darkTheme = darkTheme,
                surface = surface,
                primaryAccent = primaryAccent,
                textPrimary = textPrimary,
                borderSubtle = borderSubtle,
                onClick = { onAvailabilityChange(option) }
            )
            Spacer(modifier = Modifier.height(10.dp))
        }

        if (error.isNotBlank()) {
            Text(
                text = error,
                modifier = Modifier.padding(top = 2.dp),
                color = if (darkTheme) Color(0xFFFF8A8A) else Color(0xFFB91C1C),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            enabled = !saving,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .padding(bottom = 24.dp),
            shape = RoundedCornerShape(16.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = primaryAccent,
                contentColor = selectedText,
                disabledContainerColor = primaryAccent.copy(alpha = 0.55f),
                disabledContentColor = selectedText.copy(alpha = 0.70f)
            ),
            contentPadding = PaddingValues(horizontal = 24.dp)
        ) {
            Text(
                text = "Continue  →",
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
private fun StageThreeTopBar(
    darkTheme: Boolean,
    textPrimary: Color,
    onBack: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(
            onClick = onBack,
            modifier = Modifier.size(32.dp)
        ) {
            Text(
                text = "<",
                color = textPrimary,
                fontSize = 26.sp,
                lineHeight = 26.sp,
                fontWeight = FontWeight.Light,
                textAlign = TextAlign.Center
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
}

@Composable
private fun StageThreeProgress(
    darkTheme: Boolean,
    activeColor: Color
) {
    val inactiveColor = if (darkTheme) Color(0xFF3A393E) else Color(0xFFD8D7DA)

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        repeat(4) { index ->
            Box(
                modifier = Modifier
                    .weight(1f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(8.dp))
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
    darkTheme: Boolean,
    surface: Color,
    primaryAccent: Color,
    textPrimary: Color,
    selectedText: Color,
    borderSubtle: Color,
    onSelect: (String) -> Unit
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        academicStatusOptions.chunked(2).forEach { row ->
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                row.forEach { option ->
                    StageThreeSelectionChip(
                        text = option,
                        selected = selected == option,
                        darkTheme = darkTheme,
                        surface = surface,
                        primaryAccent = primaryAccent,
                        textPrimary = textPrimary,
                        selectedText = selectedText,
                        borderSubtle = borderSubtle,
                        onClick = { onSelect(option) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}

@Composable
private fun StageThreeSelectionChip(
    text: String,
    selected: Boolean,
    darkTheme: Boolean,
    surface: Color,
    primaryAccent: Color,
    textPrimary: Color,
    selectedText: Color,
    borderSubtle: Color,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val shape = RoundedCornerShape(14.dp)

    Box(
        modifier = modifier
            .height(50.dp)
            .clip(shape)
            .background(if (selected) primaryAccent else surface)
            .border(
                width = 1.dp,
                color = if (selected) primaryAccent else borderSubtle,
                shape = shape
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 12.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = text,
            color = if (selected) selectedText else textPrimary,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
private fun GraduationInputShell(
    displayText: String,
    darkTheme: Boolean,
    surface: Color,
    textPrimary: Color,
    borderSubtle: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(surface)
            .border(1.dp, borderSubtle, RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CalendarGlyph(
            tint = if (darkTheme) Color(0xFFA0A0A5) else Color(0xFF64748B),
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
private fun AvailabilityCard(
    text: String,
    selected: Boolean,
    darkTheme: Boolean,
    surface: Color,
    primaryAccent: Color,
    textPrimary: Color,
    borderSubtle: Color,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(58.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(surface)
            .border(
                1.dp,
                if (selected) primaryAccent else borderSubtle,
                RoundedCornerShape(16.dp)
            )
            .clickable(onClick = onClick)
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
