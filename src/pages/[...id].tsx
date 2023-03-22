import Head from "next/head";
import Layout from "../components/Layout";
import { getAllContentFilePaths, getDirectoryData, toFilePath, toSlug } from "../lib/slug";
import { constructGraphData, CustomNode, getLocalGraphData, LocalGraphData } from "../lib/graph";
import { getFlattenArray, TreeData } from "../lib/markdown";
import { getSearchIndex, SearchData } from "../lib/search";
import { initCache, slugs, toFileName } from "volglass-backend";
import { getMarkdownFolder, readFileSync } from "../lib/io";
import dynamic from "next/dynamic";
import MDContent from "../components/MDContentData";
import FolderTree from "../components/FolderTree";
import { SearchBar } from "../components/Search";

// TODO make customizable
// FIXME This should be a string field, but I don't know to avoid init error
export function FIRST_PAGE(): string {
  return "README";
}
interface HomeElement extends HTMLElement {
  checked: boolean;
}

const DynamicThemeSwitcher = dynamic(async () => await import("../components/ThemeSwitcher"), {
  loading: () => <p>Loading...</p>,
  ssr: false,
});

// This trick is to dynamically load component that interact with window object (browser only)
const DynamicGraph = dynamic(async () => await import("../components/Graph"), {
  loading: () => <p>Loading ...</p>,
  ssr: false,
});
export interface Prop {
  fileName: string;
  markdownContent: string;
  tree: TreeData;
  flattenNodes: TreeData[];
  graphData: LocalGraphData;
  backLinks: CustomNode[];
  searchIndex: SearchData[];
}

export default function Home({
  fileName,
  markdownContent,
  backLinks,
  tree,
  flattenNodes,
  graphData,
  searchIndex,
}: Prop): JSX.Element {
  const burgerId = "hamburger-input";
  const closeBurger = (): void => {
    const element = document.getElementById(burgerId) as HomeElement | null;
    if (element !== null) {
      element.checked = false;
    }
  };

  return (
    <Layout>
      <Head>{<meta name="title" content={fileName} />}</Head>
      <div className="fixed flex h-full w-full flex-row overflow-hidden">
        <div className="burger-menu">
          <input type="checkbox" id={burgerId} />
          <label id="hamburger-menu" htmlFor="hamburger-input">
            <span className="menu">
              {" "}
              <span className="hamburger"></span>{" "}
            </span>
          </label>
          <nav>
            <FolderTree tree={tree} flattenNodes={flattenNodes} onNodeSelect={closeBurger} />
            <DynamicGraph graph={graphData} />
          </nav>
        </div>
        <div>
          <nav className="nav-bar">
            <DynamicThemeSwitcher />
            <SearchBar index={searchIndex} />
            <FolderTree tree={tree} flattenNodes={flattenNodes} />
          </nav>
        </div>
        <MDContent fileName={fileName} content={markdownContent} backLinks={backLinks} />
        <DynamicGraph graph={graphData} />
      </div>
    </Layout>
  );
}

export async function getStaticPaths(): Promise<{
  paths: Array<{ params: { id: string[] } }>;
  fallback: false;
}> {
  initCache(getAllContentFilePaths, getMarkdownFolder, toSlug, readFileSync);
  // TODO allows to put in image files in `posts` directory
  const paths = slugs.map((p) => ({ params: { id: p.replace("/", "").split("/") } }));

  return {
    paths,
    fallback: false,
  };
}

const { nodes, edges } = constructGraphData();

export function getStaticProps({ params }: { params: { id: string[] } }): { props: Prop } {
  const slugString = params.id.join("/");
  const fileName = toFileName(slugString);
  const filePath = toFilePath(`/${slugString}`);
  const markdownContent = readFileSync(filePath);
  const tree = getDirectoryData();
  const flattenNodes = getFlattenArray(tree);

  const listOfEdges = edges.filter((anEdge) => anEdge.target === slugString);
  const internalLinks = listOfEdges
    .map((anEdge) => nodes.find((aNode) => aNode.slug === anEdge.source) ?? null)
    .filter((element): element is CustomNode => element !== null);
  const backLinks = [...new Set(internalLinks)];
  const graphData = getLocalGraphData(params.id.join("/"));
  const searchIndex = getSearchIndex();
  return {
    props: {
      fileName,
      markdownContent,
      tree,
      flattenNodes,
      backLinks: backLinks.filter((link) => link.slug !== slugString),
      graphData,
      searchIndex,
    },
  };
}
