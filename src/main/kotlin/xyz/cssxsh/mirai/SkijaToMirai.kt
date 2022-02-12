package xyz.cssxsh.mirai

import io.github.humbleui.skija.*
import net.mamoe.mirai.utils.*
import java.io.*

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

/**
 * 从 [File] 获取图片快照资源, 用于转换图片格式，例如 WEBP to PNG
 * @see Surface.makeImageSnapshot
 * @see Image.encodeToData
 * @see ExternalResource
 */
@JvmOverloads
public fun File.makeSnapshotResource(format: EncodedImageFormat = EncodedImageFormat.PNG): SkijaExternalResource {
    return SkijaExternalResource(image = Image.makeFromEncoded(readBytes()), format = format)
}