export interface ImpactNews {
  timestamp: string;
  sentimentLabel: string;
  sentiment: number;
  topic: string;
  summary: string;
  link: string;
  publish_date: number;
  title: string;
  source: string;
  entites: Array<Entity>;
}

interface Entity {
  value: string;
  entityType: string;
}
