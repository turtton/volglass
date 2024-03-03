import { FC, memo } from "react";
import { Handle, NodeProps, Position } from "reactflow";
import { useRouter } from "next/router";

export interface LinkData {
	slug?: string;
	contentHtml: FC;
	positions: PositionData[];
	width: number;
	height: number;
	color?: string;
}

export interface PositionData {
	position: Position;
	type: "target" | "source";
}

function LinkNode({ data, isConnectable }: NodeProps<LinkData>) {
	const router = useRouter();
	const Content = data.contentHtml;
	const className = `block link-node rounded-md border-2 ${data.color} m-2 p-4 overflow-auto hover:overflow-scroll`;
	return (
		<>
			{/* TODO Implement onKeyDown event
									 rome-ignore lint: lint/a11y.useKeyWithClickEvents*/}
			<div
				className={className}
				style={{ width: data.width, height: data.height }}
				onClick={() => {
					if (data.slug !== undefined) {
						void router.push(data.slug?.replace(/ /g, '+'));
					}
				}}
			>
				<div className="w-fit h-fit">
					<h1 className="font-bold text-xl">{data.slug?.split("/")?.pop()}</h1>
					<div className="my-4">
						<Content />
					</div>
				</div>
				{data.positions.map((position) => (
					<Handle
						id={position.position}
						type={position.type}
						position={position.position}
						style={{ background: "#555" }}
						isConnectable={isConnectable}
					/>
				))}
			</div>
		</>
	);
}

export default memo(LinkNode);
