import { memo } from "react";
import { Handle, Position } from "reactflow";

function LinkNode({ data, isConnectable }) {
    return (
        <a href={data.value}>
            <div className="link-node"></div>
            <h2>{data.text}</h2>
            <Handle
                id="top"
                type="target"
                position={Position.Top}
                style={{ background: "#555" }}
                isConnectable={isConnectable}
            />
            <Handle
                id="bottom"
                type="source"
                position={Position.Bottom}
                style={{ background: "#555" }}
                isConnectable={isConnectable}
            />
        </a>
    );
}

export default memo(LinkNode);
