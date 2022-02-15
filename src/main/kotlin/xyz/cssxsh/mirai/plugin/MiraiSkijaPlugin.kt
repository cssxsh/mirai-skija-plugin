package xyz.cssxsh.mirai.plugin

import io.github.humbleui.skija.*
import io.github.humbleui.skija.impl.*
import kotlinx.coroutines.*
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.contact.Group
import net.mamoe.mirai.event.*
import net.mamoe.mirai.message.data.At
import net.mamoe.mirai.message.data.PlainText
import net.mamoe.mirai.message.data.findIsInstance
import net.mamoe.mirai.message.data.firstIsInstance
import net.mamoe.mirai.utils.*
import xyz.cssxsh.mirai.*
import xyz.cssxsh.skija.*

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
     * 字体 文件夹
     */
    private val fonts get() = dataFolder.resolve("fonts")

    @JvmSynthetic
    public suspend fun loadFace(): Unit = withContext(Dispatchers.IO) {
        val sprite = download(urlString = "https://benisland.neocities.org/petpet/img/sprite.png", dataFolder)
        System.setProperty(PET_PET_SPRITE, sprite.absolutePath)
        val background = download(
            urlString = "https://mirai.mamoe.net/assets/uploads/files/1644930509601-background.png",
            dataFolder
        )
        System.setProperty(SHOUT_BACKGROUND, background.absolutePath)
    }

    override fun onEnable() {
        launch {
            logger.info { "platform: ${Platform.CURRENT}" }
            loadFace()
            MiraiTypefaceFontProvider.INSTANCE.loadTypeface(
                folder = fonts,
                "https://mirrors.tuna.tsinghua.edu.cn/github-release/be5invis/Sarasa-Gothic/Sarasa%20Gothic%20version%200.35.8/sarasa-gothic-ttc-0.35.8.7z",
                "http://dl.font.im/Noto_Sans.zip",
                "http://dl.font.im/Noto_Serif.zip"
            )
            logger.info { "fonts: ${MiraiTypefaceFontProvider.INSTANCE.makeFamilies().keys}" }
        }

        // Test
        globalEventChannel().subscribeMessages {
            """^#ph\s+(\S+)\s+(\S+)""".toRegex() findingReply { result ->
                logger.info { "ph ${result.value}" }
                val (porn, hub) = result.destructured

                subject.uploadImage(resource = pornhub(porn, hub).makeSnapshotResource())
            }
            """^#pet( \d+(?:\.\d+)?)?""".toRegex() findingReply { result ->
                logger.info { "pet ${result.value}" }
                val delay = result.groups[1]?.value?.toDoubleOrNull() ?: 0.02
                val user = message.findIsInstance<At>()?.target?.let { (subject as? Group)?.get(it) } ?: sender
                val file = dataFolder.resolve("${user.id}.jpg")
                if (file.exists().not()) download(urlString = user.avatarUrl, folder = dataFolder).renameTo(file)
                val face = Image.makeFromEncoded(file.readBytes())

                subject.uploadImage(resource = SkijaExternalResource(origin = petpet(face, delay), formatName = "gif"))
            }
            """^#shout(.+)""".toRegex() findingReply { result ->
                logger.info { "shout ${result.value}" }
                val lines = message.firstIsInstance<PlainText>().content
                    .removePrefix("#shout")
                    .split(' ').filterNot { it.isBlank() }
                    .toTypedArray()
                subject.uploadImage(resource = shout(lines = lines).makeSnapshotResource())
            }
            """^#choyen\s+(\S+)\s+(\S+)""".toRegex() findingReply { result ->
                logger.info { "choyen ${result.value}" }
                val (top, bottom) = result.destructured

                subject.uploadImage(resource = choyen(top, bottom).makeSnapshotResource())
            }
        }
    }
}