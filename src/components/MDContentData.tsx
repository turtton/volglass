import React from "react";
import Footer from "./Footer";
import { useRouter } from "next/router";
import { deserializeBackLinks, getContent } from "volglass-backend";

function BackLinks({ backLink }: { backLink: string }): JSX.Element {
  const linkList = deserializeBackLinks(backLink);
  const router = useRouter();
  return (
    <div className="note-footer">
      <h3 className="backlink-heading">Link to this note</h3>
      {linkList != null && linkList.length > 0 ? (
        <>
          <div className="backlink-container">
            {linkList.map((aLink) => (
              <a key={aLink.slug}>
                <div className="backlink" onClick={() => {void router.push(aLink.slug);}}>
                  <p className="backlink-title">{aLink.title}</p>
                  <p className="backlink-preview">{aLink.summary} </p>
                </div>
              </a>
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
  backLinks: string;
}

function MDContent({ fileName, content, cacheData, backLinks }: MDContentData): JSX.Element {
  const router = useRouter();

  const Content = getContent(fileName, `# ${fileName}\n${content}`, cacheData, router);
  return (
    <div className="markdown-rendered">
      <div className="mt-4 overflow-hidden overflow-y-auto px-8">
        <Content />
        <div style={{ marginBottom: "3em" }}>
          <BackLinks backLink={backLinks} />
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default MDContent;
