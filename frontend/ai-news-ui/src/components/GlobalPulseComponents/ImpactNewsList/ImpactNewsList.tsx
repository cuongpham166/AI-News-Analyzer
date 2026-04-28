import { useState, useEffect, useCallback } from 'react';
import {
  Card,
  Text,
  Badge,
  Stack,
  Paper,
  Button,
  Group,
  ActionIcon,
} from '@mantine/core';

import { PlusIcon } from '@phosphor-icons/react';
import type { ImpactNews } from '../../../shared/interfaces/ImpactNews';
import { fetchImpactNews } from '../../../services/analysisService';
import { SentimentColors, ThemeColors } from '../../../shared/contants/Colors';
import { getColorCode } from '../../../shared/utils/getColorCode';
import { useGlobalPulse } from '../../../shared/contexts/global_pulse/useGlobalPulse';
const options = {
  weekday: 'long',
  year: 'numeric',
  month: 'long',
  day: 'numeric',
};

interface ImpactNewsListProps {
  sentiment: string;
}

const ImpactNewsList = (props: ImpactNewsListProps) => {
  const { sentiment } = props;
  const [loading, setLoading] = useState<boolean>(true);
  const [news, setNews] = useState<ImpactNews[]>([]);
  const { globalPulseInterval } = useGlobalPulse();

  const fetchNews = useCallback(
    async (
      intervalUnit: string,
      amount: number,
      topN: number,
      isPositive: boolean,
    ) => {
      const result = await fetchImpactNews(
        intervalUnit,
        amount,
        topN,
        isPositive,
      );
      if (result) setNews(result);
    },
    [],
  );

  useEffect(() => {
    const loadNews = async () => {
      await fetchNews(
        globalPulseInterval.intervalUnit,
        globalPulseInterval.amount,
        3,
        sentiment === 'positive',
      );
    };
    loadNews();
  }, [sentiment, fetchNews, globalPulseInterval]);

  return (
    <Paper p='sm' style={{ flex: 1, background: ThemeColors.third }}>
      <Stack>
        <Group justify='space-between' align='center'>
          <Badge
            color={
              sentiment == 'positive'
                ? getColorCode(SentimentColors.postive)
                : getColorCode(SentimentColors.crisis)
            }
            size='lg'
            radius='sm'
          >
            {sentiment == 'positive' ? 'Positive Impact' : 'Critical News'}
          </Badge>
          <ActionIcon
            variant='filled'
            color={ThemeColors.primary}
            radius='sm'
            aria-label='Settings'
          >
            <PlusIcon
              style={{
                width: '70%',
                height: '70%',
                color: ThemeColors.third,
              }}
            />
          </ActionIcon>
        </Group>

        <Stack style={{ flex: 1 }} gap='xs'>
          {news.map((article) => {
            const publishDate = new Date(article.publish_date * 1000);

            return (
              <Card
                padding='sm'
                withBorder
                key={article.link}
                style={{
                  background: ThemeColors.third,
                  borderColor: ThemeColors.primary,
                }}
              >
                <Text size='sm' fw={600} lineClamp={2} c={ThemeColors.primary}>
                  {article.title}
                </Text>
                <Text size='xs'>
                  {publishDate.toLocaleDateString(undefined, options)}
                </Text>
              </Card>
            );
          })}
        </Stack>
      </Stack>
    </Paper>
  );
};

export default ImpactNewsList;
