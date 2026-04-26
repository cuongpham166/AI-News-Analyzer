import { Grid, Stack } from '@mantine/core';
import HomeTaskbar from '../components/GlobalPulseComponents/HomeTaskbar';
import GlobalTrendsCard from '../components/GlobalPulseComponents/GlobalTrendsCard';
import EntityTrendsCard from '../components/GlobalPulseComponents/EntityTrendsCard';
import ImpactNewsList from '../components/GlobalPulseComponents/ImpactNewsList';

function GlobalPulsePage() {
  return (
    <Stack>
      <HomeTaskbar />
      <Grid gap='md'>
        <Grid.Col span={8}>
          <Stack gap='md'>
            <GlobalTrendsCard />
            <EntityTrendsCard />
          </Stack>
        </Grid.Col>

        <Grid.Col span={4}>
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
