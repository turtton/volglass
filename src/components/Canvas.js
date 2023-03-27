import { useMemo } from "react";
import ReactFlow, { Background, ConnectionLineType, Controls } from "reactflow";
import LinkNode from "./LinkNode";

function Canvas({ edges, nodes }) {
    const nodeTypes = useMemo(
        () => ({
            // file: FileNode,
            // markdown: MarkdownNode,
            link: LinkNode,
        }),
        []
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
            minZoom="0.03"
          >
            <Background />
            <Controls />
          </ReactFlow>
        )}
      </div>
    );
}

export default Canvas;
