import { Box, Card, Paper, Stack, Title } from '@mantine/core';
import { useState } from 'react';
import { ThemeColors } from '../../../../shared/contants/Colors';
import PowerCoupleChart from './PowerCoupleChart';
const PowerCoupleCard = () => {
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
            Power Couples Intelligence
          </Title>
          <Box style={{ flex: 1, minHeight: 0 }}>
            <PowerCoupleChart />
          </Box>
        </Paper>
      </Stack>
    </Card>
  );
};

export default PowerCoupleCard;
