package com.example.e_taraina.data.remote

import com.example.e_taraina.data.remote.model.LoginRequestDto
import com.example.e_taraina.data.remote.model.LoginResponseDto

/**
 * Contract for the real backend. Once the API is available, implement
 * this with Retrofit, e.g.:
 *
 * interface AuthApiService {
 *     @POST("auth/login")
 *     suspend fun login(@Body request: LoginRequestDto): LoginResponseDto
 * }
 *
 * and swap AuthRemoteDataSource's mock body for a real call to it.
 */
interface AuthApiService {
    suspend fun login(request: LoginRequestDto): LoginResponseDto
}
