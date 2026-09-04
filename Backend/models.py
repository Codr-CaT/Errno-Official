import uuid
from datetime import datetime, timezone
from sqlalchemy import Column, String, Boolean, DateTime, ForeignKey, Text, Float
from sqlalchemy.dialects.postgresql import UUID
from sqlalchemy.orm import relationship
from database import Base

def generate_uuid():
    return str(uuid.uuid4())

class User(Base):
    __tablename__ = "users"

    id = Column(String, primary_key=True, default=generate_uuid)
    full_name = Column(String, nullable=True)
    phone_number = Column(String, unique=True, index=True, nullable=False)
    role = Column(String, nullable=False, default="shopkeeper")  # shopkeeper or worker
    is_active = Column(Boolean, default=True)
    created_at = Column(DateTime, default=lambda: datetime.now(timezone.utc))

class OTP(Base):
    __tablename__ = "otps"

    id = Column(String, primary_key=True, default=generate_uuid)
    phone_number = Column(String, index=True, nullable=False)
    otp_code = Column(String, nullable=False)
    created_at = Column(DateTime, default=lambda: datetime.now(timezone.utc))
    is_verified = Column(Boolean, default=False)

class Shop(Base):
    __tablename__ = "shops"

    id = Column(String, primary_key=True, default=generate_uuid)
    owner_id = Column(String, ForeignKey("users.id"), nullable=False)
    shop_name = Column(String, nullable=False)
    address = Column(Text, nullable=True)
    category = Column(String, nullable=True)
    created_at = Column(DateTime, default=lambda: datetime.now(timezone.utc))

class Job(Base):
    __tablename__ = "jobs"

    id = Column(String, primary_key=True, default=generate_uuid)
    shop_id = Column(String, ForeignKey("shops.id"), nullable=True)
    title = Column(String, nullable=False)
    description = Column(Text, nullable=True)
    pay_amount = Column(Float, nullable=False, default=0.0)
    duration_type = Column(String, nullable=False, default="hourly")
    status = Column(String, nullable=False, default="open")  # open, assigned, completed
    created_at = Column(DateTime, default=lambda: datetime.now(timezone.utc))
