import * as React from 'react';
import { useState, useContext } from 'react';
import { GlobalPulseContext } from '../../../shared/contexts/global_pulse/GlobalPulseContext';
import {
  ActionIcon,
  Autocomplete,
  NumberInput,
  NativeSelect,
  Paper,
  Title,
  Button,
  Group,
} from '@mantine/core';
import { ArrowRightIcon, UserCircleIcon } from '@phosphor-icons/react';
import { ThemeColors } from '../../../shared/contants/Colors';
import { useGlobalPulse } from '../../../shared/contexts/global_pulse/useGlobalPulse';
type Props = {};

const GlobalPulseTaskbar: React.FC<Props> = (props) => {
  const [intervalUnit, setIntervalUnit] = useState<string>('month');
  const [intervalAmount, setIntervalAmount] = useState<number>(6);
  const { globalPulseInterval, setGlobalPulseInterval } = useGlobalPulse();

  const onChangeNumberInterval = (value) => {
    setIntervalAmount(parseInt(value));
  };

  const onChangeTypeInterval = (value) => {
    switch (value) {
      case 'Days ago':
        setIntervalUnit('day');
        break;
      case 'Weeks ago':
        setIntervalUnit('week');
        break;
      case 'Months ago':
        setIntervalUnit('month');
        break;
      case 'Years ago':
        setIntervalUnit('year');
        break;
      default:
        break;
    }
  };

  const onChangeGlobalPulseInterval = () => {
    setGlobalPulseInterval({
      ...globalPulseInterval,
      ...{ intervalUnit: intervalUnit, amount: intervalAmount },
    });
  };

  return (
    <Paper p='md' style={{ background: ThemeColors.third }}>
      <Group align='center' gap='lg' style={{ width: '100%' }}>
        <Title order={3} style={{ color: ThemeColors.primary }}>
          Global Pulse
        </Title>

        <Group
          gap='md'
          align='center'
          style={{ flex: 1, justifyContent: 'flex-end' }}
        >
          {/* Search section */}
          <Group gap='sm' align='center' style={{ flex: 2 }}>
            <NativeSelect
              variant='filled'
              radius='sm'
              data={['News', 'Topic', 'Entity']}
              style={{ minWidth: 120 }}
            />
            <Autocomplete
              placeholder='Search'
              radius='sm'
              style={{ flex: 1 }}
            />
          </Group>

          {/* Time section */}
          <Group gap='sm' align='center'>
            <NumberInput
              name='time_value'
              defaultValue={6}
              min={1}
              max={10}
              style={{ width: 80 }}
              onChange={(value) => onChangeNumberInterval(value)}
            />
            <NativeSelect
              variant='filled'
              radius='sm'
              data={['Days ago', 'Weeks ago', 'Months ago', 'Years ago']}
              defaultValue='Months ago'
              style={{ minWidth: 120 }}
              onChange={(event) =>
                onChangeTypeInterval(event.currentTarget.value)
              }
            />
            <Button
              variant='filled'
              rightSection={<ArrowRightIcon size={14} />}
              styles={{
                root: {
                  backgroundColor: ThemeColors.primary,
                  color: ThemeColors.secondary,
                  border: 'none',
                  '&:hover': {
                    backgroundColor: '#1864ab !important',
                  },
                },
              }}
              onClick={onChangeGlobalPulseInterval}
            >
              Update
            </Button>
          </Group>
        </Group>
      </Group>
    </Paper>
  );
};

export default GlobalPulseTaskbar;
