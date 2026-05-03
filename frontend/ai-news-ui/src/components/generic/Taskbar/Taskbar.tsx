import React, { type ReactNode } from 'react';
import Searchbar from '../Searchbar';
import { Group, Paper, Title } from '@mantine/core';
import { ThemeColors } from '@/shared/constants/Colors';

type Props = {
  children: ReactNode;
  taskbarTitle: string;
};

const Taskbar: React.FC<Props> = ({ children, taskbarTitle }) => {
  return (
    <Paper p='md' style={{ background: ThemeColors.third }}>
      <Group align='center' gap='lg' style={{ width: '100%' }}>
        <Title order={3} style={{ color: ThemeColors.primary }}>
          {taskbarTitle}
        </Title>
        <Group
          gap='md'
          align='center'
          style={{ flex: 1, justifyContent: 'flex-end' }}
        >
          <Searchbar />
          {children}
        </Group>
      </Group>
    </Paper>
  );
};

export default Taskbar;
