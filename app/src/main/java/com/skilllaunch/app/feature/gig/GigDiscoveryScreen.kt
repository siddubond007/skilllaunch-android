package com.skilllaunch.app.feature.gig

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skilllaunch.app.core.common.collectAsStateWithLifecycleCompat
import com.skilllaunch.app.data.model.gig.Gig
import com.skilllaunch.app.data.repository.gig.GigRepository

@Composable
fun GigDiscoveryScreen(
    repository: GigRepository
) {
    val factory = remember(repository) {
        object : ViewModelProvider.Factory {
            @Suppress("UNCHECKED_CAST")
            override fun <T : androidx.lifecycle.ViewModel> create(
                modelClass: Class<T>
            ): T {
                return GigViewModel(repository) as T
            }
        }
    }

    val gigViewModel: GigViewModel = viewModel(factory = factory)
    val uiState by gigViewModel.uiState.collectAsStateWithLifecycleCompat()

    LaunchedEffect(Unit) {
        gigViewModel.loadGigs()
    }

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            uiState.isLoading && uiState.gigs.isEmpty() -> {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    CircularProgressIndicator()
                    Text(
                        text = "Loading gigs…",
                        modifier = Modifier.padding(top = 12.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            uiState.errorMessage != null && uiState.gigs.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "We couldn't load the marketplace",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.SemiBold
                    )

                    Text(
                        text = uiState.errorMessage.orEmpty(),
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )

                    Button(
                        onClick = {
                            gigViewModel.clearError()
                            gigViewModel.loadGigs(forceRefresh = true)
                        },
                        modifier = Modifier.padding(top = 18.dp)
                    ) {
                        Text("Try again")
                    }
                }
            }

            uiState.gigs.isEmpty() -> {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Text(
                        text = "No gigs are published yet",
                        style = MaterialTheme.typography.titleLarge
                    )

                    Text(
                        text = "Published student services will appear here.",
                        modifier = Modifier.padding(top = 8.dp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            else -> {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp, vertical = 14.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    item {
                        Text(
                            text = "Discover student services",
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.SemiBold
                        )

                        Text(
                            text = uiState.gigs.size.toString() +
                                " published gig" +
                                if (uiState.gigs.size == 1) "" else "s",
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    items(
                        items = uiState.gigs,
                        key = { gig -> gig.id ?: gig.title.orEmpty() }
                    ) { gig ->
                        GigCard(gig)
                    }

                    item {
                        Button(
                            onClick = {
                                gigViewModel.loadGigs(forceRefresh = true)
                            },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Refresh marketplace")
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun GigCard(
    gig: Gig
) {
    OutlinedCard(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = gig.title?.takeIf { it.isNotBlank() } ?: "Untitled gig",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.SemiBold
            )

            gig.category?.takeIf { it.isNotBlank() }?.let { category ->
                Text(
                    text = category,
                    modifier = Modifier.padding(top = 4.dp),
                    color = MaterialTheme.colorScheme.primary,
                    style = MaterialTheme.typography.labelLarge
                )
            }

            gig.description?.takeIf { it.isNotBlank() }?.let { description ->
                Text(
                    text = description
                        .replace(Regex("<[^>]*>"), " ")
                        .replace(Regex("\\s+"), " ")
                        .trim()
                        .take(180)
                        .let { text -> if (text.length == 180) text + "…" else text },
                    modifier = Modifier.padding(top = 8.dp),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            val sellerName = gig.seller?.fullName?.takeIf { it.isNotBlank() }
                ?: "Student freelancer"

            Text(
                text = "By " + sellerName,
                modifier = Modifier.padding(top = 12.dp),
                style = MaterialTheme.typography.bodyMedium
            )

            val package = gig.packages.minByOrNull { it.price ?: Double.MAX_VALUE }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(12.dp)
                ) {
                    Text(
                        text = package?.tierName ?: "Service",
                        style = MaterialTheme.typography.labelLarge
                    )

                    Text(
                        text = package?.price?.let { "From ₹" + formatPrice(it) }
                            ?: "Pricing available in gig details",
                        modifier = Modifier.padding(top = 4.dp),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.SemiBold
                    )

                    package?.deliveryDays?.let { days ->
                        Text(
                            text = days.toString() + " day delivery",
                            modifier = Modifier.padding(top = 4.dp),
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}

private fun formatPrice(value: Double): String {
    return if (value % 1.0 == 0.0) {
        value.toInt().toString()
    } else {
        String.format("%.2f", value)
    }
}
