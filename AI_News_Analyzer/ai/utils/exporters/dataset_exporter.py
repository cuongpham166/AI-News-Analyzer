import pandas as pd

df = pd.read_csv("ai/dataset/classification/final_train_dataset.csv")
print("Original columns:", df.columns)

min_size = df["label"].value_counts().min()

df["label"] = df["label"].replace({
    "sport": "sports",
    "tech": "technology",
    "nation": "politics"
})

print(df["label"].value_counts())

balanced_df = (
    df.groupby("label")
      .sample(n=min_size, random_state=42)
      .reset_index(drop=True)
)

# Debug checks
print("Balanced columns:", balanced_df.columns)
print(balanced_df["label"].value_counts())

# Export
balanced_df.to_csv("ai/dataset/classification/balanced_news_dataset.csv", index=False)