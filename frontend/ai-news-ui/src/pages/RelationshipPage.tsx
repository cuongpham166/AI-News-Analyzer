import { Grid, Stack } from '@mantine/core';
import * as React from 'react';
import { useState, useEffect } from 'react';
import Taskbar from '../components/generic/Taskbar';
import PowerCoupleTaskbar from '../components/RelationshipComponents/PowerCoupleComponents/PowerCoupleTaskbar';
import PowerCoupleCard from '../components/RelationshipComponents/PowerCoupleComponents/PowerCoupleCard';
function RelationshipPage() {
  return (
    <Stack>
      <PowerCoupleTaskbar />
      <Grid gap='md'>
        <Grid.Col span={9} style={{ height: '90%' }}>
          <PowerCoupleCard />
        </Grid.Col>
        <Grid.Col span={3} style={{ height: '90%' }}></Grid.Col>
      </Grid>
    </Stack>
  );
}

export default RelationshipPage;
