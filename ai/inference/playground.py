from ai.inference.inference_service import InferenceService

def main():
    test_texts = [
        {'title': 'Breaking the Gaza aid bottleneck: 106-tonne delivery arrives via new sea route', 'publish_date': 1775858400.0, 'source': 'UN', 'link': 'https://news.un.org/feed/view/en/story/2026/04/1167235', 'language': 'en', 'text': 'The consignment through the WHO Humanitarian Bridge Initiative in Cyprus arrived at Ashdod port in Israel and is being prepared for onward distribution to the devastated enclave.\n\n“This shipment marks a significant operational milestone in strengthening WHO’s interregional humanitarian logistics capacity for a region affected by the ongoing conflict, particularly in Gaza,” the UN agency said.'}
    ]
    inference_service = InferenceService()
    result = inference_service.analyze(test_texts)
    print("result",result)

if __name__ == '__main__':
    main()