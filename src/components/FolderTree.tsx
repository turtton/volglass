import * as React from "react";
import { TreeView } from "@mui/x-tree-view/TreeView";
import ExpandMoreIcon from "@mui/icons-material/ExpandMore";
import ChevronRightIcon from "@mui/icons-material/ChevronRight";
import { TreeItem } from "@mui/x-tree-view/TreeItem";
import { useRouter } from "next/router";
import { TreeData } from "../lib/markdown";
import { Box, Typography } from "@mui/material";

const FileNameElement: (props: { name: string }) => JSX.Element = ({
	name,
}) => (
	<Typography
		sx={{
			fontSize: "1rem",
			paddingLeft: "6px",
			fontFamily:
				'-apple-system, BlinkMacSystemFont, "Segoe UI", Roboto, Helvetica, Arial, sans-serif,',
			lineHeight: 2.0,
		}}
	>
		{name}
	</Typography>
);

export default function FolderTree(props: {
	onNodeSelect?: () => void;
	tree: TreeData;
	flattenNodes: TreeData[];
}): JSX.Element {
	const renderTree = (nodes: TreeData): JSX.Element => {
		const dotSplit = nodes.name.split(".");
		let extension: string | undefined;
		if (dotSplit.length > 1) {
			extension = dotSplit.pop();
		}
		return (
			<TreeItem
				key={nodes.id}
				nodeId={nodes.id}
				label={
					extension === undefined ? (
						<FileNameElement name={nodes.name} />
					) : (
						<Box sx={{ display: "flex", alignItems: "center", p: 0.5, pr: 0 }}>
							<FileNameElement name={dotSplit.join(".")} />
							<Typography
								variant="caption"
								color="inherit"
								sx={{
									float: "right",
									textAlign: "right",
									borderRadius: "5px",
									fontFamily: "verdana, 'open sans', sans-serif",
									margin: "3.5px 3.5px 2.5px 4px",
									padding: "0.5px 3.5px 2.5px 4px",
								}}
								className="bg-gray-300 dark:bg-gray-600"
							>
								{extension}
							</Typography>
						</Box>
					)
				}
				className="dark:text-gray-200"
			>
				{nodes.children.map((node) => renderTree(node))}
			</TreeItem>
		);
	};

	const router = useRouter();
	// const childrenNodeIds = props.tree.children.map(aNode => {return aNode.id})
	const expandedNodes = [props.tree.id];
	return (
		<TreeView
			aria-label="rich object"
			defaultCollapseIcon={<ExpandMoreIcon />}
			defaultExpanded={expandedNodes}
			defaultExpandIcon={<ChevronRightIcon />}
			onNodeSelect={(event, nodIds) => {
				const currentNode = props.flattenNodes.find((aNode) => {
					return aNode.id === nodIds;
				});
				// console.log(event)
				// console.log(currentNode)
				if (currentNode?.routePath != null) {
					void router.push(currentNode.routePath);
					// router.reload()
					if (props.onNodeSelect !== undefined) {
						props.onNodeSelect();
					}
				}
			}}
			sx={{ flexGrow: 1, maxWidth: 400, overflowY: "auto" }}
		>
			{renderTree(props.tree)}
		</TreeView>
	);
}
