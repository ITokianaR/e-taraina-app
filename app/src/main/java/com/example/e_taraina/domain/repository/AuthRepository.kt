package com.example.e_taraina.domain.repository

import com.example.e_taraina.domain.models.User

/**
 * Domain-layer contract. The UI/domain layers depend on this
 * interface only — never on the concrete data-layer implementation.
 */
interface AuthRepository {
    suspend fun login(username: String, password: String): Result<User>
}
