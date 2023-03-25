import fs, { PathLike } from "fs";
import path from "path";

export function getAllMarkdownFiles(): string[] {
	return getFiles(getMarkdownFolder());
}

export function getFiles(dir: string): string[] {
	let results: string[] = [];
	fs.readdirSync(dir).forEach(function (file) {
		file = `${dir}/${file}`;
		const stat = fs.statSync(file);
		if (stat?.isDirectory()) {
			/* Recurse into a subdirectory */
			results = results.concat(getFiles(file));
		} else {
			/* Is a file */
			results.push(file);
		}
	});
	return results.filter((f) => f !== ".gitignore");
}

export function readFileSync(fullPath): string {
	return fs.readFileSync(fullPath, "utf8");
}

export function getMarkdownFolder(): string {
	return path.join(process.cwd(), "posts");
}

export function getPublicFolder(): string {
	return path.join(process.cwd(), "public");
}

export function isFile(filename: PathLike): boolean {
	try {
		return fs.lstatSync(filename).isFile();
	} catch (err) {
		console.log(err);
		return false;
	}
}

/**
 * Copies file that is in post folder to public folder, and adds `img` before extension
 * Example: /path/to/post/dir/img.png -> /path/to/public/dir/img-img.png
 * @return result path(/path/to/public/dir/img-img.png)
 */
export function copyToPublicFolder(targetPath: string): string {
	const postDir = getMarkdownFolder();
	const publicDir = getPublicFolder();

	let relativePath = targetPath.replace(postDir, "");
	const splitSlug = relativePath.split("/");
	let fileName: string | undefined;
	if (splitSlug.length > 1) {
		// remove fileName
		fileName = splitSlug.pop();

		const subDir = splitSlug.join("/");
		fs.mkdirSync(`${publicDir}${subDir}`, { recursive: true });
	}
	// dir/img.png -> img.png
	if (fileName === undefined) {
		fileName = splitSlug[splitSlug.length - 1];
	}
	// img.png -> img
	const rawName = fileName.split(".")[0];
	// dir/img.png -> dir/img-img.png
	relativePath = relativePath.replace(rawName, `${rawName}-img`);
	const resultPath = `${publicDir}${relativePath}`;
	fs.copyFileSync(targetPath, resultPath);
	return resultPath;
}

export function clearPublicDir(): void {
	fs.readdirSync(getPublicFolder()).forEach((file) => {
		if (!file.endsWith("favicon.ico")) {
			fs.rmSync(file, { recursive: true, force: true });
		}
	});
}
