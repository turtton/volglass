import Footer from "./Footer";
import { useRouter } from "next/router";
import { deserializeBackLinks, getContent } from "volglass-backend";
import { refractor } from "refractor/lib/all";
import { toHtml } from "hast-util-to-html";
import mermaid from "mermaid";
import { useCurrentTheme } from "./ThemeSwitcher";
import katex from "katex";
import Canvas from "./Canvas";

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
							<div key={aLink.slug}>
								{/* TODO Implement onKeyDown event
									 rome-ignore lint: lint/a11y.useKeyWithClickEvents*/}
								<div
									className="backlink text-text-accent hover:text-text-accent-hover dark:dark-text-accent dark:hover:dark-text-accent-hover"
									onClick={() => {
										void router.push(aLink.slug);
									}}
								>
									<p className="backlink-title">{aLink.title}</p>
									<p className="backlink-preview">{aLink.summary} </p>
								</div>
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

const codeEncoder = (code, language) => {
	let result: string;
	try {
		result = toHtml(refractor.highlight(code, language));
	} catch (e) {
		console.error("Failed to parse code block", e);
		result = code;
	}
	return result;
};

let currentId = 0;
const createUuid = () => `mermaid-${currentId++}`;
const renderMermaid =
	(isDark: boolean) => (content: string, setter: (string) => void) => {
		mermaid.initialize({ theme: isDark ? "dark" : "light" });
		mermaid
			.render(createUuid(), content)
			.then((result) => setter(result.svg))
			.catch((e) => {
				setter(
					"<p class='text-red-600 dark:text-red-400'>Mermaid: Syntax error in graph</p>",
				);
				console.error(`Error occurred by Mermaid.\nContent:\n${content}\n`, e);
			});
	};

const renderTex = (content: string, isNewLine: boolean) =>
	katex.renderToString(content, {
		displayMode: isNewLine,
		throwOnError: false,
		trust: true,
		output: "mathml",
	});

export interface MDContentData {
	fileName: string;
	content: string;
	cacheData: string;
	backLinks: string;
}

function MDContent({
	fileName,
	content,
	cacheData,
	backLinks,
}: MDContentData): JSX.Element {
	const router = useRouter();
	const Content = getContent(
		fileName,
		`${content}`,
		cacheData,
		router,
		codeEncoder,
		renderMermaid(useCurrentTheme() === "dark"),
		renderTex,
		Canvas,
	);
	if (fileName.match(".canvas")) {
		return (
			<>
				<div className="mt-4 overflow-hidden overflow-y-auto px-8 w-full dark:text-white">
					<div className="mx-auto text-center">
						<strong>{fileName.replace(".canvas", "")}</strong>
					</div>
					<Content />
				</div>
				<Footer />
			</>
		);
	}
	return (
		<div className="markdown-rendered">
			<div className="mt-4 overflow-hidden overflow-y-auto px-8">
				{fileName.match(/\.[a-zA-Z0-9]/) ? (
					<strong>{fileName}</strong>
				) : (
					<h1>{fileName}</h1>
				)}
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
