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

const entityTrendsLabels = [
  { title: 'Crisis', color: SentimentColors.crisis },
  { title: 'Negative', color: SentimentColors.negative },
  { title: 'Positive', color: SentimentColors.postive },
  { title: 'Neutral', color: SentimentColors.neutral },
];
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
        <Group gap='md'>
          {entityTrendsLabels.map((label, index) => (
            <Group gap='5' key={index}>
              <ColorSwatch size={20} color={getColorCode(label.color)} />
              <Text size='sm' c={ThemeColors.primary} fw={600}>
                {label.title}
              </Text>
            </Group>
          ))}
        </Group>
      </Stack>
    </Paper>
  );
}

export default EntityTrendsCard;
