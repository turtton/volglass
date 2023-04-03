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
				}))
				.with(P.instanceOf(NodeFile), (nodeFile: NodeFile) => ({
					slug: `/${nodeFile.file}`.replace(".md", ""),
					contentHtml: readContent(`/${nodeFile.file}`),
					positions: findTargetSide(node.id, canvas.edges),
					width: node.width,
					height: node.height,
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
