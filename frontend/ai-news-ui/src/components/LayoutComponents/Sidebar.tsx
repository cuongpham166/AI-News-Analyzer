import * as React from 'react';
import { useState, useEffect } from 'react';

import { Stack, Text, Button, NavLink, Group } from '@mantine/core';
import {
  FingerprintIcon,
  GaugeIcon,
  HeartbeatIcon,
  MagnifyingGlassIcon,
  RssSimpleIcon,
  SignOutIcon,
  UserIcon,
} from '@phosphor-icons/react';
import { ThemeColors } from '../../shared/contants/Colors';
function Sidebar() {
  const [hoveredIndex, setHoveredIndex] = useState(null);
  const data = [
    { icon: GaugeIcon, label: 'Dashboard' },
    { icon: HeartbeatIcon, label: 'Activity' },
    { icon: MagnifyingGlassIcon, label: 'Explore' },
    { icon: UserIcon, label: 'Profile' },
  ];
  return (
    <Stack justify='space-between' style={{ height: '100%' }}>
      <Group gap='xs' justify='center' align='center' style={{ width: '100%' }}>
        <RssSimpleIcon size={40} color={ThemeColors.secondary} />
        <Text
          c={ThemeColors.secondary}
          size='xl'
          style={{ fontFamily: "'Roboto', sans-serif", fontWeight: 700 }}
        >
          AI Analyzer
        </Text>
      </Group>

      <Stack gap='lg'>
        {data.map((item, index) => {
          const IconComponent = item.icon;
          const isHovered = hoveredIndex === index;
          return (
            <NavLink
              key={item.label}
              label={item.label}
              component='a'
              leftSection={
                <item.icon
                  size={26}
                  color={
                    isHovered ? ThemeColors.primary : ThemeColors.secondary
                  }
                />
              }
              onMouseEnter={() => setHoveredIndex(index)}
              onMouseLeave={() => setHoveredIndex(null)}
              styles={(theme) => ({
                label: {
                  color: isHovered
                    ? ThemeColors.primary
                    : ThemeColors.secondary,
                  fontWeight: 500,
                  fontSize: '16px',
                },

                root: {
                  padding: '0.5rem 1rem',
                  borderRadius: theme.radius.sm,
                  backgroundColor: isHovered
                    ? ThemeColors.secondary
                    : 'transparent',
                  cursor: 'pointer',
                },
              })}
            />
          );
        })}
      </Stack>
      <Button
        leftSection={<SignOutIcon size={25} />}
        variant='default'
        fullWidth
      >
        Logout
      </Button>
    </Stack>
  );
}

export default Sidebar;
