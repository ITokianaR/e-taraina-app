package com.example.e_taraina.di

import com.example.e_taraina.data.datasource.AuthRemoteDataSource
import com.example.e_taraina.data.repository.AuthRepositoryImpl
import com.example.e_taraina.domain.repository.AuthRepository
import com.example.e_taraina.domain.usecase.LoginUseCase

/**
 * Lightweight manual dependency container.
 *
 * This is intentionally a simple singleton object rather than Hilt/Koin
 * so it drops into an existing project with zero extra Gradle plugins.
 * If the project grows, swap this for Hilt: the wiring is isolated here,
 * so nothing else in the app needs to change.
 */
object AppModule {
    private val authRemoteDataSource: AuthRemoteDataSource by lazy { AuthRemoteDataSource() }

    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(authRemoteDataSource) }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
}
