export interface GlobalTrend {
  date: string;
  articleCount: number;
  averageSentiment: number;
  topTopics: Record<string, number>;
}
