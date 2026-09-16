package com.example.e_taraina.data.remote.model

// modèles pour l'API de login, séparés de domain.models.User pour pouvoir
// changer le format de l'API sans impacter le reste de l'app
data class LoginRequestDto(
    val username: String,
    val password: String
)

data class LoginResponseDto(
    val id: String,
    val username: String,
    val role: String,
    val token: String
)
