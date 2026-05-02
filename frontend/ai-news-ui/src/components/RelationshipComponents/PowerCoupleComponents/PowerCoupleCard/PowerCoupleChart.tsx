import { useCallback, useEffect, useState } from 'react';
import { mapPowerCoupleData } from '../../../../shared/utils/mapData';
import type { PowerCoupleChartData } from '../../../../shared/interfaces/PowerCouples';
import { fetchPowerCouples } from '../../../../services/analysisService';
import { Sankey, Tooltip } from 'recharts';

const COLORS = {
  person: '#8884d8',
  org: '#82ca9d',
};

const CustomNode = ({ x, y, width, height, index, payload }) => {
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
        fontSize={12}
        verticalAnchor='middle'
      >
        {payload.name}
      </text>
      <text fontSize='12' stroke='#333' strokeOpacity='0.5'>
        {`${payload.value}`}
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
      await fetchPowerCoupleGraph('month', 6);
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
      <Tooltip />
    </Sankey>
  );
};

export default PowerCoupleChart;
