package xyz.cssxsh.mirai.plugin

import io.github.humbleui.skija.*
import io.github.humbleui.skija.paragraph.*
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.skija.FontStyles
import java.io.*
import java.util.zip.*

public object MiraiTypefaceFontProvider : TypefaceFontProvider() {

    init {
        FontStyles.instances.add(this)
    }

    /**
     * 加载字体
     * @param folder 字体文件文件夹
     */
    @JvmSynthetic
    public suspend fun loadTypeface(folder: File, vararg links: String): Unit = withContext(Dispatchers.IO) {
        val downloaded = mutableListOf<File>()
        val download = folder.resolve("download")

        download.mkdirs()

        for (link in links) {
            try {
                downloaded.add(download(urlString = link, folder = download))
            } catch (cause: Throwable) {
                MiraiSkijaPlugin.logger.warning({ "字体下载失败, $link" }, cause)
            }
        }

        for (pack in downloaded) {
            when (pack.extension) {
                "7z" -> {
                    ProcessBuilder(sevenZA(folder = download).absolutePath, "x", pack.absolutePath, "-y")
                        .directory(folder)
                        .start()
                        // 防止卡顿
                        .apply { inputStream.transferTo(OutputStream.nullOutputStream()) }
                        .waitFor()
                }
                "zip" -> {
                    ZipFile(pack).use { zip ->
                        for (entry in zip.entries()) {
                            with(folder.resolve(entry.name)) {
                                if (exists().not()) {
                                    outputStream().use { output ->
                                        zip.getInputStream(entry).use { input ->
                                            input.transferTo(output)
                                        }
                                    }
                                }
                                setLastModified(entry.lastModifiedTime.toMillis())
                            }
                        }
                    }
                }
                else -> Unit
            }
        }

        loadTypeface(folder = folder)
    }

    /**
     * 从指定目录加载字体
     * @param folder 字体文件文件夹
     */
    public fun loadTypeface(folder: File) {
        for (file in folder.listFiles().orEmpty()) {
            when (file.extension) {
                "ttf" -> registerTypeface(Typeface.makeFromFile(file.path))
                "ttc" -> registerTypeface(Typeface.makeFromFile(file.path))
                "otf" -> registerTypeface(Typeface.makeFromFile(file.path))
                else -> continue
            }
        }
    }
}