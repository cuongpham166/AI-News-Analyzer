import * as React from 'react';
import { useState, useEffect } from 'react';
import {
  Badge,
  Box,
  Grid,
  Paper,
  ScrollArea,
  Divider,
  Stack,
  Title,
  Autocomplete,
  Avatar,
  Group,
  Text,
  Button,
} from '@mantine/core';

import { ThemeColors } from '../shared/contants/Colors';

import GlobalTrendsCard from '../components/HomePageComponents/GlobalTrendsCard';
import EntityTrendsCard from '../components/HomePageComponents/EntityTrendsCard';
import ImpactNewsList from '../components/HomePageComponents/ImpactNewsList';
import HomeTaskbar from '../components/HomePageComponents/HomeTaskbar';
function HomePage() {
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
export default HomePage;
