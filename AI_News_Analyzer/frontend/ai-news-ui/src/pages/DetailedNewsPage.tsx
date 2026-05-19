import { Grid, Stack } from '@mantine/core';

import Taskbar from '@/components/generic/Taskbar';
import DetailedNewsCard from '@/components/DetailedNewsPageComponents/DetailedNewsCard';
import DetailedNewsMetaCard from '@/components/DetailedNewsPageComponents/DetailedNewsMetaCard';

function DetailedNewsPage() {
  return (
    <Stack>
      <Taskbar taskbarTitle='Detailed News'>
        <></>
      </Taskbar>
      <Grid gap='md'>
        <Grid.Col span={9}>
          <Stack>
            <DetailedNewsCard />
          </Stack>
        </Grid.Col>
        <Grid.Col span={3}>
          <DetailedNewsMetaCard />
        </Grid.Col>
      </Grid>
    </Stack>
  );
}

export default DetailedNewsPage;
