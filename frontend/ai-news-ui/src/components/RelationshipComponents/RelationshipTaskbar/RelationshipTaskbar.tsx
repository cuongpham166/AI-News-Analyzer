import * as React from 'react';
import { useState } from 'react';
import { NumberInput, NativeSelect, Button, Group } from '@mantine/core';
import { ArrowRightIcon, CaretDownIcon } from '@phosphor-icons/react';

import Taskbar from '../../generic/Taskbar';
import { ThemeColors } from '@/shared/constants/Colors';

const RelationshipTaskbar = () => {
  const [intervalUnit, setIntervalUnit] = useState<string>('month');
  const [intervalAmount, setIntervalAmount] = useState<number>(6);

  const onChangeNumberInterval = (value) => {
    setIntervalAmount(parseInt(value));
  };

  const onChangeEntityRelationshipInterval = () => {};

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

  return (
    <Taskbar taskbarTitle='Relationship Summary11'>
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
          onClick={onChangeEntityRelationshipInterval}
        >
          Update
        </Button>
      </Group>
    </Taskbar>
  );
};

export default RelationshipTaskbar;
