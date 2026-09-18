package com.example.e_taraina.data.datasource

import com.example.e_taraina.data.remote.model.LoginRequestDto
import com.example.e_taraina.data.remote.model.LoginResponseDto
import kotlinx.coroutines.delay

// mock en attendant le vrai backend. N'importe quel username/password
// non vide passe, et "admin" donne le rôle ADMIN, le reste USER.
// à remplacer par un vrai appel à AuthApiService plus tard, le reste
// de l'app n'aura rien à changer
class AuthRemoteDataSource {
    suspend fun login(request: LoginRequestDto): LoginResponseDto {
        delay(600) // simule la latence réseau

        if (request.password.length < 4) {
            throw IllegalArgumentException("Le mot de passe doit contenir au moins 4 caractères")
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
