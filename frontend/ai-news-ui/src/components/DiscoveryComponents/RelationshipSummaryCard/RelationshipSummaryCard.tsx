import { Box, Paper, Title, Text, Card, Stack } from '@mantine/core';
import RelationshipSummaryChart from './RelationshipSummaryChart';
import { ThemeColors } from '@/shared/constants/Colors';

const RelationshipSummaryCard = () => {
  return (
    <Card withBorder padding='0' style={{ height: '100%', overflow: 'hidden' }}>
      <Stack style={{ flex: 1 }} gap='sm'>
        <Paper
          p='md'
          style={{
            display: 'flex',
            flexDirection: 'column',
            background: ThemeColors.third,
          }}
        >
          <Title order={5} mb='xs' c={ThemeColors.primary}>
            Top Entity Relationship Intelligence
          </Title>
          <Box style={{ flex: 1, minHeight: 0 }}>
            <RelationshipSummaryChart />
          </Box>
          <Text size='sm' fw={700} c={ThemeColors.primary}>
            * Visualizing the top 300 strongest entity co-occurrences for the
            selected period.
          </Text>
        </Paper>
      </Stack>
    </Card>
  );
};

export default RelationshipSummaryCard;
