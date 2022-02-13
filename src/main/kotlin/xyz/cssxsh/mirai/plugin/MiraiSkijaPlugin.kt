package xyz.cssxsh.mirai.plugin

import io.github.humbleui.skija.*
import io.github.humbleui.skija.impl.*
import io.github.humbleui.skija.paragraph.*
import kotlinx.coroutines.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.skija.*
import java.io.*
import java.util.zip.*

public object MiraiSkijaPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-skija-plugin",
        name = "mirai-skija-plugin",
        version = "1.0.0-RC4",
    ) {
        author("cssxsh")
    }
) {
    /**
     * 字体管理器，可以通过 [download] 批量加载
     */
    public val fontProvider: TypefaceFontProvider by lazy { TypefaceFontProvider() }

    private val fonts get() = dataFolder.resolve("fonts")

    /**
     * 从指定目录加载字体到 [fontProvider]
     * @param folder 字体文件文件夹
     */
    public fun loadTypeface(folder: File) {
        folder.mkdirs()
        for (file in folder.listFiles().orEmpty()) {
            when (file.extension) {
                "ttf" -> fontProvider.registerTypeface(Typeface.makeFromFile(file.path), file.nameWithoutExtension)
                "ttc" -> fontProvider.registerTypeface(Typeface.makeFromFile(file.path), file.nameWithoutExtension)
                else -> continue
            }
        }
    }

    /**
     * 加载字体
     */
    @JvmSynthetic
    public suspend fun loadTypeface(vararg links: String): Unit = withContext(Dispatchers.IO) {
        val downloaded = mutableListOf<File>()
        val download = fonts.resolve("download")

        download.mkdirs()

        for (link in links) {
            try {
                downloaded.add(download(urlString = link, folder = download))
            } catch (cause: Throwable) {
                logger.warning({ "字体下载失败, $link" }, cause)
            }
        }

        for (pack in downloaded) {
            when (pack.extension) {
                "7z" -> {
                    ProcessBuilder(sevenZA(folder = download).absolutePath, "x", pack.absolutePath, "-y")
                        .directory(fonts)
                        .start()
                        // 防止卡顿
                        .apply { inputStream.transferTo(OutputStream.nullOutputStream()) }
                        .waitFor()
                }
                "zip" -> {
                    ZipFile(pack).use { zip ->
                        for (entry in zip.entries()) {
                            with(fonts.resolve(entry.name)) {
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

        loadTypeface(folder = fonts)
    }

    @JvmSynthetic
    public suspend fun loadPetPetSprite(): Unit = withContext(Dispatchers.IO) {
        val sprite = download(urlString = "https://benisland.neocities.org/petpet/img/sprite.png", dataFolder)
        System.setProperty(PET_PET_SPRITE, sprite.absolutePath)
    }

    override fun onEnable() {
        loadTypeface(folder = fonts)

        logger.info { "platform: ${Platform.CURRENT}" }
        logger.info { "fonts: ${fontProvider.makeFamilies().keys}" }

        launch {
            loadPetPetSprite()
            if (fontProvider.familiesCount == 0) {
                logger.info { "字体文件夹为空，尝试下载 sarasa-gothic" }
                loadTypeface(
                    "https://mirrors.tuna.tsinghua.edu.cn/github-release/be5invis/Sarasa-Gothic/Sarasa%20Gothic%20version%200.35.8/sarasa-gothic-ttc-0.35.8.7z"
                )
            }
        }
    }
}