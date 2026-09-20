package com.skilllaunch.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.skilllaunch.app.core.common.collectAsStateWithLifecycleCompat
import com.skilllaunch.app.core.network.ApiClient
import com.skilllaunch.app.core.session.SessionStore
import com.skilllaunch.app.data.repository.auth.AuthRepository
import com.skilllaunch.app.feature.auth.AuthViewModel
import com.skilllaunch.app.feature.auth.LoginScreen
import com.skilllaunch.app.feature.auth.SignupScreen
import com.skilllaunch.app.ui.theme.SkillLaunchTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SkillLaunchTheme {
                SkillLaunchRoot()
            }
        }
    }
}

@Composable
private fun SkillLaunchRoot() {
    val context = androidx.compose.ui.platform.LocalContext.current
    val showSignup = remember { androidx.compose.runtime.mutableStateOf(false) }

    val sessionStore = remember {
        SessionStore(context.applicationContext)
    }

    val authApi = remember(sessionStore) {
        ApiClient.authApi(sessionStore)
    }

    val authRepository = remember(authApi, sessionStore) {
        AuthRepository(
            authApi = authApi,
            sessionStore = sessionStore
        )
    }

    val authViewModel: AuthViewModel = viewModel(
        factory = AuthViewModel.factory(
            repository = authRepository,
            sessionStore = sessionStore
        )
    )

    val state by authViewModel.uiState.collectAsStateWithLifecycleCompat()

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        when {
            state.isCheckingSession -> SessionCheckingScreen()
            state.isAuthenticated -> SkillLaunchHome(
                username = state.user?.firstName ?: state.user?.username ?: "there"
            )
            else -> if (showSignup.value) {
                SignupScreen(
                    state = state,
                    onSignup = authViewModel::signup,
                    onBackToLogin = { showSignup.value = false }
                )
            } else {
                LoginScreen(
                    state = state,
                    onLogin = authViewModel::login,
                    onCreateAccount = { showSignup.value = true }
                )
            }
        }
    }
}

@Composable
private fun SessionCheckingScreen() {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        CircularProgressIndicator()
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = "Checking your SkillLaunch session…",
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun SkillLaunchHome(username: String) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp, vertical = 28.dp)
    ) {
        Text(
            text = "Good to see you, $username",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = "Your SkillLaunch home",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(24.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            MobileHomeCard(
                title = "Explore",
                subtitle = "Discover gigs and jobs",
                modifier = Modifier.weight(1f)
            )

            MobileHomeCard(
                title = "Orders",
                subtitle = "Track active work",
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        MobileHomeCard(
            title = "Inbox",
            subtitle = "Continue your conversations",
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun MobileHomeCard(
    title: String,
    subtitle: String,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        tonalElevation = 2.dp
    ) {
        Column(
            modifier = Modifier.padding(18.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}