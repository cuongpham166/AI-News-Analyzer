import { Stack } from '@mantine/core';
import * as React from 'react';
import { useState, useEffect } from 'react';
import Taskbar from '../components/generic/Taskbar';
function NewsPage() {
  return (
    <Stack>
      <Taskbar taskbarTitle='Latest News'>
        <></>
      </Taskbar>
    </Stack>
  );
}

export default NewsPage;
