import { getAllMarkdownFiles, getFiles, getMarkdownFolder, isFile, readFileSync } from "./io";
import directoryTree from "directory-tree";
import markdown from "remark-parse";
import { toString } from "mdast-util-to-string";
import { convertTreeData, TreeData } from "./markdown";
import { Transformer } from "./transformer";
import unified from "unified";
import { FIRST_PAGE } from "../pages/[...id]";

interface SlugMap extends Map<string, string> {
  index: string;
}

const cachedSlugMap = getSlugHashMap();

export interface Content {
  id: string;
  title: string;
  data: string[];
}

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
  getAllMarkdownFiles().forEach((aFile) => {
    const aSlug = toSlug(aFile);
    // if (slugMap.has(aSlug)) {
    //     slugMap[aSlug].push(aFile)
    // } else {
    //     slugMap[aSlug] = [aFile]
    // }
    // Note: [Future improvement] Resolve conflict
    slugMap.set(aSlug, aFile);
  });

  const firstMd = `${FIRST_PAGE()}.md`;
  slugMap.set(FIRST_PAGE(), getMarkdownFolder() + `/${firstMd}`);
  slugMap.set("/", getMarkdownFolder() + `/${firstMd}`);

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
    return filePath.replace(getMarkdownFolder(), "").replace(" ", "+").replace(".md", "");
  } else {
    // TODO handle this properly
    return "/";
  }
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

let treeDataCache: TreeData | undefined;

export function getDirectoryData(): TreeData {
  if (treeDataCache === undefined) {
    const filteredDirectory = directoryTree(getMarkdownFolder(), {
      exclude: /.gitkeep/g,
    });
    treeDataCache = convertTreeData(filteredDirectory);
  }

  return treeDataCache;
}

export function getContent(slug: string): string | null {
  const currentFilePath = toFilePath(slug);
  if (currentFilePath === undefined || currentFilePath == null) return null;
  return readFileSync(currentFilePath);
}

export function getShortSummary(slug: string): string {
  const content = getContent(slug);
  if (content === null) {
    return "";
  }

  const tree = unified().use(markdown).parse(content);
  const plainText = toString(tree);
  return plainText.split(" ").splice(0, 40).join(" ");
}

export function getRouterPath(fileName: string): string | null {
  const routerPath = getAllSlugs().find((slug) => {
    const slugFileName = Transformer.parseFileNameFromPath(toFilePath(slug));
    return (
      Transformer.normalizeFileName(slugFileName ?? "") === Transformer.normalizeFileName(fileName)
    );
  });
  const nameAndExtension = fileName.split(".");
  return nameAndExtension.length > 1 && routerPath !== undefined ? routerPath : null;
}
