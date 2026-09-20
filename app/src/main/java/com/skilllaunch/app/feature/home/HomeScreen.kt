package com.skilllaunch.app.feature.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.skilllaunch.app.core.navigation.AppDestination
import com.skilllaunch.app.data.model.auth.AuthUser
import java.util.Locale

@Composable
fun HomeScreen(
    user: AuthUser,
    onOpenDestination: (AppDestination) -> Unit
) {
    val role = user.role?.uppercase(Locale.US)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Text(
                text = "Welcome back, ${user.firstName ?: user.username ?: "there"}",
                style = MaterialTheme.typography.headlineMedium
            )

            Text(
                text = roleLabel(role),
                modifier = Modifier.padding(top = 6.dp),
                color = MaterialTheme.colorScheme.primary,
                style = MaterialTheme.typography.labelLarge
            )
        }

        item {
            Card {
                Column(
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(
                        text = when (role) {
                            "CLIENT" -> "Find student talent for your next project."
                            "ADMIN" -> "SkillLaunch administrator workspace."
                            else -> "Turn your skills into opportunities."
                        },
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = when (role) {
                            "CLIENT" -> "Your client workspace is ready for projects, proposals, orders, and conversations."
                            "ADMIN" -> "The Android shell recognizes the administrator role without prematurely building the admin application."
                            else -> "Your student workspace is ready for gigs, jobs, orders, messages, and wallet features."
                        },
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            Text(
                text = "Quick access",
                style = MaterialTheme.typography.titleLarge
            )
        }

        item {
            Button(
                onClick = { onOpenDestination(AppDestination.Explore) }
            ) {
                Text("Open Explore")
            }
        }

        item {
            OutlinedButton(
                onClick = { onOpenDestination(AppDestination.Orders) }
            ) {
                Text("Open Orders")
            }
        }

        item {
            OutlinedCard {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = when (role) {
                            "CLIENT" -> "Active projects"
                            else -> "Profile & opportunities"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = when (role) {
                            "CLIENT" -> "Live project data will appear here when the native client project workspace is connected."
                            else -> "Real profile, gig, and job data will appear here as the native marketplace features are connected."
                        },
                        modifier = Modifier.padding(top = 6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            OutlinedCard {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = when (role) {
                            "CLIENT" -> "Student proposals"
                            else -> "Recommended opportunities"
                        },
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = when (role) {
                            "CLIENT" -> "Live proposal information will appear here when that Android workflow is implemented."
                            else -> "Live gig and job recommendations will appear here when those Android APIs are connected."
                        },
                        modifier = Modifier.padding(top = 6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        item {
            OutlinedCard {
                Column(
                    modifier = Modifier.padding(18.dp)
                ) {
                    Text(
                        text = "Active work",
                        style = MaterialTheme.typography.titleMedium
                    )

                    Text(
                        text = "Real order information will appear here once the native order workspace is implemented.",
                        modifier = Modifier.padding(top = 6.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }
    }
}

private fun roleLabel(role: String?): String = when (role) {
    "CLIENT" -> "Client account"
    "ADMIN" -> "Administrator account"
    else -> "Student Freelancer account"
}
