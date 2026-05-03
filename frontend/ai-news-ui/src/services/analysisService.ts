import axios from 'axios';
import type { ImpactNews } from '../shared/interfaces/ImpactNews';
import type { EntityTrend } from '../shared/interfaces/EntityTrend';
import type { GlobalTrend } from '../shared/interfaces/GlobalTrend';
import type { RelationshipGraph } from '../shared/interfaces/RelationshipGraph';
import type { PowerCouple } from '../shared/interfaces/PowerCouples';
//const API_URL = import.meta.env.VITE_API_ENDPOINT;
const API_URL = 'http://localhost:8081/api';
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
    return response.data;
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

export const fetchRelationshipGraphData = async (
  intervalUnit: string,
  amount: number,
): Promise<RelationshipGraph> => {
  try {
    const response = await axios.get<RelationshipGraph>(
      `${API_URL}/analysis/discovery`,
      {
        params: {
          intervalUnit,
          amount,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error('Error relationship graph data:', error);
    return undefined;
  }
};

export const fetchPowerCouples = async (
  intervalUnit: string,
  amount: number,
): Promise<Array<PowerCouple>> => {
  try {
    const response = await axios.get<Array<PowerCouple>>(
      `${API_URL}/analysis/power_couple`,
      {
        params: {
          intervalUnit,
          amount,
        },
      },
    );
    return response.data;
  } catch (error) {
    console.error('Error power couple data:', error);
    return undefined;
  }
};
