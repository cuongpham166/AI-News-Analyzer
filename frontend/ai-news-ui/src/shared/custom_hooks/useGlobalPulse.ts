import { useContext } from 'react';
import { GlobalPulseContext } from '../contexts/GlobalPulseContext.ts';

export const useGlobalPulse = () => {
  const context = useContext(GlobalPulseContext);
  if (!context) {
    throw new Error('useGlobalPulse must be used within a GlobalPulseProvider');
  }
  return context;
};
