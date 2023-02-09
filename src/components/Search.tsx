import { TextField } from "@mui/material";
import Fuse from "fuse.js";
import IFuseOptions = Fuse.IFuseOptions;
import { SearchData } from "../lib/search";
import { useState } from "react";
import { useRouter } from "next/router";
import { isJapanese, toRomaji } from "wanakana";

interface SearchProp {
  index: readonly SearchData[];
}

interface ResultProp {
  contents: DataResult;
  hidden: boolean;
}

type DataResult = readonly SearchData[] | "Empty";

const fuseConfig: IFuseOptions<SearchData> = {
  keys: ["rawTitle", "rawContent"],
};

export function SearchBar(prop: SearchProp): JSX.Element {
  const fuse = new Fuse(prop.index, fuseConfig);
  const [hitData, setHitData] = useState("Empty" as DataResult);
  const [isResultHidden, setResultHidden] = useState(false);
  return (
    <div className="mb-4 px-2.5">
      <TextField
        className="bg-white"
        fullWidth
        label="Search"
        onBlur={() => {
          setResultHidden(true);
        }}
        onFocus={() => {
          setResultHidden(false);
        }}
        onChange={(input) => {
          let text = input.target.value;
          if (text.length === 0) {
            setHitData("Empty");
            return;
          }
          if (isJapanese(text)) {
            text = toRomaji(text);
          }
          let result = fuse.search(text).map((r) => r.item);
          const titles = result.map((r) => r.title);
          result = result.filter((value, index) => index === titles.indexOf(value.title));
          setHitData(result);
        }}
      />
      <ResultList contents={hitData} hidden={isResultHidden} />
    </div>
  );
}

function ResultList({ contents, hidden }: ResultProp): JSX.Element {
  const router = useRouter();
  if (contents === "Empty") {
    return <div hidden={true} />;
  }
  return (
    <div
      className="absolute z-10 mt-2 w-11/12 divide-y divide-gray-100 rounded-lg bg-white shadow dark:bg-gray-700"
      hidden={hidden}
    >
      <ul className="py-2 text-sm text-gray-700 dark:text-gray-200">
        {contents.length === 0 ? (
            <li className="inline-flex w-full px-4 py-2">
              <p>Not Found</p>
            </li>
          ) :
          contents.map((data) => (
            <li
              key={data.rawTitle}
              className="inline-flex w-full px-4 py-2 dark:hover:bg-gray-600 dark:hover:text-white"
              onClick={(): void => {
                void router.push(data.path);
              }}
            >
              <button
                className="w-full truncate"
                key={data.title}
              >
                <p className="w-full truncate text-left underline">{data.title}</p>
                <p className="truncate text-left text-xs text-gray-500">
                  {data.lineAt}: {data.singleLineContent}
                </p>
              </button>
            </li>
          ))
        }
      </ul>
    </div>
  );
}
