import * as React from 'react';
import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { Stack, Text, Button, NavLink, Group } from '@mantine/core';
import {
  GaugeIcon,
  HeartbeatIcon,
  MagnifyingGlassIcon,
  RssSimpleIcon,
  SignOutIcon,
  UserIcon,
  TreeStructureIcon,
  NewspaperIcon,
  VectorThreeIcon,
} from '@phosphor-icons/react';
import { ThemeColors } from '../../shared/contants/Colors';
function Sidebar() {
  const [hoveredIndex, setHoveredIndex] = useState(null);
  const navigate = useNavigate();
  const menuData = [
    { icon: GaugeIcon, label: 'Global Pulse', url: '/' },
    { icon: VectorThreeIcon, label: 'Dimensions & Risks', url: '/dimension' },
    { icon: TreeStructureIcon, label: 'Relationship', url: '/relationship' },
    { icon: MagnifyingGlassIcon, label: 'Discovery' },
    { icon: NewspaperIcon, label: 'News' },
    { icon: HeartbeatIcon, label: 'Activity' },
    { icon: UserIcon, label: 'Profile' },
  ];

  const onMenuClick = (event) => {
    const menuTitle = event.target.innerText;
    const foundItem = menuData.filter((menu) => menu.label == menuTitle);
    const url = foundItem[0]['url'];
    navigate(url);
  };

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
        {menuData.map((item, index) => {
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
              onClick={(event) => onMenuClick(event)}
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
        style={{
          background: ThemeColors.secondary,
          color: ThemeColors.primary,
          border: 'none',
        }}
      >
        Logout
      </Button>
    </Stack>
  );
}

export default Sidebar;
