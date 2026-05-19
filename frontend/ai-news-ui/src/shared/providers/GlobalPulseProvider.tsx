import { useState, type ReactNode } from 'react';
import { GlobalPulseContext } from '../contexts/GlobalPulseContext.ts';
import type { GlobalPulseInterval } from '../interfaces/intervals/GlobalPulseInterval.ts';

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
