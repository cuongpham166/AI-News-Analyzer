import { Paper, Stack, Title } from '@mantine/core';
import { ThemeColors } from '@/shared/constants/Colors';

function DetailedNewsMetaCard() {
  return (
    <Paper
      p='lg'
      style={{
        display: 'flex',
        flexDirection: 'column',
        background: ThemeColors.third,
      }}
    >
      <Stack>
        <Title order={5} style={{ color: ThemeColors.primary }}>
          Meta Data
        </Title>
      </Stack>
    </Paper>
  );
}

export default DetailedNewsMetaCard;
