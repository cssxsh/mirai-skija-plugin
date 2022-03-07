package xyz.cssxsh.mirai.plugin

import io.github.humbleui.skija.impl.Platform as SkijaPlatform
import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.*
import xyz.cssxsh.skija.*

public object MiraiSkijaPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-skija-plugin",
        name = "mirai-skija-plugin",
        version = "1.0.0",
    ) {
        author("cssxsh")
    }
) {

    /**
     * 字体 文件夹
     */
    private val fonts get() = dataFolder.resolve("fonts")

    override fun onEnable() {
        logger.info { "platform: ${SkijaPlatform.CURRENT}" }
        MiraiTypefaceFontProvider.loadTypeface(folder = fonts)
        logger.info { "fonts: ${MiraiTypefaceFontProvider.makeFamilies().keys}" }
    }
}