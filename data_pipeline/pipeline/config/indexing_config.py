import os
from dotenv import load_dotenv

load_dotenv()

def get_elasticsearch_config():
    elastic_config = {
        "elastic_url" : os.getenv("ELASTIC_URL"),
        "elastic_script_path" : os.getenv("ELASTIC_SCRIPT_PATH"),
        "root_folder": os.getenv("ELASTIC_SCRIPT_PATH")
    }
    return elastic_config