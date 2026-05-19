from ai.analyzer.topic_classifier.topic_classifier import TopicClassifier

def main():
    test_texts = [
        "Emmanuel Macron is the President of France",
        "A shock to oil supplies is rattling financial markets",
        "MIDDLE EAST LIVE 30 March: UN peacekeepers killed amid Israel-Hezbollah clashes"
    ]

    news_classifier = TopicClassifier()
    result = news_classifier.classify(test_texts)
    print("news_classifier: ", result)
    result_int8 = news_classifier.classify_onnx(test_texts)
    print("news_classifier_int8: ", result_int8)

if __name__ == '__main__':
    main()
