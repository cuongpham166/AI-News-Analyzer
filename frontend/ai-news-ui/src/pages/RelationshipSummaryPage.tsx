import * as React from 'react';
import { useState, useEffect } from 'react';
import RelationshipSummaryTaskbar from '../components/RelationshipSummaryComponents/RelationshipSummaryTaskbar';
import RelationshipSummaryCard from '../components/RelationshipSummaryComponents/RelationshipSummaryCard';
import { Card, Grid, Stack, Text } from '@mantine/core';

function RelationshipSummaryPage() {
  return (
    <Stack>
      <RelationshipSummaryTaskbar />
      <Grid gap='md'>
        <Grid.Col span={12} style={{ height: '90%' }}>
          <Card
            withBorder
            padding='0'
            style={{ height: '100%', overflow: 'hidden' }}
          >
            <Stack style={{ flex: 1 }} gap='sm'>
              <RelationshipSummaryCard />
            </Stack>
          </Card>
        </Grid.Col>
      </Grid>
    </Stack>
  );
}

export default RelationshipSummaryPage;
