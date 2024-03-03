import directoryTree from "directory-tree";
import { FIRST_PAGE } from "../pages/[...id]";
import { getAllMarkdownFiles, getFiles, getMarkdownFolder, isFile } from "./io";
import { TreeData, convertTreeData } from "./markdown";
import { Transformer } from "./transformer";

interface SlugMap extends Map<string, string> {
	index: string;
}

const cachedSlugMap = getSlugHashMap();

export function toFilePath(slug: string): string {
	const result = cachedSlugMap.get(slug);
	return result ?? "";
}

export function getSlugHashMap(): Map<string, string> {
	// This is to solve problem of converting between slug and filepath,
	// where previously if I convert a slug to a file path sometime
	// it does not always resolve to correct filepath, converting function is not bi-directional
	// and not conflict-free, other solution was considered (hash file name into a hash, but this
	// is not SEO-friendly and make url look ugly ==> I chose this

	const slugMap = new Map<string, string>() as SlugMap;
	for (const aFile of getAllMarkdownFiles()) {
		const aSlug = toSlug(aFile);
		// if (slugMap.has(aSlug)) {
		//     slugMap[aSlug].push(aFile)
		// } else {
		//     slugMap[aSlug] = [aFile]
		// }
		// Note: [Future improvement] Resolve conflict
		slugMap.set(aSlug, aFile);
	}

	const firstMd = `${FIRST_PAGE()}.md`;
	slugMap.set(FIRST_PAGE(), `${getMarkdownFolder()}/${firstMd}`);
	slugMap.set("/", `${getMarkdownFolder()}/${firstMd}`);

	return slugMap;
}

/**
 * Converts filePath to Slug
 *
 * Examples:
 * - posts/path/to/Content.md -> path/to/Content
 * - posts/path/to/picture.jpg -> path/to/picture.jpg
 * @param filePath
 */
export function toSlug(filePath: string): string {
	if (isFile(filePath) && filePath.includes(getMarkdownFolder())) {
		return filePath
			.replace(getMarkdownFolder(), "")
			.replaceAll(" ", "+")
			.replace(".md", "");
	}
	// TODO handle this properly
	return "/";
}

export function getAllContentFilePaths(): string[] {
	return getFiles(getMarkdownFolder()).filter(
		(f) => !(f.endsWith(FIRST_PAGE()) || f.endsWith("sidebar")),
	);
}

export function getAllSlugs(): string[] {
	// console.log("\n\nAll Posts are scanning")
	// Get file names under /posts
	const filePaths = getAllContentFilePaths();
	return filePaths.map((f) => toSlug(f));
}

export function getDirectoryData(): TreeData {
	const filteredDirectory = directoryTree(getMarkdownFolder(), {
		exclude: /.gitkeep/g,
	});
	return convertTreeData(filteredDirectory);
}

export function getRouterPath(fileName: string): string | null {
	const routerPath = getAllSlugs().find((slug) => {
		const slugFileName = Transformer.parseFileNameFromPath(toFilePath(slug));
		return (
			Transformer.normalizeFileName(slugFileName ?? "") ===
			Transformer.normalizeFileName(fileName)
		);
	});
	const nameAndExtension = fileName.split(".");
	return nameAndExtension.length > 1 && routerPath !== undefined
		? routerPath
		: null;
}
