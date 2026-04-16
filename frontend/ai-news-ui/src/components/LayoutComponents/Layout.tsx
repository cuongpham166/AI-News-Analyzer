import React, { type ReactNode } from 'react';
import {
  AppShell,
  AppShellFooter,
  Burger,
  Container,
  Stack,
  Title,
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { ThemeColors } from '../../shared/contants/Colors';
import Sidebar from './Sidebar';
interface LayoutProps {
  children: ReactNode;
}

const Layout: React.FC<LayoutProps> = ({ children }) => {
  return (
    <AppShell
      navbar={{ width: 250, breakpoint: 'sm' }}
      padding='md'
      // Force the Shell to be exactly the height of the window
      styles={{
        root: { height: '100vh', display: 'flex', flexDirection: 'column' },
        main: {
          height: '100vh',
          display: 'flex',
          overflow: 'hidden',
          backgroundColor: ThemeColors.secondary,
        },
      }}
    >
      <AppShell.Navbar
        p='lg'
        style={{ backgroundColor: ThemeColors.primary, border: 'none' }}
      >
        <Sidebar />
      </AppShell.Navbar>

      <AppShell.Main>
        {/* We use a div here to ensure children take 100% height/width of Main */}
        <div
          style={{
            flex: 1,
            display: 'flex',
            flexDirection: 'column',
            width: '100%',
          }}
        >
          {children}
        </div>
      </AppShell.Main>
    </AppShell>
  );
};

export default Layout;
