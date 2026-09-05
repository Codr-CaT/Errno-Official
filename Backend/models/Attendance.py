from sqlalchemy import Column, Integer, ForeignKey, DateTime
from geoalchemy2 import Geometry
from database import Base

class Attendance(Base):
    __tablename__ = "attendance"

    id = Column(Integer, primary_key=True, index=True)
    booking_id = Column(Integer, ForeignKey("bookings.id"), nullable=False, unique=True, index=True)
    check_in_time = Column(DateTime(timezone=True), nullable=True)
    check_in_location = Column(Geometry(geometry_type="POINT", srid=4326), nullable=True)
    check_out_time = Column(DateTime(timezone=True), nullable=True)
    check_out_location = Column(Geometry(geometry_type="POINT", srid=4326), nullable=True)