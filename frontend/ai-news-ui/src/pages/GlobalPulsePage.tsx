import { useState } from 'react';
import { Grid, Stack } from '@mantine/core';
import GlobalPulseTaskbar from '../components/GlobalPulseComponents/GlobalPulseTaskbar';
import GlobalTrendsCard from '../components/GlobalPulseComponents/GlobalTrendsCard';
import EntityTrendsCard from '../components/GlobalPulseComponents/EntityTrendsCard';
import ImpactNewsList from '../components/GlobalPulseComponents/ImpactNewsList';

function GlobalPulsePage() {
  return (
    <Stack>
      <GlobalPulseTaskbar />
      <Grid gap='md'>
        <Grid.Col span={9}>
          <Stack gap='md'>
            <GlobalTrendsCard />
            <EntityTrendsCard />
          </Stack>
        </Grid.Col>

        <Grid.Col span={3}>
          <Stack style={{ flex: 1 }} gap='sm'>
            <ImpactNewsList sentiment='positive' />
            <ImpactNewsList sentiment='negative' />
          </Stack>
        </Grid.Col>
      </Grid>
    </Stack>
  );
}
export default GlobalPulsePage;
