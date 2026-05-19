from sentiment_tokenizer import SentimentTokenizer

def main():
    test_texts = [
        "Cooking microwave pizzas, yummy",
    ]
    sentiment_tokenizer = SentimentTokenizer()
    tokenizered_input = sentiment_tokenizer.encode(test_texts)
    print("tokenizered_input")
    print(tokenizered_input)

if __name__ == '__main__':
    main()