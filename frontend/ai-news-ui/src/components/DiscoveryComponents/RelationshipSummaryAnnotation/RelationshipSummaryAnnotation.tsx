import {
  Card,
  Text,
  Title,
  Stack,
  Group,
  Divider,
  ThemeIcon,
  ColorSwatch,
  Paper,
} from '@mantine/core';
import { SentimentColors, ThemeColors } from '../../../shared/contants/Colors';
import { getColorCode } from '../../../shared/utils/getColorCode';
import { GraphIcon, LineSegmentIcon, CircleIcon } from '@phosphor-icons/react';
const RelationshipSummaryAnnotation = () => {
  return (
    <Card withBorder padding='0' style={{ height: '100%', overflow: 'hidden' }}>
      <Paper
        p='md'
        style={{
          display: 'flex',
          flexDirection: 'column',
          background: ThemeColors.third,
        }}
      >
        <Title order={5} mb='xs' c={ThemeColors.primary}>
          Map Intelligence
        </Title>

        <Stack gap='sm'>
          {/* Sentiment Section */}
          <Card
            padding='xs'
            withBorder
            style={{
              background: ThemeColors.third,
              borderColor: ThemeColors.primary,
            }}
          >
            <Group gap='xs' mb='xs'>
              <GraphIcon size={20} color={ThemeColors.primary} />
              <Text fw={600} size='xs' c={ThemeColors.primary}>
                SENTIMENT (S)
              </Text>
            </Group>
            <Stack gap={16}>
              <Stack gap={8}>
                <Group gap='xs'>
                  <ColorSwatch
                    color={getColorCode(SentimentColors.postive)}
                    size={14}
                  />
                  <Text
                    size='xs'
                    c={getColorCode(SentimentColors.postive)}
                    fw={600}
                  >
                    Positive:
                  </Text>
                </Group>
                <Text size='xs'> Cooperative relations, trade, or peace.</Text>
              </Stack>
              <Stack gap={8}>
                <Group gap='xs'>
                  <ColorSwatch
                    color={getColorCode(SentimentColors.negative)}
                    size={14}
                  />
                  <Text
                    size='xs'
                    c={getColorCode(SentimentColors.negative)}
                    fw={600}
                  >
                    Negative:
                  </Text>
                </Group>
                <Text size='xs'> Conflict, sanctions, or tension.</Text>
              </Stack>
              <Stack gap={8}>
                <Group gap='xs'>
                  <ColorSwatch
                    color={getColorCode(SentimentColors.neutral)}
                    size={14}
                  />
                  <Text
                    size='xs'
                    c={getColorCode(SentimentColors.neutral)}
                    fw={600}
                  >
                    Neutral:
                  </Text>
                </Group>
                <Text size='xs'>Objective reporting.</Text>
              </Stack>
            </Stack>
          </Card>

          {/* Weight Section */}
          <Card
            padding='xs'
            withBorder
            style={{
              background: ThemeColors.third,
              borderColor: ThemeColors.primary,
            }}
          >
            <Group gap='xs' mb='xs'>
              <LineSegmentIcon size={20} color={ThemeColors.primary} />
              <Text fw={600} size='xs' c={ThemeColors.primary}>
                WEIGHT (W)
              </Text>
            </Group>
            <Text size='xs'>
              Represents the total number of news articles where these two
              entities were mentioned together.
            </Text>
            <Group mt='xs' align='flex-end' gap={4}>
              <div style={{ width: 16, height: 5, backgroundColor: '#ccc' }} />
              <div style={{ width: 16, height: 8, backgroundColor: '#999' }} />
              <Text size='xs' ml='xs'>
                Thicker links = Higher volume
              </Text>
            </Group>
          </Card>

          {/* Node Section */}
          <Card
            padding='xs'
            withBorder
            style={{
              background: ThemeColors.third,
              borderColor: ThemeColors.primary,
            }}
          >
            <Group gap='xs' mb='xs'>
              <CircleIcon size={20} color={ThemeColors.primary} />
              <Text fw={600} size='xs' c={ThemeColors.primary}>
                NODES (ENTITY TONE)
              </Text>
            </Group>
            <Text size='xs' mb='sm'>
              Node colors represent the **average sentiment** of the news
              coverage associated with that specific entity:
            </Text>
            <Stack gap={16}>
              <Stack gap={8}>
                <Group gap='xs'>
                  <ColorSwatch
                    color={getColorCode(SentimentColors.postive)}
                    size={14}
                  />
                  <Text
                    size='xs'
                    c={getColorCode(SentimentColors.postive)}
                    fw={600}
                  >
                    Positive Tone:
                  </Text>
                </Group>
                <Text size='xs'>Entity associated with constructive news.</Text>
              </Stack>

              <Stack gap={8}>
                <Group gap='xs'>
                  <ColorSwatch
                    color={getColorCode(SentimentColors.negative)}
                    size={14}
                  />
                  <Text
                    size='xs'
                    c={getColorCode(SentimentColors.negative)}
                    fw={600}
                  >
                    Negative Tone:
                  </Text>
                </Group>
                <Text size='xs'>
                  Entity associated with critical/conflict news.
                </Text>
              </Stack>
              <Stack gap={8}>
                <Group gap='xs'>
                  <ColorSwatch
                    color={getColorCode(SentimentColors.neutral)}
                    size={14}
                  />
                  <Text
                    size='xs'
                    c={getColorCode(SentimentColors.neutral)}
                    fw={600}
                  >
                    Neutral Tone:
                  </Text>
                </Group>
                <Text size='xs'>Objective or balanced mentions.</Text>
              </Stack>
            </Stack>
          </Card>

          <Text size='xs' fs='italic'>
            Tip: Use the scroll wheel to zoom into specific clusters. Labels for
            Weight and Sentiment will appear automatically as you zoom in.
          </Text>
        </Stack>
      </Paper>
    </Card>
  );
};

export default RelationshipSummaryAnnotation;
