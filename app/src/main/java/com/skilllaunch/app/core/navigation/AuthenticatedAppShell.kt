package com.skilllaunch.app.core.navigation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.navigation3.runtime.NavEntry
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.ui.NavDisplay
import com.skilllaunch.app.data.model.auth.AuthUser
import com.skilllaunch.app.data.repository.profile.ProfileRepository
import com.skilllaunch.app.feature.home.HomeScreen
import com.skilllaunch.app.feature.profile.ProfileScreen
import java.util.Locale

sealed interface AppDestination : NavKey {
    data object Home : AppDestination
    data object Explore : AppDestination
    data object Orders : AppDestination
    data object Chat : AppDestination
    data object Profile : AppDestination
}

private val shellDestinations = listOf(
    AppDestination.Home,
    AppDestination.Explore,
    AppDestination.Orders,
    AppDestination.Chat,
    AppDestination.Profile
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AuthenticatedAppShell(
    user: AuthUser,
    profileRepository: ProfileRepository,
    onLogout: () -> Unit
) {
    val backStack = remember {
        mutableStateListOf<AppDestination>(AppDestination.Home)
    }
    val current = backStack.lastOrNull() ?: AppDestination.Home
    val imeVisible = WindowInsets.ime.getBottom(LocalDensity.current) > 0

    fun openDestination(destination: AppDestination) {
        if (destination == AppDestination.Home) {
            backStack.clear()
            backStack.add(AppDestination.Home)
        } else if (destination != current) {
            backStack.clear()
            backStack.add(AppDestination.Home)
            backStack.add(destination)
        }
    }

    Scaffold(
        topBar = {
            if (current != AppDestination.Profile) {
                CenterAlignedTopAppBar(
                    title = {
                        Text(
                            text = "SkillLaunch",
                            style = MaterialTheme.typography.titleLarge
                        )
                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = MaterialTheme.colorScheme.background
                    )
                )
            }
        },
        bottomBar = {
            if (!imeVisible) {
                NavigationBar {
                    shellDestinations.forEach { destination ->
                        NavigationBarItem(
                            selected = current == destination,
                            onClick = { openDestination(destination) },
                            icon = {
                                Text(
                                    text = destinationGlyph(destination),
                                    style = MaterialTheme.typography.titleMedium
                                )
                            },
                            label = {
                                Text(destinationTitle(destination))
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        NavDisplay(
            backStack = backStack,
            onBack = { backStack.removeLastOrNull() },
            entryProvider = { key ->
                when (key) {
                    AppDestination.Home -> NavEntry(key) {
                        HomeScreen(
                            user = user,
                            onOpenDestination = ::openDestination
                        )
                    }

                    AppDestination.Explore -> NavEntry(key) {
                        ShellEmptyState(
                            title = "Explore",
                            message = "Marketplace discovery will appear here as the native Explore feature is connected."
                        )
                    }

                    AppDestination.Orders -> NavEntry(key) {
                        ShellEmptyState(
                            title = "Orders",
                            message = "Your active and completed work will appear here as the native Orders feature is connected."
                        )
                    }

                    AppDestination.Chat -> NavEntry(key) {
                        ShellEmptyState(
                            title = "Chat",
                            message = "Conversations and realtime messaging will appear here as the native Chat feature is connected."
                        )
                    }

                    AppDestination.Profile -> NavEntry(key) {
                        ProfileScreen(
                            user = user,
                            repository = profileRepository,
                            onLogout = onLogout
                        )
                    }
                }
            },
            modifier = Modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun ShellEmptyState(
    title: String,
    message: String
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.headlineMedium
            )
            Text(
                text = message,
                modifier = Modifier.padding(top = 10.dp),
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                style = MaterialTheme.typography.bodyLarge
            )
        }
    }
}

@Composable
private fun ProfileShell(
    user: AuthUser,
    onLogout: () -> Unit
) {
    val displayName = user.fullName
        ?: listOfNotNull(user.firstName, user.lastName)
            .joinToString(" ")
            .ifBlank { "SkillLaunch User" }

    val role = user.role
        ?.replace('_', ' ')
        ?.lowercase(Locale.getDefault())
        ?.replaceFirstChar { it.titlecase(Locale.getDefault()) }
        ?: "Unknown"

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        verticalArrangement = Arrangement.Top
    ) {
        Text(
            text = displayName,
            style = MaterialTheme.typography.headlineSmall
        )

        Text(
            text = user.email ?: "",
            modifier = Modifier.padding(top = 6.dp),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Text(
            text = "Role: $role",
            modifier = Modifier.padding(top = 16.dp),
            color = MaterialTheme.colorScheme.primary,
            style = MaterialTheme.typography.labelLarge
        )

        Button(
            onClick = onLogout,
            modifier = Modifier.padding(top = 28.dp)
        ) {
            Text("Log out")
        }
    }
}

private fun destinationTitle(destination: AppDestination): String = when (destination) {
    AppDestination.Home -> "Home"
    AppDestination.Explore -> "Explore"
    AppDestination.Orders -> "Orders"
    AppDestination.Chat -> "Chat"
    AppDestination.Profile -> "Profile"
}

private fun destinationGlyph(destination: AppDestination): String = when (destination) {
    AppDestination.Home -> "⌂"
    AppDestination.Explore -> "⌕"
    AppDestination.Orders -> "□"
    AppDestination.Chat -> "◌"
    AppDestination.Profile -> "○"
}
