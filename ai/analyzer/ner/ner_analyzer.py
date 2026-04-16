from typing import List, Dict
import torch
from gliner import GLiNER

pytorch_model_dir = 'ai/models/ner/pytorch'

class NERAnalyzer:
    def __init__(self):
        self.model = GLiNER.from_pretrained(pytorch_model_dir,local_files_only=True)
        self.device = torch.device("cuda" if torch.cuda.is_available() else "cpu")
        self.model.to(self.device)
        self.model.eval()

    def save(self):
        self.model.save_pretrained("ai/models/ner/pytorch")
        
    def analyze_input(self, articles: List[str]):
        prediction_result = []
        entity_types = ["person", "organization", "location", "date", "event", "product", "money", "percent", "title"]
    
        for article in articles:
            prediction_records = []
            entities = self.model.predict_entities(article, entity_types, threshold=0.3)
            for entity in entities:
                prediction_record = {
                    "value":entity["text"],
                    "entity_type":entity["label"]
                }
                prediction_records.append(prediction_record)
            prediction_result.append(prediction_records)
            
        return prediction_result