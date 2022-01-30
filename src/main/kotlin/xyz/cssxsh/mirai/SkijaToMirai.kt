package xyz.cssxsh.mirai

import net.mamoe.mirai.utils.*
import org.jetbrains.skija.*

/**
 * 从 [Surface] 获取图片快照资源
 * @see Surface.makeImageSnapshot
 * @see Image.encodeToData
 * @see ExternalResource
 */
public fun Surface.makeImageResource(format: EncodedImageFormat = EncodedImageFormat.PNG): SkijaExternalResource {
    return SkijaExternalResource(surface = this, format = format)
}