import { Blockquote, Card, Paper, Stack, Text, Title } from '@mantine/core';
import { ThemeColors } from '../../../shared/contants/Colors';
import detailedNewsData from '../../../../test/DetailedNews';
function DetailedNewsCard() {
  const isoString = detailedNewsData.publishDate;
  const publishedDate = new Date(isoString);
  return (
    <Paper
      p='lg'
      h='calc(100vh - 120px)'
      style={{
        display: 'flex',
        flexDirection: 'column',
        background: ThemeColors.third,
      }}
    >
      <Stack>
        {/*Headline Seaction */}
        <Stack>
          <Title order={3} style={{ color: ThemeColors.primary }}>
            {detailedNewsData.title}
          </Title>
          <Text size='md'>{publishedDate.toLocaleString('en-GB')}</Text>
        </Stack>

        <Blockquote
          color={ThemeColors.primary}
          radius='md'
          cite='– Summarized by distilbart-cnn-12-6'
          mt='xs'
        >
          {detailedNewsData.summary}
        </Blockquote>
        <Text size='md'>{detailedNewsData.fullText}</Text>
      </Stack>
    </Paper>
  );
}
export default DetailedNewsCard;
