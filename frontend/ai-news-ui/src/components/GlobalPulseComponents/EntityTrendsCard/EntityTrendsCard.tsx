import { SentimentColors, ThemeColors } from '../../../shared/contants/Colors';
import { getColorCode } from '../../../shared/utils/getColorCode';
import EntityTrendsChart from './EntityTrendsChart';
import {
  Box,
  Paper,
  Title,
  ColorSwatch,
  Group,
  Text,
  Stack,
} from '@mantine/core';

function EntityTrendsCard() {
  return (
    <Paper
      p='md'
      h='calc(50vh - 60px)'
      style={{
        display: 'flex',
        flexDirection: 'column',
        background: ThemeColors.third,
      }}
    >
      <Title order={5} mb='xs' c={ThemeColors.primary}>
        Entity Mentions & Impact
      </Title>

      <Stack gap='xs'>
        <Box style={{ flex: 1, minHeight: 0 }}>
          <EntityTrendsChart />
        </Box>
        <Group gap='sm'>
          <Group gap='xs'>
            <ColorSwatch
              size={20}
              color={getColorCode(SentimentColors.crisis)}
            />
            <Text size='sm'>Crisis</Text>
          </Group>
          <Group gap='xs'>
            <ColorSwatch
              size={20}
              color={getColorCode(SentimentColors.negative)}
            />
            <Text size='sm'>Negative</Text>
          </Group>

          <Group gap='xs'>
            <ColorSwatch
              size={20}
              color={getColorCode(SentimentColors.postive)}
            />
            <Text size='sm'>Positive</Text>
          </Group>

          <Group gap='xs'>
            <ColorSwatch
              size={20}
              color={getColorCode(SentimentColors.neutral)}
            />
            <Text size='sm'>Neutral</Text>
          </Group>
        </Group>
      </Stack>
    </Paper>
  );
}

export default EntityTrendsCard;
