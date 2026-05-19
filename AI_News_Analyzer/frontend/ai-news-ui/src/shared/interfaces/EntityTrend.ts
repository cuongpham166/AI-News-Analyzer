export interface EntityTrend {
  timeline: Timeline;
}

interface Timeline {
  [date: string]: TimelineItem[]; // keys are date strings, values are arrays of TimelineItem
}

interface TimelineItem {
  name: string;
  count: number;
  averageSentiment: number;
}

export interface EntityTrendsChartData {
  children: EntityEntry[];
  name: string;
}
interface EntityEntry {
  name: string;
  size: number;
  sentiment: number;
}
