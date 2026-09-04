package com.erno.app.data.remote

import com.erno.app.data.model.AuthResponse
import com.erno.app.data.model.RegisterRequest
import com.erno.app.data.model.SendOtpRequest
import com.erno.app.data.model.SendOtpResponse
import com.erno.app.data.model.VerifyOtpRequest

/**
 * Retrofit interface for ERNO API service.
 */
interface ErnoApiService {

    suspend fun sendOtp(request: SendOtpRequest): SendOtpResponse

    suspend fun verifyOtp(request: VerifyOtpRequest): AuthResponse

    suspend fun register(request: RegisterRequest): AuthResponse
}
