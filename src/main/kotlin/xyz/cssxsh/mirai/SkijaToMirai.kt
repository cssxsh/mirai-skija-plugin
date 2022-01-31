package xyz.cssxsh.mirai

import io.github.humbleui.skija.*
import net.mamoe.mirai.utils.*

/**
 * 从 [Surface] 获取图片快照资源
 * @see Surface.makeImageSnapshot
 * @see Image.encodeToData
 * @see ExternalResource
 */
@JvmOverloads
public fun Surface.makeSnapshotResource(format: EncodedImageFormat = EncodedImageFormat.PNG): SkijaExternalResource {
    return SkijaExternalResource(image = makeImageSnapshot(), format = format)
}