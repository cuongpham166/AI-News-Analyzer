import GlobalTrendsChart from './GlobalTrendsChart';
import { Box, Paper, Title } from '@mantine/core';
import { ThemeColors } from '@/shared/constants/Colors';

function GlobalTrendsCard() {
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
        Topic Trends Over Time
      </Title>
      <Box style={{ flex: 1, minHeight: 0 }}>
        <GlobalTrendsChart />
      </Box>
    </Paper>
  );
}

export default GlobalTrendsCard;
