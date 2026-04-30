import { createContext } from 'react';
import type { EntityRelationshipInterval } from '../../interfaces/intervals/EntityRelationshipInterval';

type EntityRelationshipContextType = {
  entityRelationshipInterval: EntityRelationshipInterval;
  setEntityRelationshipInterval: (
    updatedInterval: EntityRelationshipInterval,
  ) => void;
};

export const EntityRelationshipContext = createContext<
  EntityRelationshipContextType | undefined
>(undefined);
