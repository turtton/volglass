export const Transformer = {
  /* Normalize File Names */
  normalizeFileName: function (filename: string) {
    let processedFileName = filename.replace(".md", "");
    processedFileName = processedFileName.replace("(", "").replace(")", "");
    processedFileName = processedFileName.split(" ").join("-");
    processedFileName = processedFileName.toLowerCase();
    const conversionLetters = [
      ["ç", "c"],
      ["ş", "s"],
      ["ı", "i"],
      ["ü", "u"],
      ["ö", "o"],
      ["ğ", "g"],
      ["Ç", "C"],
      ["Ş", "S"],
      ["İ", "I"],
      ["Ü", "U"],
      ["Ö", "O"],
      ["Ğ", "G"],
    ];
    conversionLetters.forEach((letterPair) => {
      processedFileName = processedFileName.split(letterPair[0]).join(letterPair[1]);
      // processedFileName = processedFileName.replace(letterPair[0], letterPair[1])
    });
    // console.log("filename", processedFileName)
    return processedFileName;
  },
  /* Parse file name from path then sanitize it */
  parseFileNameFromPath: function (filepath: string): string | null {
    if (filepath.includes("/")) {
      const splitPath = filepath.split("/");
      const parsedFileFromPath = splitPath[splitPath.length - 1];
      return parsedFileFromPath.replace(".md", "");
    } else {
      return null;
    }
  },
};
