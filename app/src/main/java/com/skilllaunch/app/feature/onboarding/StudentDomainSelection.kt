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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TextButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.layout.ContentScale
import com.skilllaunch.app.feature.auth.SkillLaunchBrand
import coil3.compose.AsyncImage
import com.skilllaunch.app.R

internal data class DomainOption(
    val id: String,
    val category: String,
    val title: String,
    val imageRes: Int
)

private val FEATURED_DOMAIN_IDS = listOf(
    "web-development",
    "mobile-app-development",
    "ai-ml-data-science",
    "design-creative",
    "software-it-services",
    "digital-marketing-seo"
)

internal val STUDENT_GIG_DOMAIN_CATALOG: List<DomainOption> = listOf(
    DomainOption("web-development", "DEVELOPMENT", "Web Development", R.raw.student_domain_web),
    DomainOption("software-it-services", "DEVELOPMENT", "Software & IT Services", R.raw.student_domain_web),
    DomainOption("mobile-app-development", "DEVELOPMENT", "Mobile App Development", R.raw.student_domain_mobile),
    DomainOption("ai-ml-data-science", "TECHNOLOGY", "AI, Machine Learning & Data Science", R.raw.student_domain_ai),
    DomainOption("design-creative", "DESIGN", "Design & Creative", R.raw.student_domain_design),
    DomainOption("photography-image-editing", "DESIGN", "Photography & Image Editing", R.raw.student_domain_design),
    DomainOption("video-audio-animation", "CREATIVE", "Video, Audio & Animation", R.raw.student_domain_video),
    DomainOption("social-media-community", "MARKETING", "Social Media & Community", R.raw.student_domain_marketing),
    DomainOption("digital-marketing-seo", "MARKETING", "Digital Marketing & SEO", R.raw.student_domain_marketing),
    DomainOption("e-commerce-retail", "RETAIL", "E-commerce & Retail", R.raw.student_domain_business),
    DomainOption("writing-content-creation", "CONTENT", "Writing & Content Creation", R.raw.student_domain_content),
    DomainOption("translation-transcription", "CONTENT", "Translation & Transcription", R.raw.student_domain_content),
    DomainOption("gaming-esports", "GAMING", "Gaming & Esports", R.raw.student_domain_gaming),
    DomainOption("admin-support-operations", "SERVICES", "Admin, Support & Operations", R.raw.student_domain_business),
    DomainOption("business-finance-hr", "BUSINESS", "Business, Finance & HR", R.raw.student_domain_business),
    DomainOption("legal-compliance", "SECURITY", "Legal & Compliance", R.raw.student_domain_security),
    DomainOption("engineering-architecture-3d", "DESIGN", "Engineering, Architecture & 3D", R.raw.student_domain_design),
    DomainOption("education-tutoring-coaching", "CONTENT", "Education, Tutoring & Coaching", R.raw.student_domain_content),
    DomainOption("events-travel-local-services", "SERVICES", "Events, Travel & Local Services", R.raw.student_domain_video),
    DomainOption("telecommunications-networking", "INFRASTRUCTURE", "Telecommunications & Networking", R.raw.student_domain_mobile),
    DomainOption("health-wellness", "SERVICES", "Health & Wellness", R.raw.student_domain_business),
    DomainOption("manufacturing-product-development", "BUSINESS", "Manufacturing & Product Development", R.raw.student_domain_business),
    DomainOption("product-management-operations", "BUSINESS", "Product Management & Operations", R.raw.student_domain_business),
    DomainOption("market-research-consumer-insights", "RESEARCH", "Market Research & Consumer Insights", R.raw.student_domain_ai),
    DomainOption("public-relations-communications", "MARKETING", "Public Relations & Communications", R.raw.student_domain_marketing),
    DomainOption("career-professional-services", "CONTENT", "Career & Professional Services", R.raw.student_domain_content),
    DomainOption("government-nonprofit-services", "SERVICES", "Government & Nonprofit Services", R.raw.student_domain_business),
    DomainOption("real-estate-property-services", "BUSINESS", "Real Estate & Property Services", R.raw.student_domain_business),
    DomainOption("travel-hospitality", "SERVICES", "Travel & Hospitality", R.raw.student_domain_business),
    DomainOption("food-culinary-services", "SERVICES", "Food & Culinary Services", R.raw.student_domain_business),
    DomainOption("beauty-personal-care", "DESIGN", "Beauty & Personal Care", R.raw.student_domain_design),
    DomainOption("fashion-jewelry-accessories", "DESIGN", "Fashion, Jewelry & Accessories", R.raw.student_domain_design),
    DomainOption("scientific-technical-research", "RESEARCH", "Scientific & Technical Research", R.raw.student_domain_ai),
    DomainOption("freight-delivery-transportation", "INFRASTRUCTURE", "Freight, Delivery & Transportation", R.raw.student_domain_business),
    DomainOption("agriculture-environmental-services", "SERVICES", "Agriculture & Environmental Services", R.raw.student_domain_business),
    DomainOption("3d-printing-digital-fabrication", "INFRASTRUCTURE", "3D Printing & Digital Fabrication", R.raw.student_domain_cloud),
    DomainOption("consulting-professional-advisory", "BUSINESS", "Consulting & Professional Advisory", R.raw.student_domain_business),
    DomainOption("personal-development-hobbies", "OTHER", "Personal Development & Hobbies", R.raw.student_domain_other)
)

