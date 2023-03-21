import React, { FunctionComponent } from "react";
// import Alert from '@mui/material/Alert';
// import AlertTitle from '@mui/material/AlertTitle';
import { useRouter } from "next/router";
import { CustomNode } from "../lib/graph";
import Footer from "./Footer";
import { RoutableProps } from "volglass-backend";

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
  content: FunctionComponent<RoutableProps>;
  backLinks: CustomNode[];
}

function MDContent({ content, backLinks }: MDContentData): JSX.Element {
  // function handleInternalLinkClick() {
  //     //Processing fetching
  //     //pass result up to parent container
  //     //TODO: handle clicking on internal link, go fetching md content from file then passing it up to parent
  //     handleOpenNewContent(content)
  // }

  const Content = content;

  const router = useRouter();

  return (
    <div className="markdown-rendered">
      <div className="mt-4 overflow-hidden overflow-y-auto px-8">
        <Content
          push={(slug) => {
            void router.push(slug);
          }}
        />
        {/* <button onClick={handleInternalLinkClick}>Click me</button> */}
        {/* <hr/> */}
        <div style={{ marginBottom: "3em" }}>
          <BackLinks linkList={backLinks} />
        </div>
      </div>
      <Footer />
    </div>
  );
}

export default MDContent;
