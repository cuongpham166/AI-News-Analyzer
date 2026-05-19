import React, { type ReactNode } from 'react';
import { Box, Card, Paper, Stack, Title } from '@mantine/core';
import { ThemeColors } from '@/shared/constants/Colors';

type Props = {
  children: ReactNode;
};

const RelationshipMainCard: React.FC<Props> = ({ children }) => {
  return (
    <Card withBorder padding='0' style={{ height: '100%', overflow: 'hidden' }}>
      <Stack style={{ flex: 1 }} gap='sm'>
        <Paper
          p='md'
          style={{
            display: 'flex',
            flexDirection: 'column',
            background: ThemeColors.third,
          }}
        >
          <Title order={5} mb='xs' c={ThemeColors.primary}>
            Power Couples Intelligence
          </Title>
          <Box style={{ flex: 1, minHeight: 0 }}>{children}</Box>
        </Paper>
      </Stack>
    </Card>
  );
};

export default RelationshipMainCard;
