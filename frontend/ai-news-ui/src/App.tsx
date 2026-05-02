import React, { useState } from 'react';
import '@mantine/core/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Layout from './components/LayoutComponents/Layout.tsx';

import GlobalPulsePage from './pages/GlobalPulsePage.tsx';
import DimensionsRisksPage from './pages/DimensionsRisksPage.tsx';
import DiscoveryPage from './pages/DiscoveryPage.tsx';
import NewsPage from './pages/NewsPage.tsx';
import NotFoundPage from './pages/NotFoundPage.tsx';
import RelationshipPage from './pages/RelationshipPage.tsx';
import { GlobalPulseProvider } from './shared/contexts/global_pulse/GlobalPulseProvider.tsx';
import { EntityRelationshipProvider } from './shared/contexts/entity_relationship/EntityRelationshipProvider.tsx';
import DetailedNewsPage from './pages/DetailedNewsPage.tsx';
import powerCouplesData from '../test/PowerCouples.ts';
import { mapPowerCoupleData } from './shared/utils/mapData.ts';
const App: React.FC = () => {
  mapPowerCoupleData(powerCouplesData);
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
            <Route path='/relationship' element={<RelationshipPage />} />
            <Route
              path='/discovery'
              element={
                <EntityRelationshipProvider>
                  <DiscoveryPage />
                </EntityRelationshipProvider>
              }
            />
            <Route path='/news' element={<DetailedNewsPage />} />
            <Route path='/news/:link' element={<DetailedNewsPage />} />
            <Route path='*' element={<NotFoundPage />} />
          </Routes>
        </Layout>
      </Router>
    </MantineProvider>
  );
};

export default App;
