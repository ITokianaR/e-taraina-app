package com.example.e_taraina.ui.features.login

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.e_taraina.domain.models.UserRole
import com.example.e_taraina.ui.common.components.ETarainaButton
import com.example.e_taraina.ui.common.components.ETarainaTextField
import com.example.e_taraina.ui.common.theme.ETarainaGreen
import com.example.e_taraina.ui.viewmodel.LoginEvent
import com.example.e_taraina.ui.viewmodel.LoginViewModel

// écran de login. onLoginSuccess renvoie le username + le rôle, c'est le
// nav graph qui décide où on atterrit ensuite (Home-user ou Home-admin)
@Composable
fun LoginScreen(
    onLoginSuccess: (username: String, role: UserRole) -> Unit,
    viewModel: LoginViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        viewModel.events.collect { event ->
            when (event) {
                is LoginEvent.NavigateHome -> onLoginSuccess(event.username, event.role)
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.Start
    ) {
        Text(
            text = "E-taraina",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )

        Spacer(modifier = Modifier.height(40.dp))

        ETarainaTextField(
            value = uiState.username,
            onValueChange = viewModel::onUsernameChange,
            label = "Username",
            isError = uiState.errorMessage != null
        )

        Spacer(modifier = Modifier.height(12.dp))

        ETarainaTextField(
            value = uiState.password,
            onValueChange = viewModel::onPasswordChange,
            label = "Password",
            isPassword = true,
            isError = uiState.errorMessage != null,
            supportingText = uiState.errorMessage
        )

        Spacer(modifier = Modifier.height(24.dp))

        ETarainaButton(
            text = "Login",
            onClick = viewModel::onLoginClick,
            containerColor = ETarainaGreen,
            contentColor = Color.Black,
            isLoading = uiState.isLoading
        )
    }
}
