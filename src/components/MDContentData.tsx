import React from "react";
// import Alert from '@mui/material/Alert';
// import AlertTitle from '@mui/material/AlertTitle';
import { CustomNode } from "../lib/graph";
import Footer from "./Footer";
import { useRouter } from "next/router";
import { getContent } from "volglass-backend";

function BackLinks({ linkList }: { linkList: CustomNode[] }): JSX.Element {
  return (
    <div className="note-footer">
      <h3 className="backlink-heading">Link to this note</h3>
      {linkList != null && linkList.length > 0 ? (
        <>
          <div className="backlink-container">
            {linkList.map((aLink) => (
              <div key={aLink.slug} className="backlink">
                {/* <Link href={aLink.slug}> */}
                <a href={aLink.slug}>
                  <p className="backlink-title">{aLink.title}</p>
                  <p className="backlink-preview">{aLink.shortSummary} </p>
                </a>
                {/* </Link> */}
              </div>
            ))}
          </div>
        </>
      ) : (
        <>
          {" "}
          <p className="no-backlinks"> No backlinks found</p>{" "}
        </>
      )}
    </div>
  );
}

export interface MDContentData {
  fileName: string;
  content: string;
  cacheData: string;
  backLinks: CustomNode[];
}

function MDContent({ fileName, content, cacheData, backLinks }: MDContentData): JSX.Element {
  const router = useRouter();

  const Content = getContent(fileName, `# ${fileName}\n${content}`, cacheData, router);
  return (
    <div className="markdown-rendered">
      <div className="mt-4 overflow-hidden overflow-y-auto px-8">
        <Content />
        <div style={{ marginBottom: "3em" }}>
          <BackLinks linkList={backLinks} />
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default MDContent;
