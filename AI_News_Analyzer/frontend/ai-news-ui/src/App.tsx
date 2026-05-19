import '@mantine/core/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import Layout from '@/components/LayoutComponents/Layout.tsx';

import {
  DetailedNewsPage,
  GlobalPulsePage,
  DimensionsRisksPage,
  DiscoveryPage,
  NotFoundPage,
  RelationshipPage,
} from '@/pages';

import {
  EntityRelationshipProvider,
  GlobalPulseProvider,
} from '@/shared/providers';

const App = () => {
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
