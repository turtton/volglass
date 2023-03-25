import { DirectoryTree } from "directory-tree";
import { getRouterPath } from "./slug";

export interface TreeData {
	name: string;
	children: TreeData[];
	id: string;
	routePath: string | null;
}

export function convertTreeData(thisObject: DirectoryTree): TreeData {
	const children: TreeData[] = [];

	const objectName = thisObject.name;
	const routerPath = getRouterPath(objectName);
	const newObject: TreeData = {
		name: objectName.replace(".md", ""),
		children,
		id: objectName,
		routePath: routerPath,
	};

	if (thisObject.children != null && thisObject.children.length > 0) {
		thisObject.children.forEach((aChild) => {
			const newChild = convertTreeData(aChild);
			children.push(newChild);
		});
		return newObject;
	} else {
		return newObject;
	}
}

function flat(array: TreeData[]): TreeData[] {
	let result: TreeData[] = [];
	array.forEach(function (a) {
		result.push(a);
		if (Array.isArray(a.children)) {
			result = result.concat(flat(a.children));
		}
	});
	return result;
}

export function getFlattenArray(thisObject: TreeData): TreeData[] {
	return flat(thisObject.children);
}
