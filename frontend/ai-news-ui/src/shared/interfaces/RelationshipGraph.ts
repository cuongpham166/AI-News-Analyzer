interface Node {
  id: string;
  label: string;
  group: string;
  size: number;
  sentiment: number;
}

interface Link {
  source: string;
  target: string;
  value: number;
  sentiment: number;
}

export interface RelationshipGraph {
  nodes: Array<Node>;
  links: Array<Link>;
}
