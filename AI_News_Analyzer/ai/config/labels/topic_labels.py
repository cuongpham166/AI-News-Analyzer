class TopicLabel:
    def __init__(self):
        self.topic_labels = [
            "economy",
            "entertainment",
            "health",
            "politics",
            "science",
            "sports",
            "technology",
            "world"
        ]

    def id2label(self, topic_id)-> str:
        return self.topic_labels[topic_id]

    def label2id(self,label:str) -> int:
        return self.topic_labels.index(label)