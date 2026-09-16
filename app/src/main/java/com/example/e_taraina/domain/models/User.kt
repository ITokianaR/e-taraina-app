package com.example.e_taraina.domain.models

/**
 * Domain-level representation of an authenticated user.
 * The [role] decides whether the app routes to the user home
 * (Home-user) or the admin home (Home-admin) after login.
 */
data class User(
    val id: String,
    val username: String,
    val role: UserRole
)

enum class UserRole {
    USER,
    ADMIN
}