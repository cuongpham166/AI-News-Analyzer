import { useState, type ReactNode } from 'react';
import { EntityRelationshipContext } from '../contexts/EntityRelationshipContext.ts';
import type { EntityRelationshipInterval } from '../interfaces/intervals/EntityRelationshipInterval.ts';

type Props = {
  children: ReactNode;
};

export const EntityRelationshipProvider = ({ children }: Props) => {
  const [entityRelationshipInterval, setEntityRelationshipInterval] =
    useState<EntityRelationshipInterval>({
      intervalUnit: 'month',
      amount: 6,
    });

  return (
    <EntityRelationshipContext.Provider
      value={{
        entityRelationshipInterval,
        setEntityRelationshipInterval,
      }}
    >
      {children}
    </EntityRelationshipContext.Provider>
  );
};
