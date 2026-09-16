package com.example.e_taraina.domain.models

// utilisateur connecté. role sert à savoir si on l'envoie sur
// Home-user ou Home-admin après le login
data class User(
    val id: String,
    val username: String,
    val role: UserRole
)

enum class UserRole {
    USER,
    ADMIN
}