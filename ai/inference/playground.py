import json

from ai.inference.inference_service.inference_processor import InferenceProcessor
from ai.responses.inference_news_response import InferenceNewsResponse


def main():
    test_texts = [
        {'title': 'Breaking the Gaza aid bottleneck: 106-tonne delivery arrives via new sea route',
         'publish_date': 1775858400.0, 'source': 'UN', 'link': 'https://news.un.org/feed/view/en/story/2026/04/1167235',
         'language': 'en',
         'text': 'The consignment through the WHO Humanitarian Bridge Initiative in Cyprus arrived at Ashdod port in Israel and is being prepared for onward distribution to the devastated enclave.\n\n“This shipment marks a significant operational milestone in strengthening WHO’s interregional humanitarian logistics capacity for a region affected by the ongoing conflict, particularly in Gaza,” the UN agency said.'}
    ]
    inference_service = InferenceProcessor()
    inference_result = inference_service.analyze(test_texts).results
    for res in inference_result:
        inference_news = InferenceNewsResponse(
            link=res.link,
            summary=res.summarization,
            sentiment_label=res.sentiment.label,
            sentiment=res.sentiment.score,
            topic=res.classification.topic,
            entities=res.ner.entities
        )
        # print("Playground", inference_news)
        print("Encode: ", inference_news.model_dump_json().encode())
        encoded = inference_news.model_dump_json().encode()
        print("Load: ", json.loads(encoded.decode()))


if __name__ == '__main__':
    main()
