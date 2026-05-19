class SentimentLabel:
    def __init__(self):
        self.sentiment_labels = ["negative", "positive"]

    def id2label(self, topic_id)-> str:
        return self.sentiment_labels[topic_id]

    def label2id(self,label:str) -> int:
        return self.sentiment_labels.index(label)