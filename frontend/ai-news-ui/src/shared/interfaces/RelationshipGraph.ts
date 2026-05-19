interface Node {
  id: string;
  label: string;
  group: string;
  size: number;
  sentiment: number;
  color: string;
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
