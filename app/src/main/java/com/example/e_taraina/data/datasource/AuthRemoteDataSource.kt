package com.example.e_taraina.data.datasource

import com.example.e_taraina.data.remote.model.LoginRequestDto
import com.example.e_taraina.data.remote.model.LoginResponseDto
import kotlinx.coroutines.delay

/**
 * Temporary in-memory data source so the login flow is fully
 * functional before the real backend exists.
 *
 * Behaviour: any non-blank username/password succeeds. Logging in
 * with the username "admin" returns the ADMIN role (routes to
 * Home-admin); anything else returns USER (routes to Home-user).
 *
 * Replace the body of [login] with a real call to [AuthApiService]
 * once the backend endpoint is ready — nothing above this layer needs
 * to change.
 */
class AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        delay(600) // simulate network latency

        if (request.password.length < 4) {
            throw IllegalArgumentException("Password must be at least 4 characters")
        }

        val role = if (request.username.equals("admin", ignoreCase = true)) "ADMIN" else "USER"
        return LoginResponseDto(
            id = "1",
            username = request.username,
            role = role,
            token = "mock-token"
        )
    }
}
