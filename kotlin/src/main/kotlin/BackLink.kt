@file:OptIn(ExperimentalJsExport::class)

import kotlinx.serialization.Serializable

@JsExport
@Serializable
data class BackLink(
    val title: String,
    val slug: String,
    val summary: String,
)

@JsExport
fun getBackLinks(slug: String, cacheData: String, readContent: (String) -> String): String {
    val slugString = SlugString(slug)
    val (dependencyData, fileNameInfo) = deserialize<CacheData>(cacheData)
    val duplicatedFile = fileNameInfo.duplicatedFile
    return dependencyData.linkDependencies[slugString.toFileName(duplicatedFile)]
        ?.map {
            val targetSlug = fileNameInfo.fileNameToSlug[it]!!
            val targetDir = fileNameInfo.fileNameToPath[it]!!
            val summary = readContent(targetDir.path).substring(0..60)
            BackLink(it.fileName, targetSlug.slug, summary)
        }?.let {
            serialize(it)
        }
        ?: "[]"
}

@JsExport
fun deserializeBackLinks(backLinks: String): Array<BackLink> = deserialize(backLinks)
