export interface PowerCouple {
  person: string;
  organization: string;
  strength: number;
}

interface PowerCoupleLink {
  source: number;
  target: number;
  value: number;
}

interface PowerCoupleNode {
  name: string;
  type: string;
}

export interface PowerCoupleChartData {
  links: Array<PowerCoupleLink>;
  nodes: Array<PowerCoupleNode>;
}
