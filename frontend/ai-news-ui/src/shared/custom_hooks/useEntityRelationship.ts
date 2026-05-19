import { useContext } from 'react';
import { EntityRelationshipContext } from '../contexts/EntityRelationshipContext.ts';

export const useEntityRelationship = () => {
  const context = useContext(EntityRelationshipContext);
  if (!context) {
    throw new Error(
      'useEntityRelationship must be used within an EntityRelationshipProvider',
    );
  }
  return context;
};
