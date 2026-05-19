import { useCallback, useEffect, useState } from 'react';
import { mapPowerCoupleData } from '@/shared/utils/mapData';
import type { PowerCoupleChartData } from '@/shared/interfaces/PowerCouples';
import { fetchPowerCouples } from '@/services/analysisService';
import { Sankey, Tooltip } from 'recharts';
import { Divider, Group, Paper, Stack, Text } from '@mantine/core';
import { ThemeColors } from '@/shared/constants/Colors';

const COLORS = {
  person: '#8884d8',
  org: '#82ca9d',
};

const CustomNode = ({ x, y, width, height, index, payload }) => {
  console.log('CustomNode', payload);
  return (
    <g>
      <rect
        x={x}
        y={y}
        width={width}
        height={height}
        fill={COLORS[payload.type]}
        opacity={0.9}
      />
      <text
        x={x + width + 6}
        y={y + height / 2}
        fontSize={15}
        verticalAnchor='middle'
      >
        {payload.name}
      </text>

      <text
        x={x + width + 6}
        y={y + height / 2 + 15}
        fontSize={14}
        fill='#666'
        alignmentBaseline='middle'
      >
        {payload.value}
      </text>
    </g>
  );
};

const PowerCoupleChart = () => {
  const [powerCoupleData, setPowerCoupleData] =
    useState<PowerCoupleChartData>();

  const fetchPowerCoupleGraph = useCallback(
    async (intervalUnit: string, amount: number) => {
      try {
        const result = await fetchPowerCouples(intervalUnit, amount);
        const mappedData = mapPowerCoupleData(result);
        setPowerCoupleData(mappedData);
      } catch (error) {
        console.error('Error fetching news:', error);
      }
    },
    [],
  );

  useEffect(() => {
    const loadRelationshipGraphData = async () => {
      await fetchPowerCoupleGraph('month', 7);
    };
    loadRelationshipGraphData();
  }, [fetchPowerCoupleGraph]);

  return (
    <Sankey
      width={'100%'}
      height={800}
      data={powerCoupleData}
      node={<CustomNode />}
      nodePadding={20}
      margin={{ top: 20, bottom: 20, left: 50, right: 150 }}
      link={{ stroke: '#aaa' }}
    >
      <Tooltip
        content={({ payload }) => {
          if (payload && payload[0]) {
            const { name, value } = payload[0].payload;
            return (
              <Paper
                p='sm'
                withBorder
                style={{
                  background: ThemeColors.third,
                  borderColor: ThemeColors.primary,
                }}
              >
                <Stack gap='xs'>
                  <Text fw={700} size='md' c={ThemeColors.primary}>
                    {name}
                  </Text>
                  <Divider my={1} />
                  <Group gap='xs'>
                    <Text size='sm' fw={500} c={ThemeColors.primary}>
                      Total Mentions:
                    </Text>
                    <Text size='sm' fw={500}>
                      {value}
                    </Text>
                  </Group>
                </Stack>
              </Paper>
            );
          }
          return null;
        }}
      />
    </Sankey>
  );
};

export default PowerCoupleChart;
