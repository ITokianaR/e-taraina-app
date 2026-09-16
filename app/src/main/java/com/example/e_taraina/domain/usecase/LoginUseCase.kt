package com.example.e_taraina.domain.usecase

import com.example.e_taraina.domain.models.User
import com.example.e_taraina.domain.repository.AuthRepository

// logique du login mise ici plutôt que dans le ViewModel, comme ça il
// reste simple et ne fait que gérer l'état de l'écran
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
