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
import axios from 'axios';
import { getColorCode } from '../../shared/utils/getColorCode';
import { SentimentColors, ThemeColors } from '../../shared/contants/Colors';
import type { ImpactNews } from '../../shared/interfaces/ImpactNews';
import { PlusIcon } from '@phosphor-icons/react';

const API_URL = 'http://localhost:8081/api';
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
  const [loading, setLoading] = useState<boolean>(true);
  const { sentiment } = props;
  const [news, setNews] = useState<ImpactNews[]>([]);

  const fetchNews = useCallback(
    async (
      intervalUnit: string,
      amount: number,
      topN: number,
      isPositive: boolean,
    ) => {
      try {
        const response = await axios.get<ImpactNews[]>(
          `${API_URL}/news/impact_articles`,
          {
            params: {
              intervalUnit,
              amount,
              topN,
              isPositive,
            },
          },
        );

        setNews(response.data);
      } catch (error) {
        console.error('Error fetching news:', error);
      }
    },
    [],
  );

  useEffect(() => {
    const loadNews = async () => {
      await fetchNews('day', 7, 3, sentiment === 'positive');
    };
    loadNews();
  }, [sentiment, fetchNews]);

  return (
    <Paper p='sm' style={{ flex: 1 }}>
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
            <PlusIcon style={{ width: '70%', height: '70%' }} />
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
                style={{ background: ThemeColors.secondary }}
              >
                <Text size='sm' fw={500} lineClamp={2}>
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
