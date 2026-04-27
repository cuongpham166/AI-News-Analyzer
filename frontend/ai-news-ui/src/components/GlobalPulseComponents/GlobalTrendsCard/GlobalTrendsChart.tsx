import { useState, useEffect, useCallback, useContext } from 'react';
import {
  Area,
  CartesianGrid,
  Tooltip,
  XAxis,
  YAxis,
  ResponsiveContainer,
  Line,
  Legend,
  ComposedChart,
} from 'recharts';
import { RechartsDevtools } from '@recharts/devtools';
import { Divider, Paper, Group, Text, Stack } from '@mantine/core';
import type { GlobalTrend } from '../../../shared/interfaces/GlobalTrend';
import { fetchGlobalTrends } from '../../../services/analysisService';
import { getColorCode } from '../../../shared/utils/getColorCode';
import { TrendColors } from '../../../shared/contants/Colors';
import { useGlobalPulse } from '../../../shared/contexts/global_pulse/useGlobalPulse';

const GlobalTrendsChart = () => {
  const [globalTrends, setGlobalTrends] = useState<GlobalTrend[]>([]);
  const [topics, setTopics] = useState<string[]>([]);
  const { globalPulseInterval } = useGlobalPulse();

  const fetchGlobalTrendsData = useCallback(
    async (intervalUnit: string, amount: number) => {
      try {
        const result = await fetchGlobalTrends(intervalUnit, amount);

        const allTopics: string[] = Array.from(
          new Set(
            result['timeline'].flatMap((item) =>
              Object.keys(item.topTopics || {}),
            ),
          ),
        );

        const chartData = result['timeline'].map((item: GlobalTrend) => ({
          date: item.date,
          total: item.articleCount,
          sentiment: item.averageSentiment,
          ...item.topTopics,
        }));

        setGlobalTrends(chartData);
        setTopics(allTopics);
      } catch (error) {
        console.error('Error fetching news:', error);
      }
    },
    [],
  );

  useEffect(() => {
    const loadGlobalTrends = async () => {
      await fetchGlobalTrendsData(
        globalPulseInterval.intervalUnit,
        globalPulseInterval.amount,
      );
    };
    loadGlobalTrends();
  }, [fetchGlobalTrendsData, globalPulseInterval]);

  return (
    <ResponsiveContainer width='100%' height='95%' minHeight={300}>
      <ComposedChart
        data={globalTrends}
        margin={{ top: 10, right: 10, left: 0, bottom: 0 }}
      >
        <CartesianGrid strokeDasharray='3 3' vertical={false} />
        <XAxis dataKey='date' />
        <YAxis fontSize={12} />

        <Tooltip
          content={({ payload }) => {
            if (payload && payload[0]) {
              const { date, total, sentiment } = payload[0].payload;
              return (
                <Paper p='sm' withBorder shadow='md'>
                  <Stack gap='xs'>
                    <Text fw={700} size='sm' c='blue'>
                      {date}
                    </Text>
                    <Divider my={4} />
                    <Group gap='xs'>
                      <Text size='sm' fw={700}>
                        Total Articles:
                      </Text>
                      <Text size='sm' fw={500}>
                        {total}
                      </Text>
                    </Group>
                    <Group gap='xs'>
                      <Text size='sm' fw={700}>
                        Average Sentiment:
                      </Text>
                      <Text size='sm' fw={500}>
                        {sentiment.toFixed(2)}
                      </Text>
                    </Group>
                    <Text size='sm' fw={700}>
                      Top Topics:
                    </Text>
                    {topics.map((topic, id) =>
                      payload[0].payload[topic] != undefined ? (
                        <Group gap='xs' key={id}>
                          <Text size='sm' fw={500} tt='capitalize'>
                            {topic}:
                          </Text>
                          <Text size='sm'>{payload[0].payload[topic]}</Text>
                        </Group>
                      ) : (
                        <></>
                      ),
                    )}
                  </Stack>
                </Paper>
              );
            }
            return null;
          }}
        />
        <Legend />

        {topics.map((topic, index) => (
          <Area
            key={topic}
            name={topic.charAt(0).toUpperCase() + topic.slice(1)}
            type='monotone'
            dataKey={topic}
            stackId='1' // Stacks them
            stroke={getColorCode(TrendColors[index % TrendColors.length])}
            fill={getColorCode(TrendColors[index % TrendColors.length])}
            fillOpacity={0.4}
            connectNulls // Very important: prevents gaps if a topic is missing on a date
          />
        ))}
        <YAxis yAxisId='right' orientation='right' domain={[-1, 1]} />
        <Line
          name='Overall Sentiment'
          yAxisId='right'
          type='step'
          dataKey='sentiment'
          stroke='#a61e4d'
          strokeWidth={3}
          legendType='plainline'
          dot={{ r: 4 }}
        />
        <RechartsDevtools />
      </ComposedChart>
    </ResponsiveContainer>
  );
};

export default GlobalTrendsChart;
