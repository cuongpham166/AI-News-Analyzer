import { useState, useEffect, useCallback } from 'react';
import { Tooltip, ResponsiveContainer, Treemap } from 'recharts';
import {
  Divider,
  Paper,
  Group,
  Text as MantineText,
  Stack,
} from '@mantine/core';
import { getColorCode } from '../../../shared/utils/getColorCode';
import { SentimentColors } from '../../../shared/contants/Colors';
import type { EntityTrendsChartData } from '../../../shared/interfaces/EntityTrend';
import { fetchGlobalEntityTrends } from '../../../services/analysisService';
import { aggregateEntities } from '../../../shared/utils/aggregateData';

const getColor = (val) => {
  if (val <= -0.6) return getColorCode(SentimentColors.crisis);
  if (val < -0.1) return getColorCode(SentimentColors.negative);
  if (val > 0.1) return getColorCode(SentimentColors.postive);
  return getColorCode(SentimentColors.neutral);
};

const CustomizedContent = (props) => {
  const { x, y, width, height, name, sentiment } = props;

  return (
    <g>
      <rect
        x={x}
        y={y}
        width={width}
        height={height}
        style={{
          fill: getColor(sentiment),
          stroke: '#fff',
          strokeWidth: 1,
          strokeOpacity: 1,
        }}
      />
      {width > 50 && (
        <text
          x={x + width / 2}
          y={y + height / 2}
          textAnchor='middle'
          dominantBaseline='middle'
          fill='#fff'
          fontSize={12}
          style={{ pointerEvents: 'none' }}
        >
          {name}
        </text>
      )}
    </g>
  );
};

const EntityTrendsChart = () => {
  const [entityTrend, setEntityTrend] = useState<EntityTrendsChartData[]>([]);

  const fetchEntityTrends = useCallback(
    async (intervalUnit: string, amount: number) => {
      try {
        const result = await fetchGlobalEntityTrends(intervalUnit, amount);
        if (result) {
          const chartData = [
            { name: 'Top Entities', children: aggregateEntities(result) },
          ];
          setEntityTrend(chartData);
        }
      } catch (error) {
        console.error('Error fetching news:', error);
      }
    },
    [],
  );

  useEffect(() => {
    const loadEntityTrends = async () => {
      await fetchEntityTrends('day', 7);
    };
    loadEntityTrends();
  }, [fetchEntityTrends]);

  return (
    <ResponsiveContainer width='100%' height='95%' minHeight={300}>
      <Treemap
        data={entityTrend}
        dataKey='size'
        aspectRatio={4 / 3}
        stroke='#fff'
        content={<CustomizedContent />}
      >
        <Tooltip
          content={({ payload }) => {
            if (payload && payload[0]) {
              const { name, size, sentiment } = payload[0].payload;
              return (
                <Paper p='sm' withBorder shadow='md'>
                  <MantineText fw={700} size='sm' c='blue'>
                    {name}
                  </MantineText>
                  <Divider my={4} />
                  <Stack>
                    <Group gap='xs'>
                      <MantineText size='sm' fw={700}>
                        Total Mentions:
                      </MantineText>
                      <MantineText size='sm' fw={500}>
                        {size}
                      </MantineText>
                    </Group>
                    <Group gap='xs'>
                      <MantineText size='sm' fw={700}>
                        Avg. Sentiment:
                      </MantineText>
                      <MantineText size='sm' fw={500}>
                        {sentiment.toFixed(2)}
                      </MantineText>
                    </Group>
                  </Stack>
                </Paper>
              );
            }
            return null;
          }}
        />
      </Treemap>
    </ResponsiveContainer>
  );
};

export default EntityTrendsChart;
