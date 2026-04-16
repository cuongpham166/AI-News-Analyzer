from ai.analyzer.sentiment.sentiment_analyzer import SentimentAnalyzer

def main():
    test_texts = [
        "Cooking microwave pizzas, yummy",
        "I hate you"
    ]
    sentiment_analyzer = SentimentAnalyzer()
    result = sentiment_analyzer.analyze_input(test_texts)
    print("sentiment_analyzer", result)
    

if __name__ == '__main__':
    main()
