import { FC, useMemo } from "react";
import ReactFlow, {
	Background,
	ConnectionLineType,
	Controls,
	Edge,
	Node,
	Position,
} from "reactflow";
import LinkNode, { LinkData, PositionData } from "./LinkNode";
import {
	CanvasData,
	CanvasEdge,
	NodeFile,
	NodeSide,
	NodeText,
} from "volglass-backend";
import { match, P } from "ts-pattern";

// The class name has to be defined without interpolation at compile time
// so tailwind will be able to see and not strip it from production css
// TODO: support edge colors
const NODE_STYLES = {
	"0": "bg-gray-500/50 border-gray-500",
	"1": "bg-red-600/50 border-red-600",
	"2": "bg-orange-500/50 border-orange-500",
	"3": "bg-yellow-300/50 border-yellow-300",
	"4": "bg-green-400/50 border-green-400",
	"5": "bg-cyan-400/50 border-cyan-400",
	"6": "bg-violet-500/50 border-violet-500",
}

const Canvas = (canvas: CanvasData, readContent: (id: string) => FC) => () => {
	const nodes = canvas.nodes.map(
		(node): Node<LinkData> => ({
			id: node.id,
			position: { x: node.x, y: node.y },
			// FIXME: not work
			width: node.width,
			height: node.height,
			type: "link",
			data: match(node.nodeData)
				.with(P.instanceOf(NodeText), (nodeText) => ({
					contentHtml: () => <p>{nodeText.text}</p>,
					positions: findTargetSide(node.id, canvas.edges),
					width: node.width,
					height: node.height,
					color: NODE_STYLES[node.color || "0"],
				}))
				.with(P.instanceOf(NodeFile), (nodeFile: NodeFile) => ({
					slug: `/${nodeFile.file}`.replace(".md", ""),
					contentHtml: readContent(`/${nodeFile.file}`),
					positions: findTargetSide(node.id, canvas.edges),
					width: node.width,
					height: node.height,
					color: NODE_STYLES[node.color || "0"],
				}))
				.run(),
		}),
	);
	const edges = canvas.edges.map(
		(edge): Edge => ({
			id: edge.id,
			source: edge.fromNode,
			sourceHandle: convertSideData(edge.fromSide),
			target: edge.toNode,
			targetHandle: convertSideData(edge.toSide),
			labelBgStyle: {opacity: 0.5},
			label: edge.label,
		}),
	);
	const nodeTypes = useMemo(
		() => ({
			link: LinkNode,
		}),
		[],
	);
	return (
		<div className="canvas">
			{nodes && (
				<ReactFlow
					connectionLineType={ConnectionLineType.Bezier}
					fitView
					nodeTypes={nodeTypes}
					nodes={nodes}
					edges={edges}
					minZoom={0.03}
				>
					<Background />
					<Controls className="rounded-md dark:bg-dark-background-primary dark:border-gray-600 dark:fill-gray-200" />
				</ReactFlow>
			)}
		</div>
	);
};

const convertSideData = (side: NodeSide) =>
	match(side.name)
		.with("TOP", () => Position.Top)
		.with("BOTTOM", () => Position.Bottom)
		.with("RIGHT", () => Position.Right)
		.with("LEFT", () => Position.Left)
		.run();

const findTargetSide = (targetNode: string, edges: CanvasEdge[]) =>
	edges
		.map((edge) =>
			edge.toNode === targetNode
				? { position: convertSideData(edge.toSide), type: "target" }
				: edge.fromNode === targetNode
				? { position: convertSideData(edge.fromSide), type: "source" }
				: null,
		)
		.filter((side) => side !== null)
		.filter((side, i, array) => array.indexOf(side) === i)
		.map((side) => side as PositionData);

export default Canvas;
