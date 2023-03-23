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
            val targetSlug = fileNameInfo.fileNameToSlug[it] ?: error("failed to get slug. fileName:$it current: ${fileNameInfo.fileNameToSlug}")
            val targetDir = fileNameInfo.fileNameToPath[it] ?: error("failed to get path. fileName:$it current: ${fileNameInfo.fileNameToPath}")
            val summary = readContent(targetDir.path).substring(0..60)
            BackLink(it.fileName, targetSlug.slug, summary)
        }?.let {
            serialize(it)
        }
        ?: "[]"
}

@JsExport
fun deserializeBackLinks(backLinks: String): Array<BackLink> = deserialize(backLinks)
