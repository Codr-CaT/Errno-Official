from sqlalchemy import Column, Integer, String, ForeignKey, DateTime, Numeric, Text
from sqlalchemy.sql import func
from geoalchemy2 import Geometry
from database import Base

class Job(Base):
    __tablename__ = "jobs"

    id = Column(Integer, primary_key=True, index=True)
    shop_id = Column(Integer, ForeignKey("shops.id"), nullable=False, index=True)
    category_id = Column(Integer, ForeignKey("categories.id"), nullable=False)
    worker_count = Column(Integer, nullable=False)
    date = Column(DateTime(timezone=True), nullable=False)
    start_time = Column(DateTime(timezone=True), nullable=False)
    duration_hours = Column(Numeric(4, 1), nullable=False)
    pay_amount = Column(Numeric(10, 2), nullable=False)
    description = Column(Text, nullable=True)
    radius_km = Column(Integer, default=5, nullable=False)
    location = Column(Geometry(geometry_type="POINT", srid=4326), nullable=True)
    status = Column(String, default="open", nullable=False)  # open, filled, cancelled, completed
    created_at = Column(DateTime(timezone=True), server_default=func.now())