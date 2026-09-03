from sqlalchemy import Column, Integer, ForeignKey, DateTime, Text, CheckConstraint
from sqlalchemy.sql import func
from database import Base

class Rating(Base):
    __tablename__ = "ratings"
    __table_args__ = (
        CheckConstraint("stars >= 1 AND stars <= 5", name="check_stars_range"),
    )

    id = Column(Integer, primary_key=True, index=True)
    booking_id = Column(Integer, ForeignKey("bookings.id"), nullable=False, index=True)
    rated_by_user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    rated_user_id = Column(Integer, ForeignKey("users.id"), nullable=False)
    stars = Column(Integer, nullable=False)
    feedback = Column(Text, nullable=True)
    created_at = Column(DateTime(timezone=True), server_default=func.now())