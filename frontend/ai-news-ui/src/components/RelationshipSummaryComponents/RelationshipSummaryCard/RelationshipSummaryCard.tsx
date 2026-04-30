import * as React from 'react';
import { useState, useContext } from 'react';
import { SentimentColors, ThemeColors } from '../../../shared/contants/Colors';
import { Box, Paper, Title, Text, Group, ColorSwatch } from '@mantine/core';
import RelationshipSummaryChart from './RelationshipSummaryChart';
import { getColorCode } from '../../../shared/utils/getColorCode';
type Props = {};

const relationshipLabels = [
  { title: 'Negative Relationship', color: SentimentColors.crisis },
  { title: 'Positive Relationship', color: SentimentColors.postive },
];

const RelationshipSummaryCard: React.FC<Props> = (props) => {
  return (
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
      <Group justify='space-between'>
        <Group gap='xl'>
          {relationshipLabels.map((label, index) => (
            <Group gap='5' key={index}>
              <ColorSwatch size={20} color={getColorCode(label.color)} />
              <Text size='sm' c={ThemeColors.primary} fw={600}>
                {label.title}
              </Text>
            </Group>
          ))}
        </Group>
        <Text size='sm' fw={700} c={ThemeColors.primary}>
          * Visualizing the top 300 strongest entity co-occurrences for the
          selected period.
        </Text>
      </Group>
    </Paper>
  );
};

export default RelationshipSummaryCard;
