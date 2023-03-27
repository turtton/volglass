import { FC, memo } from "react";
import { Handle, NodeProps, Position } from "reactflow";
import { useRouter } from "next/router";

export interface LinkData {
	slug?: string;
	contentReader: (slug: string) => FC;
	positions: Position[];
}

function LinkNode({ data, isConnectable }: NodeProps<LinkData>) {
	const router = useRouter();
	const Content =
		data.slug !== undefined ? data.contentReader(data.slug) : () => <></>;

	return (
		<div
			className="link-node dark:bg-gray-500"
			onClick={() => {
				if (data.slug !== undefined) {
					void router.push(data.slug);
				}
			}}
		>
			<div className="flex">
				<Content />
			</div>
			{data.positions.map((position) => (
				<Handle
					id={position}
					type="target"
					position={position}
					style={{ background: "#555" }}
					isConnectable={isConnectable}
				/>
			))}
		</div>
	);
}

export default memo(LinkNode);
