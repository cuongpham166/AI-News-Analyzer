import axios from 'axios';
import type { ImpactNews } from '../shared/interfaces/ImpactNews';
import type { EntityTrend } from '../shared/interfaces/EntityTrend';
import type { GlobalTrend } from '../shared/interfaces/GlobalTrend';

const API_URL = import.meta.env.VITE_API_ENDPOINT;

export const fetchImpactNews = async (
  intervalUnit: string,
  amount: number,
  topN: number,
  isPositive: boolean,
): Promise<ImpactNews[]> => {
  try {
    const response = await axios.get<ImpactNews[]>(
      `${API_URL}/analysis/impact_articles`,
      {
        params: {
          intervalUnit,
          amount,
          topN,
          isPositive,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error('Error fetching news:', error);
    return undefined;
  }
};

export const fetchGlobalEntityTrends = async (
  intervalUnit: string,
  amount: number,
): Promise<EntityTrend> => {
  try {
    const response = await axios.get<EntityTrend>(
      `${API_URL}/analysis/global_entity_trends`,
      {
        params: {
          intervalUnit,
          amount,
        },
      },
    );
    const result: EntityTrend = response.data;
    return result;
  } catch (error) {
    console.error('Error fetching global entity trends:', error);
    return undefined;
  }
};

export const fetchGlobalTrends = async (
  intervalUnit: string,
  amount: number,
): Promise<GlobalTrend[]> => {
  try {
    const response = await axios.get<GlobalTrend[]>(
      `${API_URL}/analysis/global_trends`,
      {
        params: {
          intervalUnit,
          amount,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error('Error fetching global trends:', error);
    return undefined;
  }
};
