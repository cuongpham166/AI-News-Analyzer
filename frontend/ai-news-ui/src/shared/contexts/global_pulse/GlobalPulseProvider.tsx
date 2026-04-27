import { useState, type ReactNode } from 'react';
import { GlobalPulseContext } from './GlobalPulseContext';
import type { GlobalPulseInterval } from '../../interfaces/GlobalPulseInterval';

type Props = {
  children: ReactNode;
};

export const GlobalPulseProvider = ({ children }: Props) => {
  const initGlobalPulseInterval: GlobalPulseInterval = {
    intervalUnit: 'month',
    amount: 6,
  };
  const [globalPulseInterval, setGlobalPulseInterval] =
    useState<GlobalPulseInterval>(initGlobalPulseInterval);

  return (
    <GlobalPulseContext.Provider
      value={{ globalPulseInterval, setGlobalPulseInterval }}
    >
      {children}
    </GlobalPulseContext.Provider>
  );
};
