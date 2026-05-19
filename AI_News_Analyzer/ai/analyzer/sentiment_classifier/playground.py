from ai.analyzer.sentiment_classifier.sentiment_classifier import SentimentClassifier

def main():
    test_texts = [
        "Cooking microwave pizzas, yummy",
        "I hate you",
        "MC, happy mother`s day to your mom ;).. love yah"
    ]
    sentiment_analyzer = SentimentClassifier()
    result = sentiment_analyzer.analyze_input(test_texts)
    print("sentiment_analyzer", result)
    result_int8 = sentiment_analyzer.analyze_input_onnx(test_texts)
    print("sentiment_analyzer_int8", result_int8)
    

if __name__ == '__main__':
    main()
