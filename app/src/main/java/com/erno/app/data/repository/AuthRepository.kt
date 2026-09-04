package com.erno.app.data.repository

import com.erno.app.data.model.AuthResponse
import com.erno.app.data.model.RegisterRequest
import com.erno.app.data.model.SendOtpRequest
import com.erno.app.data.model.SendOtpResponse
import com.erno.app.data.model.VerifyOtpRequest
import com.erno.app.data.remote.ErnoApiService
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

interface AuthRepository {
    fun sendOtp(phoneNumber: String, role: String? = null): Flow<Result<SendOtpResponse>>
    fun verifyOtp(phoneNumber: String, otpCode: String): Flow<Result<AuthResponse>>
    fun register(fullName: String, phoneNumber: String, role: String): Flow<Result<AuthResponse>>
}

class AuthRepositoryImpl(
    private val apiService: ErnoApiService? = null
) : AuthRepository {

    override fun sendOtp(phoneNumber: String, role: String?): Flow<Result<SendOtpResponse>> = flow {
        try {
            if (apiService != null) {
                val response = apiService.sendOtp(SendOtpRequest(phoneNumber, role))
                emit(Result.success(response))
            } else {
                // Default local simulation if API service is not yet initialized
                emit(Result.success(SendOtpResponse(success = true, message = "OTP sent successfully")))
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun verifyOtp(phoneNumber: String, otpCode: String): Flow<Result<AuthResponse>> = flow {
        try {
            if (apiService != null) {
                val response = apiService.verifyOtp(VerifyOtpRequest(phoneNumber, otpCode))
                emit(Result.success(response))
            } else {
                // Local verification check (validates non-empty 6-digit OTP)
                if (otpCode.length == 6) {
                    emit(
                        Result.success(
                            AuthResponse(
                                success = true,
                                token = "demo_jwt_token",
                                message = "OTP Verified Successfully"
                            )
                        )
                    )
                } else {
                    emit(Result.failure(IllegalArgumentException("Invalid OTP code")))
                }
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }

    override fun register(fullName: String, phoneNumber: String, role: String): Flow<Result<AuthResponse>> = flow {
        try {
            if (apiService != null) {
                val response = apiService.register(RegisterRequest(fullName, phoneNumber, role))
                emit(Result.success(response))
            } else {
                emit(
                    Result.success(
                        AuthResponse(
                            success = true,
                            message = "Account created successfully"
                        )
                    )
                )
            }
        } catch (e: Exception) {
            emit(Result.failure(e))
        }
    }
}
