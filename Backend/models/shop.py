from sqlalchemy import Column, Integer, String, ForeignKey, DateTime
from sqlalchemy.sql import func
from geoalchemy2 import Geometry
from database import Base

class Shop(Base):
    __tablename__ = "shops"

    id = Column(Integer, primary_key=True, index=True)
    owner_id = Column(Integer, ForeignKey("users.id"), nullable=False, index=True)
    business_name = Column(String, nullable=False)
    category_id = Column(Integer, ForeignKey("categories.id"), nullable=True)
    address = Column(String, nullable=True)
    location = Column(Geometry(geometry_type="POINT", srid=4326), nullable=True)
    created_at = Column(DateTime(timezone=True), server_default=func.now())