import React, { useState } from 'react';
import '@mantine/core/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import GlobalPulse from './pages/GlobalPulsePage.tsx';
import NewsPage from './pages/NewsPage.tsx';
import NotFoundPage from './pages/NotFoundPage.tsx';
import Layout from './components/LayoutComponents/Layout.tsx';

const App: React.FC = () => {
  return (
    <MantineProvider>
      <Router>
        <Layout>
          <Routes>
            <Route path='/' element={<GlobalPulse />} />
            <Route path='/news' element={<NewsPage />} />
            <Route path='*' element={<NotFoundPage />} />
          </Routes>
        </Layout>
      </Router>
    </MantineProvider>
  );
};

export default App;
