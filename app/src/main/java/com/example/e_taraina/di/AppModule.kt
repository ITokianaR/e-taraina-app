package com.example.e_taraina.di

import com.example.e_taraina.data.datasource.AuthRemoteDataSource
import com.example.e_taraina.data.repository.AuthRepositoryImpl
import com.example.e_taraina.domain.repository.AuthRepository
import com.example.e_taraina.domain.usecase.LoginUseCase

// petit conteneur de dépendances fait main, pas de Hilt/Koin pour éviter
// d'ajouter des plugins Gradle en plus pour un projet aussi petit. Si ça
// grossit un jour on pourra basculer sur Hilt sans toucher au reste
object AppModule {
    private val authRemoteDataSource: AuthRemoteDataSource by lazy { AuthRemoteDataSource() }

    val authRepository: AuthRepository by lazy { AuthRepositoryImpl(authRemoteDataSource) }

    val loginUseCase: LoginUseCase by lazy { LoginUseCase(authRepository) }
}
