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
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.skilllaunch.app.feature.auth.SkillLaunchBrand

internal data class StudentGigDomain(
    val name: String,
    val description: String,
    val searchTerms: List<String>
)

private val featuredDomainNames = listOf(
    "Web Development",
    "Mobile App Development",
    "AI, Machine Learning & Data Science",
    "Design & Creative",
    "Software & IT Services",
    "Digital Marketing & SEO"
)

internal val STUDENT_GIG_FEATURED_DOMAINS = featuredDomainNames

internal val STUDENT_GIG_DOMAIN_CATALOG = listOf(
    StudentGigDomain("Web Development", "Websites, web apps and storefronts", listOf("frontend", "backend", "full stack", "cms", "website", "web app")),
    StudentGigDomain("Software & IT Services", "Software, APIs, automation and technical systems", listOf("software", "api", "desktop", "automation", "testing", "it")),
    StudentGigDomain("Mobile App Development", "Android, iOS and cross-platform apps", listOf("android", "ios", "flutter", "react native", "mobile", "app")),
    StudentGigDomain("AI, Machine Learning & Data Science", "AI, ML, analytics and data systems", listOf("artificial intelligence", "machine learning", "deep learning", "data", "analytics", "llm", "computer vision")),
    StudentGigDomain("Design & Creative", "UI, UX, branding and visual design", listOf("ui", "ux", "figma", "graphic", "branding", "illustration", "creative")),
    StudentGigDomain("Photography & Image Editing", "Photography, retouching and image work", listOf("photo", "photography", "retouching", "editing", "image")),
    StudentGigDomain("Video, Audio & Animation", "Video, audio, motion and animation", listOf("video", "audio", "motion", "animation", "podcast", "voice")),
    StudentGigDomain("Social Media & Community", "Social content, communities and engagement", listOf("social media", "community", "discord", "telegram", "content")),
    StudentGigDomain("Digital Marketing & SEO", "SEO, ads, campaigns and growth", listOf("marketing", "seo", "google ads", "meta ads", "ppc", "growth")),
    StudentGigDomain("E-commerce & Retail", "Online stores, marketplaces and retail support", listOf("ecommerce", "e-commerce", "shopify", "amazon", "retail", "store")),
    StudentGigDomain("Writing & Content Creation", "Articles, copy, scripts and documentation", listOf("writing", "copywriting", "blog", "content", "script", "documentation")),
    StudentGigDomain("Translation & Transcription", "Translation, localization, captions and transcripts", listOf("translation", "localization", "transcription", "subtitles", "caption")),
    StudentGigDomain("Gaming & Esports", "Game development, game art and esports", listOf("game", "gaming", "unity", "unreal", "esports", "streaming")),
    StudentGigDomain("Admin, Support & Operations", "Virtual assistance, support and operations", listOf("admin", "virtual assistant", "customer support", "data entry", "operations")),
    StudentGigDomain("Business, Finance & HR", "Business analysis, finance and people operations", listOf("business", "finance", "accounting", "hr", "recruiting", "sales")),
    StudentGigDomain("Legal & Compliance", "Legal documents, research and compliance support", listOf("legal", "compliance", "contracts", "ip", "privacy")),
    StudentGigDomain("Engineering, Architecture & 3D", "Engineering design, architecture and 3D work", listOf("engineering", "architecture", "cad", "3d", "mechanical", "civil", "electrical")),
    StudentGigDomain("Education, Tutoring & Coaching", "Tutoring, exam prep and coaching", listOf("education", "tutoring", "coaching", "exam", "teaching", "career")),
    StudentGigDomain("Events, Travel & Local Services", "Events, travel planning and local services", listOf("events", "travel", "wedding", "local", "drone")),
    StudentGigDomain("Telecommunications & Networking", "Networks, VoIP, hosting and communication systems", listOf("network", "networking", "telecom", "voip", "vpn", "hosting")),
    StudentGigDomain("Health & Wellness", "Fitness, nutrition and wellness services", listOf("health", "wellness", "fitness", "nutrition", "sports", "mindfulness")),
    StudentGigDomain("Manufacturing & Product Development", "Product design, sourcing and manufacturing support", listOf("manufacturing", "product", "sourcing", "prototype", "packaging")),
    StudentGigDomain("Product Management & Operations", "Product strategy, delivery and process systems", listOf("product management", "roadmap", "project management", "agile", "process")),
    StudentGigDomain("Market Research & Consumer Insights", "Market, competitor and customer research", listOf("market research", "consumer", "competitor", "customer research", "insights")),
    StudentGigDomain("Public Relations & Communications", "PR, corporate messaging and creator relations", listOf("pr", "public relations", "communications", "media", "influencer")),
    StudentGigDomain("Career & Professional Services", "Resumes, profiles, job search and interview prep", listOf("career", "resume", "cv", "linkedin", "interview", "professional")),
    StudentGigDomain("Government & Nonprofit Services", "Research, documentation and nonprofit support", listOf("government", "nonprofit", "non-profit", "ngo", "public sector")),
    StudentGigDomain("Real Estate & Property Services", "Property listings, research and marketing", listOf("real estate", "property", "listing", "realtor")),
    StudentGigDomain("Travel & Hospitality", "Hospitality, travel and guest experience", listOf("hospitality", "hotel", "tourism", "travel", "guest")),
    StudentGigDomain("Food & Culinary Services", "Food, recipes, menus and culinary support", listOf("food", "culinary", "recipe", "menu", "cooking", "restaurant")),
    StudentGigDomain("Beauty & Personal Care", "Beauty, grooming and personal care services", listOf("beauty", "personal care", "makeup", "hair", "grooming", "skincare")),
    StudentGigDomain("Fashion, Jewelry & Accessories", "Fashion, merchandise, jewelry and accessories", listOf("fashion", "jewelry", "apparel", "clothing", "accessories", "merchandise")),
    StudentGigDomain("Scientific & Technical Research", "Scientific research, technical analysis and documentation", listOf("scientific", "research", "technical research", "laboratory", "analysis")),
    StudentGigDomain("Freight, Delivery & Transportation", "Logistics, delivery and transport support", listOf("freight", "delivery", "transportation", "logistics", "shipping")),
    StudentGigDomain("Agriculture & Environmental Services", "Agriculture, sustainability and environmental work", listOf("agriculture", "farming", "environment", "sustainability", "ecology")),
    StudentGigDomain("3D Printing & Digital Fabrication", "3D printing, fabrication and production files", listOf("3d printing", "digital fabrication", "cnc", "laser cutting", "stl")),
    StudentGigDomain("Consulting & Professional Advisory", "Business, technology and creative consulting", listOf("consulting", "advisory", "strategy", "technology consulting")),
    StudentGigDomain("Personal Development & Hobbies", "Personal growth, hobbies and creative instruction", listOf("personal development", "hobbies", "drawing", "music lessons", "productivity"))
)

