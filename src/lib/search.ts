import { getAllMarkdownFiles, isFile } from "./io";
import path from "path";
import fs from "fs";
import { Transformer } from "./transformer";
import { isJapanese, toRomaji } from "wanakana";
import { getRouterPath } from "./slug";

export interface SearchData {
  title: string;
  rawTitle: string;
  singleLineContent: string;
  rawContent: string;
  lineAt: number;
  path: string;
}

export function getSearchIndex(): SearchData[] {
  const filePath = path.join(process.cwd(), "search-index.json");
  if (fs.existsSync(filePath) && isFile(filePath)) {
    const rawData = fs.readFileSync(filePath);
    return JSON.parse(rawData.toString());
  } else {
    const result: SearchData[] = []
    try {
      fs.rmSync(filePath);
    } catch (ignore) {
    }
    const filePaths = getAllMarkdownFiles();
    filePaths.forEach((markdownFile) => {
      const title = Transformer.parseFileNameFromPath(markdownFile);
      if (title == null) {
        return;
      }
      const rawTitle = isJapanese(title) ? toRomaji(title) : title;
      const content = fs.readFileSync(markdownFile)
      if (content === null || content === undefined) {
        return;
      }
      const path = getRouterPath(`${title}.md`)
      if (path === null) { return; }
      content.toString()
        .split('\n')
        .forEach((line, index) => {
          if (line.match("```") !== null || line.match("---") !== null) return;
          const rawContent = isJapanese(line) ? toRomaji(line) : line
          result.push({
            title,
            rawTitle,
            singleLineContent: line,
            rawContent,
            lineAt: index,
            path
          })
        })
    });
    fs.writeFileSync(filePath, JSON.stringify(result), "utf-8");
    return result
  }
}