internal val STUDENT_GIG_FEATURED_DOMAINS: List<DomainOption> =
    FEATURED_DOMAIN_IDS.mapNotNull { id ->
        STUDENT_GIG_DOMAIN_CATALOG.firstOrNull { it.id == id }
    }

internal val studentGigDomainNames = STUDENT_GIG_DOMAIN_CATALOG.map { it.title }.toSet()

internal val studentGigDomainSkills = mapOf(
    "Web Development" to listOf("Frontend Development", "Backend Development", "Full Stack Development", "CMS & Website Builders", "Web Management", "Cloud & DevOps"),
    "Software & IT Services" to listOf("Desktop & OS Development", "Scripting & Automation", "Web Management", "Cybersecurity", "Software Testing", "API Integration"),
    "Mobile App Development" to listOf("Android Development", "iOS Development", "Cross-Platform", "App Management & Store", "Mobile UI", "Firebase Integration"),
    "AI, Machine Learning & Data Science" to listOf("Artificial Intelligence & LLMs", "AI Agents & Chatbots", "Machine Learning & Deep Learning", "Computer Vision & Audio", "Data Science & Analytics", "Data Visualization"),
    "Design & Creative" to listOf("Graphic & Visual Design", "Branding & Logos", "UI / UX & Web Design", "Illustration & Art", "Presentations & Typography", "Fashion & Merchandise"),
    "Photography & Image Editing" to listOf("Photography Services", "Image Editing & Retouching", "Product Photography", "Portrait Photography", "Photo Manipulation", "Color Correction"),
    "Video, Audio & Animation" to listOf("Video Editing & Post-Production", "Short-Form Video", "Animation & Motion Graphics", "Audio Production & Editing", "Voice Over & Acting", "Podcast Production"),
    "Social Media & Community" to listOf("Social Media Management", "Community Management", "Graphics for Socials", "Content Scheduling", "Community Growth", "Social Analytics"),
    "Digital Marketing & SEO" to listOf("Marketing Strategy", "Search Engine Optimization (SEO)", "Paid Advertising (PPC)", "PR & Outreach", "Lead Generation", "Campaign Planning"),
    "E-commerce & Retail" to listOf("Amazon & Marketplaces", "Shopify & Stores", "Product Catalog", "Store Optimization", "Marketplace Management", "Retail Support"),
    "Writing & Content Creation" to listOf("Content & Blog Writing", "Copywriting & Sales", "Technical & Academic Writing", "Creative Writing & Scripts", "Editing & Proofreading", "Product Descriptions"),
    "Translation & Transcription" to listOf("Translation & Localization", "Transcription & Subtitles", "Document Translation", "App Localization", "Captioning", "PDF-to-text Conversion"),
    "Gaming & Esports" to listOf("Game Development & Programming", "Game Art & Level Design", "AR, VR & Metaverse", "Esports, Coaching & Streaming", "Unity", "Unreal Engine"),
    "Admin, Support & Operations" to listOf("Virtual Assistance & Admin", "Data Entry", "Customer Support", "Operations Support", "Spreadsheet Support", "Research Assistance"),
    "Business, Finance & HR" to listOf("Finance, Accounting & Trading", "HR & Recruitment", "Business Consulting & Project Mgmt", "Sales & Lead Generation", "Email Marketing", "Business Analysis"),
    "Legal & Compliance" to listOf("Contracts & Documentation", "Research & IP", "Compliance Support", "Privacy Documentation", "Business Legal Support", "Legal Research"),
    "Engineering, Architecture & 3D" to listOf("Architecture & 3D Modeling", "Mechanical & Industrial Engineering", "Electrical & Hardware Engineering", "Civil Engineering", "CAD Design", "3D Visualization"),
    "Education, Tutoring & Coaching" to listOf("Academic Tutoring", "Test & Exam Preparation", "Career Coaching & Personal Branding", "Life Coaching & Wellness", "Programming Tutoring", "Study Mentoring"),
    "Events, Travel & Local Services" to listOf("Drones & Aerial Mapping", "Events & Travel Planning", "Real Estate Operations", "Event Support", "Travel Research", "Local Technical Support"),
    "Telecommunications & Networking" to listOf("Network Setup", "VoIP & Communication Systems", "Network Security", "Domain & Hosting Services", "Wi-Fi Setup", "VPN Setup"),
    "Health & Wellness" to listOf("Fitness & Training", "Nutrition & Meal Planning", "Mental Wellness & Lifestyle", "Sports & Performance", "Workout Planning", "Mindfulness"),
    "Manufacturing & Product Development" to listOf("Product Design", "CAD & Manufacturing Documentation", "3D Printing & Prototyping", "Packaging & Production Assets", "Sourcing & Supplier Support", "Prototype Design"),
    "Product Management & Operations" to listOf("Product Strategy", "Project Management", "Process & Operations", "No-Code & Productivity Systems", "MVP Planning", "Agile Planning"),
    "Market Research & Consumer Insights" to listOf("Market Research", "Competitor Research", "Customer Research", "Business Intelligence Research", "Survey Research", "Market Sizing"),
    "Public Relations & Communications" to listOf("Public Relations", "Corporate Communications", "Influencer & Creator Relations", "Press Outreach", "Media Outreach", "Communication Planning"),
    "Career & Professional Services" to listOf("Resume & CV", "LinkedIn & Professional Profiles", "Job Search Support", "Interview Preparation", "Portfolio Review", "Personal Branding"),
    "Government & Nonprofit Services" to listOf("Government Research", "Nonprofit Operations", "Grant Research", "Public Documentation", "Community Programs", "Policy Research"),
    "Real Estate & Property Services" to listOf("Property Listing Support", "Real Estate Marketing", "Property Research", "Virtual Staging", "Listing Management", "Property Photography"),
    "Travel & Hospitality" to listOf("Travel Planning", "Hospitality Support", "Itinerary Design", "Guest Experience", "Hotel Content", "Tourism Research"),
    "Food & Culinary Services" to listOf("Recipe Development", "Menu Design", "Food Photography", "Food Content", "Culinary Research", "Restaurant Support"),
    "Beauty & Personal Care" to listOf("Makeup", "Hair & Styling", "Skincare", "Beauty Content", "Personal Grooming", "Beauty Branding"),
    "Fashion, Jewelry & Accessories" to listOf("Fashion Illustration", "Clothing Design", "Jewelry Design", "Merchandise Design", "Tech Packs", "Pattern Design"),
    "Scientific & Technical Research" to listOf("Scientific Research", "Technical Analysis", "Literature Review", "Research Documentation", "Data Analysis", "Technical Writing"),
    "Freight, Delivery & Transportation" to listOf("Logistics Support", "Delivery Operations", "Shipping Coordination", "Route Planning", "Transport Research", "Inventory Support"),
    "Agriculture & Environmental Services" to listOf("Agriculture Research", "Environmental Research", "Sustainability Planning", "Farm Support", "Ecological Analysis", "Environmental Documentation"),
    "3D Printing & Digital Fabrication" to listOf("3D Modeling for Fabrication", "Digital Fabrication", "STL Preparation", "CNC Design Files", "Laser Cutting Design", "Prototype Modeling"),
    "Consulting & Professional Advisory" to listOf("Business Consulting", "Technology Consulting", "Creative Consulting", "Strategy", "Operations Advisory", "Solution Assessment"),
    "Personal Development & Hobbies" to listOf("Personal Development", "Creative Hobbies", "Hobby Instruction", "Productivity Coaching", "Creative Mentoring", "Beginner Mentoring")
)

