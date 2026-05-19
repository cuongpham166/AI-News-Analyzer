import RelationshipSummaryTaskbar from '@/components/DiscoveryComponents/RelationshipSummaryTaskbar';
import RelationshipSummaryCard from '@/components/DiscoveryComponents/RelationshipSummaryCard';
import RelationshipSummaryAnnotation from '@/components/DiscoveryComponents/RelationshipSummaryAnnotation';
import { Grid, Stack } from '@mantine/core';

function DiscoveryPage() {
  return (
    <Stack>
      <RelationshipSummaryTaskbar />
      <Grid gap='md'>
        <Grid.Col span={9} style={{ height: '90%' }}>
          <RelationshipSummaryCard />
        </Grid.Col>
        <Grid.Col span={3} style={{ height: '90%' }}>
          <RelationshipSummaryAnnotation />
        </Grid.Col>
      </Grid>
    </Stack>
  );
}

export default DiscoveryPage;
