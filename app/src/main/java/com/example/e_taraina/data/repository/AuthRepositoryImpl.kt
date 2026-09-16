package com.example.e_taraina.data.repository

import com.example.e_taraina.data.datasource.AuthRemoteDataSource
import com.example.e_taraina.data.remote.model.LoginRequestDto
import com.example.e_taraina.domain.models.User
import com.example.e_taraina.domain.models.UserRole
import com.example.e_taraina.domain.repository.AuthRepository

class AuthRepositoryImpl(
    private val remoteDataSource: AuthRemoteDataSource
) : AuthRepository {

    override suspend fun login(username: String, password: String): Result<User> {
        return try {
            val response = remoteDataSource.login(LoginRequestDto(username, password))
            val role = if (response.role == "ADMIN") UserRole.ADMIN else UserRole.USER
            Result.success(User(id = response.id, username = response.username, role = role))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
