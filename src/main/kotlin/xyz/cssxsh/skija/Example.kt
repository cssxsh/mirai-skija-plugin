package xyz.cssxsh.skija

import io.github.humbleui.skija.*
import io.github.humbleui.types.*

/**
 * 构造 PornPub 标志
 */
public fun pornhub(pre: String = "Porn", suf: String = "Hub"): Surface {
    val font = Font(FontStyles.Arial.matchStyle(FontStyle.BOLD)!!, 90F)
    val prefix = TextLine.make(pre, font)
    val suffix = TextLine.make(suf, font)
    val black = Paint().apply { color = 0xFF000000.toInt() }
    val white = Paint().apply { color = 0xFFFFFFFF.toInt() }
    val yellow = Paint().apply { color = 0xFFFF9000.toInt() }

    val surface = Surface.makeRasterN32Premul((prefix.width + suffix.width + 50).toInt(), (suffix.height + 40).toInt())
    // surface.canvas.clear(black.color)
    surface.canvas.drawTextLine(prefix, 10F, 20 - font.metrics.ascent, white)
    surface.canvas.drawRRect(RRect.makeXYWH(prefix.width + 15, 15F, suffix.width + 20, suffix.height + 10, 10F), yellow)
    surface.canvas.drawTextLine(suffix, prefix.width + 25, 20 - font.metrics.ascent, black)

    return surface
}