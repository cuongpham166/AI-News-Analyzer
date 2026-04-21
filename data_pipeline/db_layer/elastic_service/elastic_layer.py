from elasticsearch import Elasticsearch,exceptions,helpers
from datetime import datetime, timezone

import os
from dotenv import load_dotenv
load_dotenv()
elastic_url = os.getenv("ELASTIC_URL")
elastic_script_path= os.getenv("ELASTIC_SCRIPT_PATH")
root_folder = elastic_script_path

class ElasticLayer:
    def __init__(self):
        self.es_client = Elasticsearch(elastic_url)
    
    def check_connection(self):
        print("Check connection: ", self.es_client.info())

    def create_index(self, index_name:str, mapping:dict):
        if not self.es_client.exists(index=index_name):
            self.es_client.create(index=index_name, body=mapping)
            print(f"Index '{index_name}' created")
        else:
            print(f"Index '{index_name}' already exists")

    def create_news_index(self):
        index_name = "news"
        elastic_file = root_folder+"news_mappings.json"
        with open(elastic_file, "r") as f:
            news_mapping = f.read()
            self.create_index(index_name,news_mapping)

    def index_news_document(self,document):
        doc_id = document['link']
        try:
            response = self.es_client.index(index="news", id=doc_id, document=document, op_type="create")
            return response
        except exceptions.ConflictError:
            print("Document already exists, skipping insertion")

    def get_all_news_documents(self):
        news_documents = []
        response = self.es_client.search(index="news", query={"match_all": {}})
        for hit in response['hits']['hits']:
            news_documents.append(hit["_source"])
        return news_documents 

    def get_news_documents(self,link:str):
        response = self.es_client.get(index="news", id=link)
        return response["_source"]

    def delete_news_document(self, link:str):
        try:
            self.es_client.delete(index="news", id=link)
        except Exception as e:
            print("Error deleting document: ",e)