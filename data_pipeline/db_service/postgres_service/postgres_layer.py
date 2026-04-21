import psycopg
from datetime import datetime
import os
from dotenv import load_dotenv
load_dotenv()
query_folder_path = os.getenv("SQL_QUERY_FOLDER_PATH")

root_folder = query_folder_path

class PostgresLayer:
    def __init__(self, config):
        self.config = config
        self.conn = None

    def connect(self):
        self.conn = psycopg.connect(**self.config)

    def close(self):
        if self.conn:
            self.conn.close()

    def check_connection(self):
        try:
            with self.conn.cursor() as cur:
                cur.execute("SELECT 1;")
                result = cur.fetchone()
                if result:
                    print("Connection successful! Result:", result[0])
                else:
                    print("Connection made, but no result returned.")
        except psycopg.Error as e:
            print("Unable to connect to the database")
            print("Error:", e)

    def create_entity_type_table(self):
        try:
            sql_file = root_folder+"create_entity_type_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql)
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)

    def create_entity_table(self):
        try:
            sql_file = root_folder+"create_entity_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql)
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)

    def create_source_table(self):
        try:
            sql_file = root_folder+"create_source_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql)
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)
            
    def create_topic_table(self):
        try:
            sql_file = root_folder+"create_topic_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql)
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)

    def create_news_table(self):
        try:
            sql_file = root_folder+"create_news_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql)
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)

    def create_news_entity_table(self):
        try:
            sql_file = root_folder+"create_news_entity_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql)
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)

    def insert_entity_type_data(self,entity_types):
        try:
            sql_file = root_folder+"insert_entity_type_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.executemany(sql,[(et,) for et in entity_types])
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()

    def insert_entity_data(self, entities):
        try:
            sql_file = root_folder+"insert_entity_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                for entity in entities:
                    cur.execute(sql,(entity["value"], entity["entity_type"]))
                self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()

    def insert_source_data(self,sources):
        try:
            sql_file = root_folder+"insert_source_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.executemany(sql,[(source,) for source in sources])
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()

    def insert_topic_data(self,topics):
        try:
            sql_file = root_folder+"insert_topic_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.executemany(sql,[(topic,) for topic in topics])
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()

    
    def insert_news_data(self,news):
        try:
            self.insert_source_data([news["source"]])

            sql_file = root_folder+"insert_news_table.sql"
            publish_date = news["publish_date"]

            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql,(news["title"],publish_date,news["link"],news["language"],news["text"], news["source"]))
                row = cur.fetchone()
                if row is not None:
                    new_id = row[0]
                else:
                    new_id = -1           
            self.conn.commit()
            return new_id
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()

    def insert_news_entity_data(self,news_link,news_entity):
        try:
            sql_file = root_folder+"insert_news_entity_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                cur.execute(sql,(news_link, news_entity))
            self.conn.commit()
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()

    #insert entity_type => insert entity => update_news with AI results => insert_news_entity
    def update_news_data(self,updated_data):
        try:
            if len(updated_data["entities"]) > 0:
                entity_types = [entity["entity_type"] for entity in updated_data["entities"]]
                entities = updated_data["entities"]
                self.insert_entity_type_data(entity_types)
                self.insert_entity_data(entities)

            sql_file = root_folder+"update_news_table.sql"
            with open(sql_file, "r") as f:
                sql = f.read()
            with self.conn.cursor() as cur:
                #summary=%s, sentiment_label=%s, sentiment=%s, topic_id
                cur.execute(sql,(updated_data["summary"], updated_data["sentiment_label"], updated_data["sentiment"], updated_data["topic"],updated_data["link"]))
            self.conn.commit()

            entities_list = [entity["value"] for entity in updated_data["entities"]]

            for entity_element in entities_list:
                self.insert_news_entity_data(updated_data["link"],entity_element)
        except psycopg.Error as e:
            print("Error", e)
            self.conn.rollback()


    def fetch_all_data(self, table_name):
        match table_name:
            case "entity_type":
                sql_file = root_folder+"get_entity_type_table.sql"
            case "entity":
                sql_file = root_folder+"get_entity_table.sql"
            case "news_source":
                sql_file = root_folder+"get_source_table.sql"
            case "topic":
                sql_file = root_folder+"get_topic_table.sql"
            case "news":
                sql_file = root_folder+"get_news_table.sql"
            case _:
                sql_file = root_folder+"get_entity_type_table.sql"

        with open(sql_file, "r") as f:
            sql = f.read()
            
        with self.conn.cursor() as cur:
            cur.execute(sql)
            return cur.fetchall()