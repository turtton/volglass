import dynamic from "next/dynamic";
import Head from "next/head";
import {
	getBackLinks,
	getCacheData,
	initCache,
	isMediaFile,
	toFileName,
	toFilePath,
	toRawFileName,
} from "volglass-backend";
import FolderTree from "../components/FolderTree";
import Layout from "../components/Layout";
import MDContent from "../components/MDContentData";
import { SearchBar } from "../components/Search";
import { LocalGraphData, getLocalGraphData } from "../lib/graph";
import {
	clearPublicDir,
	copyToPublicFolder,
	getMarkdownFolder,
	getPublicFolder,
	readFileSync,
} from "../lib/io";
import { TreeData, getFlattenArray } from "../lib/markdown";
import { SearchData, getSearchIndex } from "../lib/search";
import { getAllContentFilePaths, getDirectoryData, toSlug } from "../lib/slug";

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
	slugName: string;
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
	slugName,
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
			<Head>{<meta name="title" content={slugName} />}</Head>
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
					slugName={slugName}
					fileName={fileName}
					content={markdownContent}
					cacheData={cacheData}
					backLinks={backLinks}
				/>
				{!slugName.match(".canvas") ? (
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
	const searchIndex = getSearchIndex();
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
	const paths = slugs
		.map((p) => ({
			params: { id: p.replace(/^\//, "").split("/") },
		}))
		.filter((p) => p.params.id.length !== 0 && p.params.id[0] !== "");
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
	const slugString = `/${params.id.join("/")}`;
	const slugName = toFileName(slugString, cacheData);
	const fileName = toRawFileName(slugString, cacheData) ?? slugName;
	const filePath = toFilePath(slugString, cacheData);
	const markdownContent = isMediaFile(slugName) ? "" : readFileSync(filePath);
	const flattenNodes = getFlattenArray(tree);
	const backLinks = getBackLinks(slugString, cacheData, readFileSync);

	const graphData = getLocalGraphData(slugString, cacheData);

	return {
		props: {
			slugName,
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
