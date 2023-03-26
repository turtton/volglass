import { ElementDefinition } from "cytoscape";
import { getRawGraphData } from "volglass-backend";

export interface LocalGraphData {
	nodes: ElementDefinition[];
	edges: ElementDefinition[];
}

export function getLocalGraphData(
	slug: string,
	cacheData: string,
): LocalGraphData {
	const rawGraphData = getRawGraphData(slug, cacheData);
	return {
		nodes: rawGraphData.nodes.map((node) => {
			return { data: { id: node.id, label: node.label } };
		}),
		edges: rawGraphData.edges.map((edge) => {
			return { data: { source: edge.source, target: edge.target } };
		}),
	};
}
