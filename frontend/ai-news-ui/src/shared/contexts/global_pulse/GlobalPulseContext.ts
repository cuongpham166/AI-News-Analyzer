import { createContext } from 'react';
import type { GlobalPulseInterval } from '../../interfaces/GlobalPulseInterval';

type GlobalPulseContextType = {
  globalPulseInterval: GlobalPulseInterval;
  setGlobalPulseInterval: (updatedInterval: GlobalPulseInterval) => void;
};

export const GlobalPulseContext = createContext<
  GlobalPulseContextType | undefined
>(undefined);
