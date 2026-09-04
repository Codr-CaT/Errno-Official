package com.erno.app.data.model

data class SendOtpRequest(
    val phoneNumber: String,
    val role: String? = null
)

data class SendOtpResponse(
    val success: Boolean,
    val message: String
)

data class VerifyOtpRequest(
    val phoneNumber: String,
    val otpCode: String
)

data class RegisterRequest(
    val fullName: String,
    val phoneNumber: String,
    val role: String
)

data class AuthResponse(
    val success: Boolean,
    val token: String? = null,
    val message: String? = null,
    val user: UserDto? = null
)

data class UserDto(
    val id: String,
    val name: String?,
    val phoneNumber: String,
    val role: String
)
