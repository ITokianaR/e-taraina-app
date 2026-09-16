package com.example.e_taraina.data.remote

import com.example.e_taraina.data.remote.model.LoginRequestDto
import com.example.e_taraina.data.remote.model.LoginResponseDto

// contrat pour le vrai backend, pas encore implémenté. Genre plus tard
// avec Retrofit :
// @POST("auth/login")
// suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
// et on branche AuthRemoteDataSource dessus au lieu du mock
interface AuthApiService {
    suspend fun login(request: LoginRequestDto): LoginResponseDto
}
