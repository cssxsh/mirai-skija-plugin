package xyz.cssxsh.mirai.plugin

import net.mamoe.mirai.console.plugin.jvm.*
import net.mamoe.mirai.event.events.*
import net.mamoe.mirai.event.*
import net.mamoe.mirai.utils.*
import org.jetbrains.skija.*

public object MiraiSkijaPlugin : KotlinPlugin(
    JvmPluginDescription(
        id = "xyz.cssxsh.mirai.mirai-skija-plugin",
        name = "mirai-skija-plugin",
        version = "1.0.0",
    ) {
        author("cssxsh")
    }
) {
    override fun onEnable() {
        val surface = Surface.makeRasterN32Premul(100, 100)
        val paint = Paint()
        paint.color = Color.makeRGB(0xFF, 0, 0)
        surface.canvas.drawCircle(50F, 50F, 30F, paint)

        globalEventChannel().subscribeAlways<BotOnlineEvent> {
            bot.getFriendOrFail(id = 1438159989)
        }
        logger.info { "Plugin loaded" }
    }
}