internal val studentGigDomainNames = STUDENT_GIG_DOMAIN_CATALOG.map { it.name }.toSet()

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

    val matchingDomains = remember(normalizedQuery) {
        if (normalizedQuery.isBlank()) {
            STUDENT_GIG_FEATURED_DOMAINS.mapNotNull { name ->
                STUDENT_GIG_DOMAIN_CATALOG.firstOrNull { it.name == name }
            }
        } else {
            STUDENT_GIG_DOMAIN_CATALOG.filter { domain ->
                domain.name.contains(normalizedQuery, ignoreCase = true) ||
                    domain.description.contains(normalizedQuery, ignoreCase = true) ||
                    domain.searchTerms.any { it.contains(normalizedQuery, ignoreCase = true) }
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
            matchingDomains.chunked(2).forEach { row ->
                item(key = row.joinToString("|")) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        row.forEach { domain ->
                            val index = STUDENT_GIG_DOMAIN_CATALOG.indexOfFirst { it.name == domain.name }
                            StudentDomainCard(
                                domain = domain,
                                artworkIndex = index,
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

            if (matchingDomains.isEmpty()) {
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
    domain: StudentGigDomain,
    artworkIndex: Int,
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
            DomainArtwork(
                index = artworkIndex,
                darkTheme = darkTheme
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
            text = domainEyebrow(domain.name),
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
            text = domain.name,
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

@Composable
private fun DomainArtwork(
    index: Int,
    darkTheme: Boolean
) {
    val backgrounds = listOf(
        Color(0xFFD9C8FF), Color(0xFFF0E0CF), Color(0xFFF5C7D9), Color(0xFFD8D5FF),
        Color(0xFFF1CFDE), Color(0xFFE8D8C6), Color(0xFFD9CBFF), Color(0xFFF1D3C5),
        Color(0xFFD9E0FF), Color(0xFFDDD2FF), Color(0xFFFFD7D7), Color(0xFFD5E1FF)
    )
    val accents = listOf(
        Color(0xFF9A7CE8), Color(0xFFB58AF0), Color(0xFF9C8BE8), Color(0xFF8F84E0),
        Color(0xFFB684E0), Color(0xFF8F7DCC), Color(0xFFA17AE0), Color(0xFFAA84E7),
        Color(0xFF8B91E5), Color(0xFFA184E4), Color(0xFFB07EDB), Color(0xFF8C91E0)
    )
    val darks = listOf(
        Color(0xFF29253B), Color(0xFF2B273D), Color(0xFF29263D), Color(0xFF28243C),
        Color(0xFF2D263C), Color(0xFF2A263A), Color(0xFF2B2740), Color(0xFF30263B),
        Color(0xFF29273F), Color(0xFF2E283D), Color(0xFF2C273E), Color(0xFF29263E)
    )

    val bg = backgrounds[index % backgrounds.size]
    val accent = accents[index % accents.size]
    val deep = darks[index % darks.size]
    val soft = if (darkTheme) Color.White.copy(alpha = 0.88f) else Color(0xFFFFFBF4)

    Canvas(
        modifier = Modifier.fillMaxSize()
    ) {
        val w = size.width
        val h = size.height
        val cx = w * 0.50f
        val cy = h * 0.48f
        val scale = minOf(w, h) / 126f

        fun sx(v: Float) = v * scale
        fun sy(v: Float) = v * scale

        drawRoundRect(
            color = bg,
            topLeft = Offset.Zero,
            size = size,
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(17f), sx(17f))
        )

        // Soft gallery-light glow.
        drawCircle(
            color = Color.White.copy(alpha = 0.20f),
            radius = sx(38f),
            center = Offset(w * 0.77f, h * 0.23f)
        )
        drawCircle(
            color = accent.copy(alpha = 0.22f),
            radius = sx(24f),
            center = Offset(w * 0.19f, h * 0.77f)
        )

        // Museum-style plinth.
        drawRoundRect(
            color = deep.copy(alpha = 0.18f),
            topLeft = Offset(cx - sx(33f), h * 0.74f),
            size = androidx.compose.ui.geometry.Size(sx(66f), sx(12f)),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(6f), sx(6f))
        )
        drawRoundRect(
            color = soft,
            topLeft = Offset(cx - sx(35f), h * 0.70f),
            size = androidx.compose.ui.geometry.Size(sx(70f), sx(13f)),
            cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(6.5f), sx(6.5f))
        )

        val motif = index % 13

        when (motif) {
            0 -> { // Web / software: stacked interface sculpture.
                drawRoundRect(
                    color = deep,
                    topLeft = Offset(cx - sx(38f), cy - sx(30f)),
                    size = androidx.compose.ui.geometry.Size(sx(76f), sx(56f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(8f), sx(8f))
                )
                drawRoundRect(
                    color = soft.copy(alpha = 0.90f),
                    topLeft = Offset(cx - sx(29f), cy - sx(20f)),
                    size = androidx.compose.ui.geometry.Size(sx(58f), sx(34f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(5f), sx(5f))
                )
                drawRoundRect(
                    color = accent,
                    topLeft = Offset(cx - sx(21f), cy - sx(12f)),
                    size = androidx.compose.ui.geometry.Size(sx(22f), sx(14f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(3f), sx(3f))
                )
                drawRoundRect(
                    color = accent.copy(alpha = 0.55f),
                    topLeft = Offset(cx + sx(5f), cy - sx(12f)),
                    size = androidx.compose.ui.geometry.Size(sx(17f), sx(6f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(3f), sx(3f))
                )
                drawRoundRect(
                    color = accent.copy(alpha = 0.40f),
                    topLeft = Offset(cx + sx(5f), cy - sx(2f)),
                    size = androidx.compose.ui.geometry.Size(sx(23f), sx(6f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(3f), sx(3f))
                )
                drawCircle(accent, sx(5f), Offset(cx - sx(26f), cy + sx(5f)))
            }

            1 -> { // Code / APIs: command panel + orbit.
                drawRoundRect(
                    color = deep,
                    topLeft = Offset(cx - sx(35f), cy - sx(25f)),
                    size = androidx.compose.ui.geometry.Size(sx(52f), sx(43f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(8f), sx(8f))
                )
                val p = Path().apply {
                    moveTo(cx - sx(22f), cy - sx(4f))
                    lineTo(cx - sx(13f), cy + sx(2f))
                    lineTo(cx - sx(22f), cy + sx(8f))
                }
                drawPath(p, color = soft, style = Stroke(width = sx(4f), cap = StrokeCap.Round))
                drawLine(
                    color = soft,
                    start = Offset(cx - sx(8f), cy + sx(8f)),
                    end = Offset(cx + sx(6f), cy + sx(8f)),
                    strokeWidth = sx(4f),
                    cap = StrokeCap.Round
                )
                drawCircle(accent, sx(18f), Offset(cx + sx(23f), cy + sx(8f)))
                drawCircle(soft, sx(5f), Offset(cx + sx(23f), cy + sx(8f)))
                drawCircle(Color.Transparent, sx(23f), Offset(cx + sx(23f), cy + sx(8f)), style = Stroke(width = sx(3f)))
            }

            2 -> { // Mobile: premium phone + halo.
                rotate(degrees = -10f, pivot = Offset(cx, cy)) {
                    drawRoundRect(
                        color = deep,
                        topLeft = Offset(cx - sx(20f), cy - sx(38f)),
                        size = androidx.compose.ui.geometry.Size(sx(40f), sx(76f)),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(9f), sx(9f))
                    )
                    drawRoundRect(
                        color = soft.copy(alpha = 0.19f),
                        topLeft = Offset(cx - sx(15f), cy - sx(29f)),
                        size = androidx.compose.ui.geometry.Size(sx(30f), sx(55f)),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(6f), sx(6f))
                    )
                    drawCircle(soft, sx(2.6f), Offset(cx, cy + sx(28f)))
                }
                drawOval(
                    color = accent,
                    topLeft = Offset(cx - sx(49f), cy - sx(17f)),
                    size = androidx.compose.ui.geometry.Size(sx(98f), sx(34f)),
                    style = Stroke(width = sx(6f))
                )
                drawCircle(soft, sx(6f), Offset(cx + sx(37f), cy - sx(20f)))
            }

            3 -> { // AI / data: faceted crystal with nodes.
                val poly = Path().apply {
                    moveTo(cx, cy - sx(39f))
                    lineTo(cx + sx(28f), cy - sx(20f))
                    lineTo(cx + sx(34f), cy + sx(16f))
                    lineTo(cx, cy + sx(37f))
                    lineTo(cx - sx(30f), cy + sx(17f))
                    lineTo(cx - sx(25f), cy - sx(17f))
                    close()
                }
                drawPath(poly, color = soft.copy(alpha = 0.92f))
                drawLine(accent, Offset(cx, cy - sx(39f)), Offset(cx, cy + sx(37f)), sx(3f))
                drawLine(accent, Offset(cx - sx(25f), cy - sx(17f)), Offset(cx + sx(28f), cy - sx(20f)), sx(3f))
                drawLine(accent, Offset(cx - sx(30f), cy + sx(17f)), Offset(cx + sx(34f), cy + sx(16f)), sx(3f))
                listOf(
                    Offset(cx - sx(45f), cy - sx(24f)),
                    Offset(cx + sx(45f), cy - sx(8f)),
                    Offset(cx - sx(37f), cy + sx(30f))
                ).forEach { point ->
                    drawCircle(accent, sx(6f), point)
                }
            }

            4 -> { // Design: sculptural ribbon + tool.
                val ribbon = Path().apply {
                    moveTo(cx - sx(48f), cy + sx(23f))
                    cubicTo(cx - sx(28f), cy - sx(15f), cx - sx(2f), cy - sx(21f), cx + sx(19f), cy - sx(5f))
                    cubicTo(cx + sx(35f), cy + sx(7f), cx + sx(36f), cy + sx(17f), cx + sx(49f), cy + sx(28f))
                }
                drawPath(ribbon, color = accent, style = Stroke(width = sx(11f), cap = StrokeCap.Round))
                drawRoundRect(
                    color = soft,
                    topLeft = Offset(cx - sx(13f), cy - sx(30f)),
                    size = androidx.compose.ui.geometry.Size(sx(26f), sx(57f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(6f), sx(6f))
                )
                rotate(degrees = -25f, pivot = Offset(cx, cy)) {
                    drawRoundRect(
                        color = deep,
                        topLeft = Offset(cx - sx(5f), cy - sx(30f)),
                        size = androidx.compose.ui.geometry.Size(sx(10f), sx(47f)),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(4f), sx(4f))
                    )
                }
            }

            5 -> { // Photography: camera sculpture.
                drawRoundRect(
                    color = deep,
                    topLeft = Offset(cx - sx(40f), cy - sx(21f)),
                    size = androidx.compose.ui.geometry.Size(sx(80f), sx(44f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(10f), sx(10f))
                )
                drawRoundRect(
                    color = soft,
                    topLeft = Offset(cx - sx(13f), cy - sx(29f)),
                    size = androidx.compose.ui.geometry.Size(sx(26f), sx(11f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(5f), sx(5f))
                )
                drawCircle(accent, sx(21f), Offset(cx, cy))
                drawCircle(deep, sx(12f), Offset(cx, cy))
                drawCircle(soft, sx(5f), Offset(cx + sx(2f), cy - sx(2f)))
                drawCircle(accent.copy(alpha = 0.55f), sx(6f), Offset(cx + sx(39f), cy - sx(19f)))
            }

            6 -> { // Video / animation: frame + play.
                drawRoundRect(
                    color = deep,
                    topLeft = Offset(cx - sx(44f), cy - sx(28f)),
                    size = androidx.compose.ui.geometry.Size(sx(88f), sx(58f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(8f), sx(8f))
                )
                val tri = Path().apply {
                    moveTo(cx - sx(8f), cy - sx(14f))
                    lineTo(cx + sx(15f), cy)
                    lineTo(cx - sx(8f), cy + sx(14f))
                    close()
                }
                drawPath(tri, color = soft)
                drawPath(
                    Path().apply {
                        moveTo(cx - sx(45f), cy + sx(27f))
                        cubicTo(cx - sx(24f), cy + sx(8f), cx + sx(3f), cy + sx(8f), cx + sx(22f), cy + sx(23f))
                        cubicTo(cx + sx(33f), cy + sx(30f), cx + sx(42f), cy + sx(30f), cx + sx(51f), cy + sx(20f))
                    },
                    color = accent,
                    style = Stroke(width = sx(8f), cap = StrokeCap.Round)
                )
            }

            7 -> { // Social: chat sculpture.
                drawRoundRect(
                    color = deep,
                    topLeft = Offset(cx - sx(41f), cy - sx(29f)),
                    size = androidx.compose.ui.geometry.Size(sx(64f), sx(47f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(10f), sx(10f))
                )
                drawRoundRect(
                    color = soft,
                    topLeft = Offset(cx - sx(1f), cy - sx(5f)),
                    size = androidx.compose.ui.geometry.Size(sx(49f), sx(35f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(9f), sx(9f))
                )
                drawCircle(accent, sx(4f), Offset(cx + sx(11f), cy + sx(12f)))
                drawCircle(accent, sx(4f), Offset(cx + sx(23f), cy + sx(12f)))
                drawCircle(accent, sx(4f), Offset(cx + sx(35f), cy + sx(12f)))
                drawLine(
                    color = accent,
                    start = Offset(cx + sx(10f), cy + sx(20f)),
                    end = Offset(cx + sx(39f), cy + sx(20f)),
                    strokeWidth = sx(3f),
                    cap = StrokeCap.Round
                )
            }

            8 -> { // Marketing: megaphone + rising bars.
                rotate(degrees = -18f, pivot = Offset(cx - sx(12f), cy)) {
                    drawPath(
                        Path().apply {
                            moveTo(cx - sx(37f), cy - sx(9f))
                            lineTo(cx + sx(8f), cy - sx(27f))
                            lineTo(cx + sx(8f), cy + sx(27f))
                            lineTo(cx - sx(37f), cy + sx(9f))
                            close()
                        },
                        color = soft
                    )
                    drawRoundRect(
                        color = deep,
                        topLeft = Offset(cx - sx(44f), cy - sx(7f)),
                        size = androidx.compose.ui.geometry.Size(sx(14f), sx(14f)),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(4f), sx(4f))
                    )
                }
                listOf(
                    Triple(cx + sx(13f), cy + sx(22f), sx(24f)),
                    Triple(cx + sx(28f), cy + sx(22f), sx(37f)),
                    Triple(cx + sx(43f), cy + sx(22f), sx(51f))
                ).forEach { (x, base, top) ->
                    drawRoundRect(
                        color = accent,
                        topLeft = Offset(x - sx(4f), top),
                        size = androidx.compose.ui.geometry.Size(sx(8f), base - top),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(4f), sx(4f))
                    )
                }
            }

            9 -> { // E-commerce: packages / retail.
                drawRoundRect(
                    color = soft,
                    topLeft = Offset(cx - sx(35f), cy - sx(12f)),
                    size = androidx.compose.ui.geometry.Size(sx(30f), sx(31f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(5f), sx(5f))
                )
                drawRoundRect(
                    color = deep,
                    topLeft = Offset(cx - sx(2f), cy - sx(23f)),
                    size = androidx.compose.ui.geometry.Size(sx(37f), sx(43f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(6f), sx(6f))
                )
                drawLine(accent, Offset(cx + sx(16f), cy - sx(22f)), Offset(cx + sx(16f), cy + sx(19f)), sx(4f))
                drawLine(accent, Offset(cx - sx(20f), cy - sx(12f)), Offset(cx - sx(20f), cy + sx(19f)), sx(4f))
                drawCircle(accent, sx(6f), Offset(cx - sx(43f), cy + sx(25f)))
            }

            10 -> { // Writing / translation: scroll + pen.
                val scroll = Path().apply {
                    moveTo(cx - sx(38f), cy - sx(25f))
                    lineTo(cx + sx(25f), cy - sx(25f))
                    lineTo(cx + sx(25f), cy + sx(26f))
                    lineTo(cx - sx(38f), cy + sx(26f))
                    close()
                }
                drawPath(scroll, color = soft)
                drawLine(accent, Offset(cx - sx(24f), cy - sx(8f)), Offset(cx + sx(10f), cy - sx(8f)), sx(4f), cap = StrokeCap.Round)
                drawLine(accent.copy(alpha = 0.55f), Offset(cx - sx(24f), cy + sx(3f)), Offset(cx + sx(4f), cy + sx(3f)), sx(4f), cap = StrokeCap.Round)
                rotate(degrees = -35f, pivot = Offset(cx + sx(16f), cy + sx(10f))) {
                    drawRoundRect(
                        color = deep,
                        topLeft = Offset(cx + sx(8f), cy - sx(6f)),
                        size = androidx.compose.ui.geometry.Size(sx(9f), sx(43f)),
                        cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(4f), sx(4f))
                    )
                }
            }

            11 -> { // Gaming: controller sculpture.
                val controller = Path().apply {
                    moveTo(cx - sx(36f), cy - sx(15f))
                    cubicTo(cx - sx(29f), cy - sx(34f), cx - sx(14f), cy - sx(37f), cx, cy - sx(24f))
                    cubicTo(cx + sx(14f), cy - sx(37f), cx + sx(29f), cy - sx(34f), cx + sx(36f), cy - sx(15f))
                    cubicTo(cx + sx(38f), cy + sx(1f), cx + sx(34f), cy + sx(22f), cx + sx(23f), cy + sx(25f))
                    cubicTo(cx + sx(13f), cy + sx(26f), cx + sx(8f), cy + sx(11f), cx, cy + sx(11f))
                    cubicTo(cx - sx(8f), cy + sx(11f), cx - sx(13f), cy + sx(26f), cx - sx(23f), cy + sx(25f))
                    cubicTo(cx - sx(34f), cy + sx(22f), cx - sx(38f), cy + sx(1f), cx - sx(36f), cy - sx(15f))
                    close()
                }
                drawPath(controller, color = soft)
                drawLine(deep, Offset(cx - sx(20f), cy - sx(3f)), Offset(cx - sx(4f), cy - sx(3f)), sx(5f), cap = StrokeCap.Round)
                drawLine(deep, Offset(cx - sx(12f), cy - sx(11f)), Offset(cx - sx(12f), cy + sx(5f)), sx(5f), cap = StrokeCap.Round)
                drawCircle(accent, sx(5f), Offset(cx + sx(16f), cy - sx(4f)))
                drawCircle(deep, sx(5f), Offset(cx + sx(28f), cy + sx(7f)))
            }

            12 -> { // Security / legal: shield + lock.
                val shield = Path().apply {
                    moveTo(cx, cy - sx(40f))
                    lineTo(cx + sx(35f), cy - sx(24f))
                    lineTo(cx + sx(28f), cy + sx(20f))
                    cubicTo(cx + sx(20f), cy + sx(35f), cx + sx(8f), cy + sx(41f), cx, cy + sx(45f))
                    cubicTo(cx - sx(8f), cy + sx(41f), cx - sx(20f), cy + sx(35f), cx - sx(28f), cy + sx(20f))
                    lineTo(cx - sx(35f), cy - sx(24f))
                    close()
                }
                drawPath(shield, color = soft)
                drawRoundRect(
                    color = accent,
                    topLeft = Offset(cx - sx(13f), cy - sx(2f)),
                    size = androidx.compose.ui.geometry.Size(sx(26f), sx(23f)),
                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(sx(5f), sx(5f))
                )
                drawArc(
                    color = deep,
                    startAngle = 180f,
                    sweepAngle = 180f,
                    useCenter = false,
                    topLeft = Offset(cx - sx(9f), cy - sx(14f)),
                    size = androidx.compose.ui.geometry.Size(sx(18f), sx(20f)),
                    style = Stroke(width = sx(4f))
                )
            }
        }

        // A small floating sphere gives the gallery/plinth feel from the reference.
        drawCircle(
            color = if (index % 2 == 0) accent.copy(alpha = 0.78f) else soft.copy(alpha = 0.84f),
            radius = sx(6f + (index % 3)),
            center = Offset(w * (0.17f + (index % 4) * 0.18f), h * (0.18f + (index % 3) * 0.17f))
        )
    }
}

private fun domainEyebrow(domainName: String): String = when {
    domainName.contains("AI", ignoreCase = true) || domainName.contains("Data Science", ignoreCase = true) -> "TECHNOLOGY"
    domainName.contains("Mobile", ignoreCase = true) || domainName.contains("Web", ignoreCase = true) || domainName.contains("Software", ignoreCase = true) -> "DEVELOPMENT"
    domainName.contains("Security", ignoreCase = true) || domainName.contains("Legal", ignoreCase = true) -> "SECURITY"
    domainName.contains("Design", ignoreCase = true) || domainName.contains("Creative", ignoreCase = true) -> "DESIGN"
    domainName.contains("Marketing", ignoreCase = true) || domainName.contains("Social", ignoreCase = true) || domainName.contains("Public Relations", ignoreCase = true) -> "MARKETING"
    domainName.contains("Business", ignoreCase = true) || domainName.contains("Finance", ignoreCase = true) || domainName.contains("HR", ignoreCase = true) -> "BUSINESS"
    domainName.contains("Cloud", ignoreCase = true) || domainName.contains("Telecommunications", ignoreCase = true) || domainName.contains("3D Printing", ignoreCase = true) -> "INFRASTRUCTURE"
    domainName.contains("Video", ignoreCase = true) || domainName.contains("Audio", ignoreCase = true) || domainName.contains("Animation", ignoreCase = true) -> "CREATIVE"
    domainName.contains("Writing", ignoreCase = true) || domainName.contains("Translation", ignoreCase = true) || domainName.contains("Education", ignoreCase = true) -> "CONTENT"
    domainName.contains("Gaming", ignoreCase = true) -> "GAMING"
    else -> "SERVICES"
}