import os
from dotenv import load_dotenv

load_dotenv()

def get_postgres_config():
    postgres_host = os.getenv("POSTGRES_HOST")
    postgres_port = os.getenv("POSTGRES_PORT")
    postgres_dbname = os.getenv("POSTGRES_DBNAME")
    postgres_user = os.getenv("POSTGRES_USER")
    postgres_password = os.getenv("POSTGRES_PASSWORD")

    conn_params = {
        "host": postgres_host,
        "port": postgres_port,
        "dbname": postgres_dbname,
        "user": postgres_user,
        "password": postgres_password
    }

    return conn_params
