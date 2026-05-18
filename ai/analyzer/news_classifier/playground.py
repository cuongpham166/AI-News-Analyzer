from ai.analyzer.news_classifier.news_classifier import NewsClassifier

def main():
    test_texts = [
        "Emmanuel Macron is the President of France",
        "A shock to oil supplies is rattling financial markets",
        "MIDDLE EAST LIVE 30 March: UN peacekeepers killed amid Israel-Hezbollah clashes"
    ]

    news_classifier = NewsClassifier()
    result = news_classifier.classify(test_texts)
    print("news_classifier: ", result)   

if __name__ == '__main__':
    main()