@Composable
internal fun StudentDomainSelection(
    selectedDomain: String,
    darkTheme: Boolean,
    error: String,
    saving: Boolean,
    skipConfirmation: Boolean,
    onBack: () -> Unit,
    onSelectDomain: (String) -> Unit,
    onContinue: () -> Unit,
    onConfirmSkip: () -> Unit,
    onDismissSkip: () -> Unit
) {
    var query by remember { mutableStateOf("") }
    val normalizedQuery = query.trim().lowercase()

    val filteredDomains = remember(normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            STUDENT_GIG_FEATURED_DOMAINS
        } else {
            STUDENT_GIG_DOMAIN_CATALOG.filter { domain ->
                domain.title.contains(normalizedQuery, ignoreCase = true) ||
                    domain.category.contains(normalizedQuery, ignoreCase = true) ||
                    domain.id.replace('-', ' ').contains(normalizedQuery, ignoreCase = true)
            }
        }
    }

    val pageBackground = if (darkTheme) Color(0xFF1A1A1D) else Color(0xFFF8F7FA)
    val searchBackground = if (darkTheme) Color(0xFF262629) else Color(0xFFE8E7EA)
    val cardBackground = if (darkTheme) Color(0xFF303034) else Color(0xFFE0DFE2)
    val textPrimary = if (darkTheme) Color.White else Color(0xFF17171A)
    val textMuted = if (darkTheme) Color(0xFFAAA9AE) else Color(0xFF77767D)
    val lavender = Color(0xFFD4C6FF)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(pageBackground)
            .safeDrawingPadding(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(58.dp),
            contentAlignment = Alignment.Center
        ) {
            IconButton(
                onClick = onBack,
                modifier = Modifier.align(Alignment.CenterStart)
            ) {
                Text(
                    text = "‹",
                    color = textPrimary,
                    fontSize = 34.sp,
                    lineHeight = 34.sp,
                    fontWeight = FontWeight.Light
                )
            }
            SkillLaunchBrand(
                darkTheme = darkTheme,
                compact = true
            )
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
                    .fillMaxWidth(0.25f)
                    .height(4.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(lavender)
            )
        }

        Column(
            modifier = Modifier.padding(horizontal = 28.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "What's your\nsuperpower?",
                color = textPrimary,
                style = TextStyle(
                    fontFamily = FontFamily.Serif,
                    fontSize = 40.sp,
                    lineHeight = 43.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = (-0.9).sp
                )
            )
            Text(
                text = "Choose your primary domain.",
                color = textMuted,
                fontSize = 16.sp,
                lineHeight = 21.sp
            )
        }

        DomainSearchField(
            value = query,
            darkTheme = darkTheme,
            onValueChange = { query = it },
            onClear = { query = "" }
        )

        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentPadding = PaddingValues(horizontal = 28.dp, vertical = 2.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            filteredDomains.chunked(2).forEach { row ->
                item(key = row.joinToString("|")) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { domain ->
                            StudentDomainCard(
                                domain = domain,
                                selected = selectedDomain == domain.name,
                                darkTheme = darkTheme,
                                cardBackground = cardBackground,
                                textPrimary = textPrimary,
                                textMuted = textMuted,
                                onClick = { onSelectDomain(domain.name) },
                                modifier = Modifier.weight(1f)
                            )
                        }
                        if (row.size == 1) {
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            if (filteredDomains.isEmpty()) {
                item {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 28.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "No domains found. Try a broader search.",
                            color = textMuted,
                            fontSize = 14.sp
                        )
                    }
                }
            }

            item {
                Text(
                    text = if (normalizedQuery.isBlank()) {
                        "You can add more domains later in your profile settings."
                    } else {
                        "Showing every SkillLaunch marketplace domain that matches your search."
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 18.dp, vertical = 8.dp),
                    color = textMuted,
                    fontSize = 13.sp,
                    lineHeight = 18.sp,
                    fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
                )
            }
        }

        if (error.isNotBlank()) {
            Text(
                text = error,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 28.dp, vertical = 5.dp),
                color = Color(0xFFD95C5C),
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(
                    if (darkTheme) Color.White.copy(alpha = 0.08f)
                    else Color.Black.copy(alpha = 0.08f)
                )
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 12.dp),
            horizontalArrangement = Arrangement.End,
            verticalAlignment = Alignment.CenterVertically
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
                ),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 0.dp)
            ) {
                Text(
                    text = "Continue  →",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
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
private fun SearchGlyph(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = 1.9.dp.toPx()
        val radius = size.minDimension * 0.31f
        val center = Offset(size.width * 0.43f, size.height * 0.43f)
        drawCircle(color = tint, radius = radius, center = center, style = Stroke(width = stroke))
        drawLine(
            color = tint,
            start = Offset(size.width * 0.68f, size.height * 0.68f),
            end = Offset(size.width * 0.90f, size.height * 0.90f),
            strokeWidth = stroke,
            cap = StrokeCap.Round
        )
    }
}

@Composable
private fun CloseGlyph(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = 1.8.dp.toPx()
        drawLine(color = tint, start = Offset(size.width * 0.26f, size.height * 0.26f), end = Offset(size.width * 0.74f, size.height * 0.74f), strokeWidth = stroke, cap = StrokeCap.Round)
        drawLine(color = tint, start = Offset(size.width * 0.74f, size.height * 0.26f), end = Offset(size.width * 0.26f, size.height * 0.74f), strokeWidth = stroke, cap = StrokeCap.Round)
    }
}

