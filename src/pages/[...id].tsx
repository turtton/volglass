import Head from "next/head";
import Layout from "../components/Layout";
import { getAllContentFilePaths, getDirectoryData, toSlug } from "../lib/slug";
import { getLocalGraphData, LocalGraphData } from "../lib/graph";
import { getFlattenArray, TreeData } from "../lib/markdown";
import { getSearchIndex, SearchData } from "../lib/search";
import {
	getBackLinks,
	getCacheData,
	initCache,
	isMediaFile,
	toFileName,
	toFilePath,
} from "volglass-backend";
import {
	clearPublicDir,
	copyToPublicFolder,
	getMarkdownFolder,
	getPublicFolder,
	readFileSync,
} from "../lib/io";
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

const DynamicThemeSwitcher = dynamic(
	async () => await import("../components/ThemeSwitcher"),
	{
		loading: () => <p>Loading...</p>,
		ssr: false,
	},
);

// This trick is to dynamically load component that interact with window object (browser only)
const DynamicGraph = dynamic(async () => await import("../components/Graph"), {
	loading: () => <p>Loading ...</p>,
	ssr: false,
});
export interface Prop {
	fileName: string;
	markdownContent: string;
	cacheData: string;
	tree: TreeData;
	flattenNodes: TreeData[];
	graphData: LocalGraphData;
	backLinks: string;
	searchIndex: SearchData[];
}

export default function Home({
	fileName,
	markdownContent,
	cacheData,
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
			<div className="fixed flex h-full w-full flex-row overflow-hidden dark:bg-dark-background-primary">
				<div className="burger-menu">
					<input type="checkbox" id={burgerId} />
					<label id="hamburger-menu" htmlFor="hamburger-input">
						<span className="menu">
							{" "}
							<span className="hamburger" />{" "}
						</span>
					</label>
					<nav>
						<FolderTree
							tree={tree}
							flattenNodes={flattenNodes}
							onNodeSelect={closeBurger}
						/>
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
				<MDContent
					fileName={fileName}
					content={markdownContent}
					cacheData={cacheData}
					backLinks={backLinks}
				/>
				{!fileName.match(".canvas") ? (
					<DynamicGraph graph={graphData} />
				) : (
					<></>
				)}
			</div>
		</Layout>
	);
}

export async function getStaticPaths(): Promise<{
	paths: Array<{ params: { id: string[] } }>;
	fallback: false;
}> {
	clearPublicDir();
	const directoryData = getDirectoryData();
	const searchIndex = getSearchIndex()
	const slugs = await initCache(
		JSON.stringify(directoryData),
		JSON.stringify(searchIndex),
		getAllContentFilePaths,
		getMarkdownFolder,
		getPublicFolder,
		toSlug,
		readFileSync,
		copyToPublicFolder,
	);
	// TODO allows to put in image files in `posts` directory
	const paths = slugs.map((p) => ({
		params: { id: p.replace("/", "").split("/") },
	}));
	return {
		paths,
		fallback: false,
	};
}

export async function getStaticProps({
	params,
}: {
	params: { id: string[] };
}): Promise<{ props: Prop }> {
	const [cacheData, rawTreeData, rawSearchIndex] = await getCacheData();
	const tree: TreeData = JSON.parse(rawTreeData);
	const searchIndex: SearchData[] = JSON.parse(rawSearchIndex);
	let slugString = `/${params.id.join("/")}`;
	const fileName = toFileName(slugString, cacheData);
	const filePath = toFilePath(slugString, cacheData);
	const markdownContent = isMediaFile(fileName) ? "" : readFileSync(filePath);
	const flattenNodes = getFlattenArray(tree);
	const backLinks = getBackLinks(slugString, cacheData, readFileSync);

	const graphData = getLocalGraphData(slugString, cacheData);

	return {
		props: {
			fileName,
			markdownContent,
			cacheData,
			tree,
			flattenNodes,
			backLinks,
			graphData,
			searchIndex,
		},
	};
}
