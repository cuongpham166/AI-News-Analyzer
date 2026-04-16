import React, { useState } from 'react';
import '@mantine/core/styles.css';
import { MantineProvider } from '@mantine/core';
import { BrowserRouter as Router, Routes, Route } from 'react-router-dom';

import HomePage from './pages/HomePage.tsx';
import NewsPage from './pages/NewsPage.tsx';
import NotFoundPage from './pages/NotFoundPage.tsx';
import Layout from './components/LayoutComponents/Layout.tsx';

const App: React.FC = () => {
  const [count, setCount] = useState(0);
  const gekki = 'hi';
  return (
    <MantineProvider>
      <Router>
        <Layout>
          <Routes>
            <Route path='/' element={<HomePage />} />
            <Route path='/news' element={<NewsPage />} />
            <Route path='*' element={<NotFoundPage />} />
          </Routes>
        </Layout>
      </Router>
    </MantineProvider>
  );
};

export default App;
