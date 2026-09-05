import os
from dotenv import load_dotenv
from urllib.parse import quote_plus
from sqlalchemy import create_engine, text

load_dotenv()

# Temporarily hardcode these to test (don't commit this)
password = "0vMhSKktVotlQyxF"  # exactly as Supabase gave it, unencoded
encoded_password = quote_plus(password)

DATABASE_URL = f"postgresql://postgres.rzbfmoccdntryiiiikzj:0vMhSKktVotlQyxF@aws-0-ap-northeast-2.pooler.supabase.com:5432/postgres"


print("Connecting with:", DATABASE_URL.replace(encoded_password, "****"))

engine = create_engine(DATABASE_URL)

try:
    with engine.connect() as connection:
        result = connection.execute(text("SELECT PostGIS_Version();"))
        print("✅ Connected successfully!")
        print("PostGIS version:", result.fetchone()[0])
except Exception as e:
    print("❌ Connection failed:")
    print(e)