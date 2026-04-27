import { useContext } from 'react';
import { GlobalPulseContext } from './GlobalPulseContext';

export const useGlobalPulse = () => {
  const context = useContext(GlobalPulseContext);
  if (!context) {
    throw new Error('useGlobalPulse must be used within a GlobalPulseProvider');
  }
  return context;
};
