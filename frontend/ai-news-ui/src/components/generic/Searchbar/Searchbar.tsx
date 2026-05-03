import { Autocomplete, Group, NativeSelect } from '@mantine/core';
import { CaretDownIcon } from '@phosphor-icons/react';
import { ThemeColors } from '@/shared/constants/Colors';

const Searchbar = () => {
  return (
    <Group gap='sm' align='center' style={{ flex: 2 }}>
      <NativeSelect
        variant=''
        radius='sm'
        data={['News', 'Topic', 'Entity']}
        rightSection={<CaretDownIcon size={16} color={ThemeColors.primary} />}
        style={{
          minWidth: 120,
          background: ThemeColors.secondary,
          color: ThemeColors.primary,
          borderRadius: 'calc(0.5rem * 1)',
        }}
      />
      <Autocomplete placeholder='Search' radius='sm' style={{ flex: 1 }} />
    </Group>
  );
};

export default Searchbar;
