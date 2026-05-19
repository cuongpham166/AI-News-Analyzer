import { Grid, Stack } from '@mantine/core';
import RelationshipTaskbar from '@/components/RelationshipComponents/RelationshipTaskbar';
import RelationshipMainCard from '@/components/RelationshipComponents/RelationshipMainCard';
import PowerCoupleChart from '@/components/RelationshipComponents/PowerCoupleComponents/PowerCoupleChart';

function RelationshipPage() {
  return (
    <Stack>
      <RelationshipTaskbar />
      <Grid gap='md'>
        <Grid.Col span={9} style={{ height: '90%' }}>
          <RelationshipMainCard>
            <PowerCoupleChart />
          </RelationshipMainCard>
        </Grid.Col>
        <Grid.Col span={3} style={{ height: '90%' }}></Grid.Col>
      </Grid>
    </Stack>
  );
}

export default RelationshipPage;
