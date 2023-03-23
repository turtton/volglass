@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.Serializable

@JsExport
@Serializable
data class BackLink(
    val title: String,
    val slug: SlugString,
    val summary: String,
)

@JsExport
fun getBackLinks(slugString: SlugString, cacheData: String, readContent: (PathString) -> String): String {
    val (dependencyData, fileNameInfo) = deserialize<CacheData>(cacheData)
    val postFolder = fileNameInfo.postFolderFullPath
    val duplicatedFile = fileNameInfo.duplicatedFile
    return dependencyData.linkDependencies[slugString.toFileName(postFolder, duplicatedFile)]
        ?.map {
            val title = it.toFileName(postFolder, duplicatedFile)
            val slug = fileNameInfo.fileNameToSlug[it]!!
            val targetDir = fileNameInfo.fileNameToPath[it]!!
            val summary = readContent(targetDir).substring(0..60)
            BackLink(title, slug, summary)
        }?.let {
            serialize(it)
        }
        ?: "[]"
}

@JsExport
fun deserializeBackLinks(backLinks: String): Array<BackLink> = deserialize(backLinks)
