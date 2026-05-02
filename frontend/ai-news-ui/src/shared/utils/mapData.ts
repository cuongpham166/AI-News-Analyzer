import type {
  PowerCouple,
  PowerCoupleChartData,
} from '../interfaces/PowerCouples';

const orgAlias = {
  WHO: 'World Health Organization',
  UNIFIL: 'UNIFIL',
  UNICEF: 'UNICEF',
  EU: 'European Union',
};

const cleanPowerCoupleData = (
  rawData: Array<PowerCouple>,
): Array<PowerCouple> => {
  const normalizeOrg = (org) => orgAlias[org] || org;
  return rawData.map((d) => ({
    person: d.person.trim(),
    organization: normalizeOrg(d.organization.trim()),
    strength: d.strength,
  }));
};

export const mapPowerCoupleData = (
  data: Array<PowerCouple>,
): PowerCoupleChartData => {
  const MIN_STRENGTH = 2;
  const cleanedData = cleanPowerCoupleData(data);
  const filtereddData = cleanedData.filter((d) => d.strength >= MIN_STRENGTH);
  const nodeMap = new Map();

  filtereddData.forEach(({ person, organization }) => {
    if (!nodeMap.has(person)) {
      nodeMap.set(person, { name: person, type: 'person' });
    }
    if (!nodeMap.has(organization)) {
      nodeMap.set(organization, { name: organization, type: 'org' });
    }
  });

  const nodes = Array.from(nodeMap.values());
  console.log('mapPowerCoupleData_nodeMap', nodeMap);
  console.log('mapPowerCoupleData_nodes', nodes);

  const getIndex = (name: string) => nodes.findIndex((n) => n.name === name);

  // aggregate duplicate links
  const linkMap = new Map();

  filtereddData.forEach(({ person, organization, strength }) => {
    const key = `${person}->${organization}`;
    if (!linkMap.has(key)) {
      linkMap.set(key, { source: person, target: organization, value: 0 });
    }
    linkMap.get(key).value += strength;
  });

  const links = Array.from(linkMap.values()).map((link) => ({
    source: getIndex(link.source),
    target: getIndex(link.target),
    value: link.value,
  }));

  return { nodes, links };
};
