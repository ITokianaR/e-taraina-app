package com.example.e_taraina.domain.usecase

import com.example.e_taraina.domain.models.User
import com.example.e_taraina.domain.repository.AuthRepository

/**
 * Encapsulates the "log a user in" business rule so the ViewModel
 * stays free of validation/repository details.
 */
class LoginUseCase(
    private val repository: AuthRepository
) {
    suspend operator fun invoke(username: String, password: String): Result<User> {
        if (username.isBlank() || password.isBlank()) {
            return Result.failure(IllegalArgumentException("Username and password can't be empty"))
        }
        return repository.login(username.trim(), password)
    }
}
