from ai.analyzer.classification.classification_analyzer import ClassificationAnalyzer

def main():
    test_texts = [
        "Emmanuel Macron is the President of France",
        "A shock to oil supplies is rattling financial markets",
        "MIDDLE EAST LIVE 30 March: UN peacekeepers killed amid Israel-Hezbollah clashes"
    ]

    classification_analyzer = ClassificationAnalyzer()
    result = classification_analyzer.analyze_input(test_texts)
    print("classification_analyzer: ", result)   
if __name__ == '__main__':
    main()
