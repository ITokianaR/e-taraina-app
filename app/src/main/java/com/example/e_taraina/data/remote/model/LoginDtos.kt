package com.example.e_taraina.data.remote.model

/**
 * Wire-format models for the login endpoint.
 * Kept separate from domain.models.User so the API shape can change
 * without touching the rest of the app.
 */
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
