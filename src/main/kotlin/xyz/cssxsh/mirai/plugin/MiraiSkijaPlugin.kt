package xyz.cssxsh.mirai.plugin

import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.utils.*
import org.jetbrains.skija.impl.*

public object MiraiSkijaPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-skija-plugin",
        name = "mirai-skija-plugin",
        version = "1.0.0-RC1",
    ) {
        author("cssxsh")
    }
) {
    override fun onEnable() {
        logger.info { "platform: ${Platform.CURRENT}" }
    }
}