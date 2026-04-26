import * as React from 'react';
import { useState, useEffect } from 'react';

import {
  ActionIcon,
  Autocomplete,
  NumberInput,
  Grid,
  NativeSelect,
  Paper,
  Title,
  Button,
  Group,
} from '@mantine/core';
import {
  ArrowRightIcon,
  SlidersHorizontalIcon,
  UserCircleIcon,
} from '@phosphor-icons/react';
import { ThemeColors } from '../../../shared/contants/Colors';
function HomeTaskbar() {
  return (
    <Paper p='md' style={{ background: ThemeColors.third }}>
      <Group align='center' gap='lg' style={{ width: '100%' }}>
        <Title order={3}>Global Pulse</Title>

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
              defaultValue={1}
              min={1}
              max={10}
              style={{ width: 80 }}
            />
            <NativeSelect
              variant='filled'
              radius='sm'
              data={['Days ago', 'Weeks ago', 'Months ago', 'Years ago']}
              style={{ minWidth: 120 }}
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
            >
              Update
            </Button>
          </Group>

          <ActionIcon
            variant='filled'
            aria-label='User'
            size={38}
            radius='xl'
            color={ThemeColors.primary}
          >
            <UserCircleIcon size={26} color={ThemeColors.third} />
          </ActionIcon>
        </Group>
      </Group>
    </Paper>
  );
}

export default HomeTaskbar;
