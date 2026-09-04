from pydantic import BaseModel, Field
from typing import Optional

class SendOTPRequest(BaseModel):
    phone_number: str = Field(..., example="9876543210")
    role: Optional[str] = Field(default="shopkeeper", example="shopkeeper")

class SendOTPResponse(BaseModel):
    success: bool
    message: str
    otp_demo: Optional[str] = None  # Dev demo code (e.g. 123456 or generated OTP)

class VerifyOTPRequest(BaseModel):
    phone_number: str = Field(..., example="9876543210")
    otp_code: str = Field(..., example="123456")

class UserResponse(BaseModel):
    id: str
    full_name: Optional[str] = None
    phone_number: str
    role: str

    class Config:
        from_attributes = True

class AuthResponse(BaseModel):
    success: bool
    token: Optional[str] = None
    message: Optional[str] = None
    user: Optional[UserResponse] = None

class RegisterRequest(BaseModel):
    full_name: str = Field(..., example="John Doe")
    phone_number: str = Field(..., example="9876543210")
    role: str = Field(..., example="shopkeeper")

class JobCreateRequest(BaseModel):
    title: str
    description: Optional[str] = None
    pay_amount: float
    duration_type: str = "hourly"

class JobResponse(BaseModel):
    id: str
    title: str
    description: Optional[str] = None
    pay_amount: float
    duration_type: str
    status: str

    class Config:
        from_attributes = True