@Composable
private fun CheckGlyph(tint: Color, modifier: Modifier = Modifier) {
    Canvas(modifier) {
        val stroke = 1.9.dp.toPx()
        val path = Path().apply {
            moveTo(size.width * 0.16f, size.height * 0.52f)
            lineTo(size.width * 0.40f, size.height * 0.76f)
            lineTo(size.width * 0.84f, size.height * 0.25f)
        }
        drawPath(path = path, color = tint, style = Stroke(width = stroke, cap = StrokeCap.Round))
    }
}

@Composable
private fun DomainSearchField(
    value: String,
    darkTheme: Boolean,
    onValueChange: (String) -> Unit,
    onClear: () -> Unit
) {
    val searchBackground = if (darkTheme) Color(0xFF262629) else Color(0xFFE8E7EA)
    val textPrimary = if (darkTheme) Color.White else Color(0xFF1B1B1F)
    val textMuted = if (darkTheme) Color(0xFF8F8E94) else Color(0xFF8D8C92)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 28.dp)
            .height(56.dp)
            .clip(RoundedCornerShape(30.dp))
            .background(searchBackground)
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
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
            textStyle = TextStyle(color = textPrimary, fontSize = 16.sp),
            decorationBox = { inner ->
                if (value.isBlank()) {
                    Text(
                        text = "Search domains (e.g. Web Development, AI)...",
                        color = textMuted,
                        fontSize = 16.sp,
                        maxLines = 1
                    )
                }
                inner()
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
private fun StudentDomainCard(
    domain: DomainOption,
    selected: Boolean,
    darkTheme: Boolean,
    cardBackground: Color,
    textPrimary: Color,
    textMuted: Color,
    onClick: () -> Unit,
    modifier: Modifier
) {
    val selectedBackground = if (darkTheme) Color(0xFF34343A) else Color(0xFFF8F6FC)
    val borderColor = if (selected) Color(0xFFD4C6FF) else Color.Transparent

    Column(
        modifier = modifier
            .height(208.dp)
            .clip(RoundedCornerShape(22.dp))
            .background(if (selected) selectedBackground else cardBackground)
            .border(
                width = if (selected) 2.dp else 0.dp,
                color = borderColor,
                shape = RoundedCornerShape(22.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 10.dp, vertical = 11.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(7.dp)
    ) {
        Box(
            modifier = Modifier
                .size(126.dp)
                .clip(RoundedCornerShape(17.dp))
                .background(Color.Transparent)
        ) {
            AsyncImage(
                model = domain.imageRes,
                contentDescription = domain.title + " abstract illustration",
                modifier = Modifier.fillMaxSize(),
                contentScale = ContentScale.Crop
            )
            if (selected) {
                Box(
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(6.dp)
                        .size(23.dp)
                        .shadow(5.dp, CircleShape)
                        .clip(CircleShape)
                        .background(if (darkTheme) Color(0xFF1A1A1D) else Color.White)
                        .border(1.dp, Color(0xFFD4C6FF), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    CheckGlyph(
                        tint = if (darkTheme) Color.White else Color(0xFF6F56D9),
                        modifier = Modifier.size(15.dp)
                    )
                }
            }
        }

        Text(
            text = domain.category,
            modifier = Modifier.fillMaxWidth(),
            color = textMuted,
            fontSize = 9.sp,
            lineHeight = 10.sp,
            fontWeight = FontWeight.SemiBold,
            letterSpacing = 0.3.sp,
            maxLines = 1,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )

        Text(
            text = domain.title,
            modifier = Modifier.fillMaxWidth(),
            color = textPrimary,
            fontSize = 14.5.sp,
            lineHeight = 17.sp,
            fontWeight = FontWeight.Bold,
            maxLines = 2,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
    }
}



