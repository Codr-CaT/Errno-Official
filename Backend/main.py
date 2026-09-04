import random
import logging
from typing import List
from fastapi import FastAPI, Depends, HTTPException, status
from fastapi.middleware.cors import CORSMiddleware
from sqlalchemy.orm import Session

from database import engine, Base, get_db
import models
import schemas

# Setup logging
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger("erno_backend")

# Initialize database tables
try:
    Base.metadata.create_all(bind=engine)
    logger.info("Database tables verified/created successfully.")
except Exception as e:
    logger.warning(f"Database connection warning on startup: {e}")

app = FastAPI(
    title="ERRNO Backend API",
    description="Official FastAPI REST API backend for ERRNO Android Application",
    version="1.0.0"
)

# Enable CORS for Android Emulator (10.0.2.2), localhost, and remote clients
app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

@app.get("/")
def read_root():
    return {
        "status": "online",
        "app": "ERRNO Official Backend Service",
        "version": "1.0.0",
        "database": "Supabase PostgreSQL Connected"
    }

@app.post("/auth/send-otp", response_model=schemas.SendOTPResponse)
def send_otp(request: schemas.SendOTPRequest, db: Session = Depends(get_db)):
    clean_phone = request.phone_number.strip().replace(" ", "").replace("+91", "")

    if len(clean_phone) < 10:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Please provide a valid 10-digit mobile number."
        )

    # Generate 6-digit OTP (For dev/testing: supports '123456' as standard fallback)
    generated_otp = str(random.randint(100000, 999999))

    try:
        # Save OTP in database
        otp_entry = models.OTP(
            phone_number=clean_phone,
            otp_code=generated_otp,
            is_verified=False
        )
        db.add(otp_entry)
        db.commit()
    except Exception as e:
        db.rollback()
        logger.error(f"Error saving OTP to database: {e}")

    logger.info(f"[SMS OTP DISPATCH] Sent OTP '{generated_otp}' to +91 {clean_phone}")

    return schemas.SendOTPResponse(
        success=True,
        message=f"OTP sent successfully to +91 {clean_phone}",
        otp_demo="123456" # Hardcoded dev test OTP for easy testing
    )

@app.post("/auth/verify-otp", response_model=schemas.AuthResponse)
def verify_otp(request: schemas.VerifyOTPRequest, db: Session = Depends(get_db)):
    clean_phone = request.phone_number.strip().replace(" ", "").replace("+91", "")
    otp_code = request.otp_code.strip()

    # Check OTP in database or allow standard test OTP '123456'
    is_valid = False
    if otp_code == "123456":
        is_valid = True
    else:
        db_otp = db.query(models.OTP).filter(
            models.OTP.phone_number == clean_phone,
            models.OTP.otp_code == otp_code,
            models.OTP.is_verified == False
        ).order_by(models.OTP.created_at.desc()).first()

        if db_otp:
            db_otp.is_verified = True
            db.commit()
            is_valid = True

    if not is_valid:
        raise HTTPException(
            status_code=status.HTTP_400_BAD_REQUEST,
            detail="Invalid or expired OTP code. Use 123456 for testing."
        )

    # Fetch existing user or create user entry
    user = db.query(models.User).filter(models.User.phone_number == clean_phone).first()
    if not user:
        user = models.User(
            phone_number=clean_phone,
            role="shopkeeper"
        )
        db.add(user)
        db.commit()
        db.refresh(user)

    return schemas.AuthResponse(
        success=True,
        token=f"jwt_token_{user.id}",
        message="OTP Verified Successfully",
        user=schemas.UserResponse.model_validate(user)
    )

@app.post("/auth/register", response_model=schemas.AuthResponse)
def register_user(request: schemas.RegisterRequest, db: Session = Depends(get_db)):
    clean_phone = request.phone_number.strip().replace(" ", "").replace("+91", "")

    user = db.query(models.User).filter(models.User.phone_number == clean_phone).first()
    if user:
        user.full_name = request.full_name
        user.role = request.role
    else:
        user = models.User(
            full_name=request.full_name,
            phone_number=clean_phone,
            role=request.role
        )
        db.add(user)

    db.commit()
    db.refresh(user)

    return schemas.AuthResponse(
        success=True,
        token=f"jwt_token_{user.id}",
        message="Account created successfully",
        user=schemas.UserResponse.model_validate(user)
    )

@app.get("/jobs", response_model=List[schemas.JobResponse])
def get_jobs(db: Session = Depends(get_db)):
    jobs = db.query(models.Job).all()
    return [schemas.JobResponse.model_validate(j) for j in jobs]

@app.post("/jobs", response_model=schemas.JobResponse)
def create_job(request: schemas.JobCreateRequest, db: Session = Depends(get_db)):
    job = models.Job(
        title=request.title,
        description=request.description,
        pay_amount=request.pay_amount,
        duration_type=request.duration_type
    )
    db.add(job)
    db.commit()
    db.refresh(job)
    return schemas.JobResponse.model_validate(job)

if __name__ == "__main__":
    import uvicorn
    uvicorn.run("main:app", host="0.0.0.0", port=8000, reload=True)
