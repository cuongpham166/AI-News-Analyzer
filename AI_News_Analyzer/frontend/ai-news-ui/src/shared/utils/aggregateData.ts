import type { EntityTrend } from '../interfaces/EntityTrend';

export const aggregateEntities = (entityTrendData: EntityTrend) => {
  const totals = {};
  const apiData = entityTrendData['timeline'];
  // Loop through every date
  Object.values(apiData).forEach((entities) => {
    entities.forEach((e) => {
      if (!totals[e.name]) {
        totals[e.name] = {
          name: e.name,
          count: 0,
          sentimentSum: 0,
          occurrences: 0,
        };
      }
      totals[e.name].count += e.count;
      totals[e.name].sentimentSum += e.averageSentiment;
      totals[e.name].occurrences += 1;
    });
  });

  return Object.values(totals)
    .map((e) => ({
      name: e.name,
      size: e.count, // Total mentions over the interval
      sentiment: e.sentimentSum / e.occurrences, // Mean sentiment
    }))
    .sort((a, b) => b.size - a.size)
    .slice(0, 10);
};
