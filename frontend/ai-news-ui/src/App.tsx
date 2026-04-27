import React, { useState } from 'react';
import '@mantine/core/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Layout from './components/LayoutComponents/Layout.tsx';

import GlobalPulsePage from './pages/GlobalPulsePage.tsx';
import DimensionsRisksPage from './pages/DimensionsRisksPage.tsx';
import RelationshipSummaryPage from './pages/RelationshipSummaryPage.tsx';
import NewsPage from './pages/NewsPage.tsx';
import NotFoundPage from './pages/NotFoundPage.tsx';

import { GlobalPulseProvider } from './shared/contexts/global_pulse/GlobalPulseProvider.tsx';
const App: React.FC = () => {
  return (
    <MantineProvider>
      <Router>
        <Layout>
          <Routes>
            <Route
              path='/'
              element={
                <GlobalPulseProvider>
                  <GlobalPulsePage />
                </GlobalPulseProvider>
              }
            />
            <Route path='/dimension' element={<DimensionsRisksPage />} />
            <Route path='/relationship' element={<RelationshipSummaryPage />} />
            <Route path='/news' element={<NewsPage />} />
            <Route path='*' element={<NotFoundPage />} />
          </Routes>
        </Layout>
      </Router>
    </MantineProvider>
  );
};

export default App;
