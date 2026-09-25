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
import androidx.compose.foundation.layout.offset
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
import androidx.compose.ui.layout.ContentScale
import coil3.compose.AsyncImage
import com.skilllaunch.app.feature.auth.SkillLaunchBrand
import com.skilllaunch.app.R

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

private val artworkOrder = listOf(
    "student_domain_web",
    "student_domain_mobile",
    "student_domain_ai",
    "student_domain_design",
    "student_domain_software_it",
    "student_domain_marketing",
    "student_domain_photography_editing",
    "student_domain_video_audio_animation",
    "student_domain_social_community",
    "student_domain_ecommerce_retail",
    "student_domain_writing_content",
    "student_domain_translation_transcription",
    "student_domain_gaming_esports",
    "student_domain_admin_support",
    "student_domain_business_finance_hr",
    "student_domain_legal_compliance",
    "student_domain_engineering_arch_3d",
    "student_domain_education_coaching",
    "student_domain_events_travel_local",
    "student_domain_telecom_networking",
    "student_domain_health_wellness",
    "student_domain_manufacturing_product",
    "student_domain_product_management",
    "student_domain_market_research",
    "student_domain_pr_communications",
    "student_domain_career_professional",
    "student_domain_government_nonprofit",
    "student_domain_real_estate",
    "student_domain_travel_hospitality",
    "student_domain_food_culinary",
    "student_domain_beauty_personal",
    "student_domain_fashion_jewelry",
    "student_domain_scientific_research",
    "student_domain_freight_delivery",
    "student_domain_agriculture_environment",
    "student_domain_digital_fabrication",
    "student_domain_consulting_advisory",
    "student_domain_personal_hobbies"
)

private fun artworkResource(index: Int): Int = when (index) {
    0 -> R.raw.student_domain_web
    1 -> R.raw.student_domain_mobile
    2 -> R.raw.student_domain_ai
    3 -> R.raw.student_domain_design
    4 -> R.raw.student_domain_software_it
    5 -> R.raw.student_domain_marketing
    6 -> R.raw.student_domain_photography_editing
    7 -> R.raw.student_domain_video_audio_animation
    8 -> R.raw.student_domain_social_community
    9 -> R.raw.student_domain_ecommerce_retail
    10 -> R.raw.student_domain_writing_content
    11 -> R.raw.student_domain_translation_transcription
    12 -> R.raw.student_domain_gaming_esports
    13 -> R.raw.student_domain_admin_support
    14 -> R.raw.student_domain_business_finance_hr
    15 -> R.raw.student_domain_legal_compliance
    16 -> R.raw.student_domain_engineering_arch_3d
    17 -> R.raw.student_domain_education_coaching
    18 -> R.raw.student_domain_events_travel_local
    19 -> R.raw.student_domain_telecom_networking
    20 -> R.raw.student_domain_health_wellness
    21 -> R.raw.student_domain_manufacturing_product
    22 -> R.raw.student_domain_product_management
    23 -> R.raw.student_domain_market_research
    24 -> R.raw.student_domain_pr_communications
    25 -> R.raw.student_domain_career_professional
    26 -> R.raw.student_domain_government_nonprofit
    27 -> R.raw.student_domain_real_estate
    28 -> R.raw.student_domain_travel_hospitality
    29 -> R.raw.student_domain_food_culinary
    30 -> R.raw.student_domain_beauty_personal
    31 -> R.raw.student_domain_fashion_jewelry
    32 -> R.raw.student_domain_scientific_research
    33 -> R.raw.student_domain_freight_delivery
    34 -> R.raw.student_domain_agriculture_environment
    35 -> R.raw.student_domain_digital_fabrication
    36 -> R.raw.student_domain_consulting_advisory
    else -> R.raw.student_domain_personal_hobbies
}

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
                                artworkResource = artworkResource(index),
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
    artworkResource: Int,
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
                model = artworkResource,
                contentDescription = domain.name + " abstract illustration",
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