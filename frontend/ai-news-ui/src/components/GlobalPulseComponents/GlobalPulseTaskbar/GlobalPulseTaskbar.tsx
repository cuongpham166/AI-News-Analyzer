import { useState } from 'react';
import { NumberInput, NativeSelect, Button, Group } from '@mantine/core';
import { ArrowRightIcon, CaretDownIcon } from '@phosphor-icons/react';

import { useGlobalPulse } from '@/shared/custom_hooks';
import Taskbar from '../../generic/Taskbar';
import { ThemeColors } from '@/shared/constants/Colors';

const GlobalPulseTaskbar = () => {
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
    <Taskbar taskbarTitle='Global Pulse'>
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
          variant=''
          data={['Days ago', 'Weeks ago', 'Months ago', 'Years ago']}
          defaultValue='Months ago'
          rightSection={<CaretDownIcon size={16} color={ThemeColors.primary} />}
          style={{
            minWidth: 120,
            background: ThemeColors.secondary,
            color: ThemeColors.primary,
            borderRadius: 'calc(0.5rem * 1)',
          }}
          onChange={(event) => onChangeTypeInterval(event.currentTarget.value)}
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
    </Taskbar>
  );
};

export default GlobalPulseTaskbar;
