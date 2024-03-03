import fs from "fs";
import { isJapanese, toKana, toRomaji, tokenize } from "wanakana";
import { getAllMarkdownFiles } from "./io";
import { getRouterPath } from "./slug";
import { Transformer } from "./transformer";

export interface SearchData {
	title: string;
	rawTitle: string | null;
	singleLineContent: string;
	rawContent: RawContent | null;
	lineAt: number;
	path: string;
}

export interface RawContent {
	raw: string;
	separatedOriginal: string[];
	separatedRaw: string[];
}

export function getSearchIndex(): SearchData[] {
	const result: SearchData[] = [];
	const filePaths = getAllMarkdownFiles();
	for (const markdownFile of filePaths) {
		const title = Transformer.parseFileNameFromPath(markdownFile);
		if (title == null || title.match(/\.[a-zA-Z0-9]*$/)) {
			break;
		}
		const rawTitle = isJapanese(title) ? toRomaji(title) : title;
		const content = fs.readFileSync(markdownFile);
		if (content === null || content === undefined) {
			break;
		}
		const path = getRouterPath(markdownFile);
		if (path === null) {
			break;
		}
		content
			.toString()
			.split("\n")
			.forEach((line, index) => {
				if (line.match("```") !== null || line.match("---") !== null) return;
				let rawContent: RawContent | null = null;
				if (isJapanese(line)) {
					const raw = toRomaji(line);
					const kenized = tokenize(line).map((t: string | { type: string, value: string}) => {
						if (typeof t === "string") {
							return t;
						} else {
							return t.value;
						}
					});
					const kanaizedRomaji = kenized.map((k) => toRomaji(toKana(k)));
					rawContent = {
						raw,
						separatedOriginal: kenized,
						separatedRaw: kanaizedRomaji,
					};
				}
				result.push({
					title,
					rawTitle,
					singleLineContent: line,
					lineAt: index,
					path,
					rawContent,
				});
			});
	}
	return result;
}
