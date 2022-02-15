package xyz.cssxsh.skija

import io.github.humbleui.skija.*
import io.github.humbleui.types.*
import xyz.cssxsh.skija.gif.*
import java.io.*

internal const val PET_PET_SPRITE = "xyz.cssxsh.skija.petpet"

internal const val SHOUT_BACKGROUND = "xyz.cssxsh.skija.shout"

/**
 * 构造 PornPub Logo
 */
public fun pornhub(porn: String = "Porn", hub: String = "Hub"): Surface {
    val font = Font(FontStyles.Arial.matchStyle(FontStyle.BOLD)!!, 90F)
    val prefix = TextLine.make(porn, font)
    val suffix = TextLine.make(hub, font)
    val black = Paint().setColor(0xFF000000.toInt())
    val white = Paint().setColor(0xFFFFFFFF.toInt())
    val yellow = Paint().setColor(0xFFFF9000.toInt())

    val surface = Surface.makeRasterN32Premul((prefix.width + suffix.width + 50).toInt(), (suffix.height + 40).toInt())
    surface.canvas.clear(black.color)
    surface.canvas.drawTextLine(prefix, 10F, 20 - font.metrics.ascent, white)
    surface.canvas.drawRRect(RRect.makeXYWH(prefix.width + 15, 15F, suffix.width + 20, suffix.height + 10, 10F), yellow)
    surface.canvas.drawTextLine(suffix, prefix.width + 25, 20 - font.metrics.ascent, black)

    return surface
}

/**
 * 构造 PetPet Face
 *
 * [PetPet Sprite Image Download](https://benisland.neocities.org/petpet/img/sprite.png)
 * @see PET_PET_SPRITE
 */
public fun petpet(face: Image, second: Double = 0.02): Data {
    val sprite = try {
        Image.makeFromEncoded(File(System.getProperty(PET_PET_SPRITE, "sprite.png")).readBytes())
    } catch (cause: Throwable) {
        throw IllegalStateException(
            "please download https://benisland.neocities.org/petpet/img/sprite.png , file path set property $PET_PET_SPRITE",
            cause
        )
    }
    val surface = Surface.makeRasterN32Premul(112 * 5, 112)
    val rects = listOf(
        // 0, 0, 0, 0
        Rect.makeXYWH(21F, 21F, 91F, 91F),
        // -4, 12, 4, -12
        Rect.makeXYWH(112F + 21F - 4F, 21F + 12F, 91F + 4F, 91F - 12F),
        // -12, 18, 12, -18
        Rect.makeXYWH(224F + 21F - 12F, 21F + 18F, 91F + 12F, 91F - 18F),
        // -8, 12, 4, -12
        Rect.makeXYWH(336F + 21F - 8F, 21F + 12F, 91F + 4F, 91F - 12F),
        // -4, 0, 0, 0
        Rect.makeXYWH(448F + 21F - 4F, 21F, 91F, 91F)
    )

    for (rect in rects) surface.canvas.drawImageRect(face, rect)

    surface.canvas.drawImage(sprite, 0F, 0F)

    val images = (0 until 5).map { index ->
        val rect = IRect(112 * index, 0, 112 * index + 112, 112)
        requireNotNull(surface.makeImageSnapshot(rect)) { "Make image snapshot fail" }
    }

    return gif(width = 112, height = 112) {
        table(bitmap = Bitmap.makeFromImage(surface.makeImageSnapshot()))
        loop(count = 0)
        options {
            delay = second
            method = DisposalMethod.RESTORE_TO_BACKGROUND
            transparency = true
        }
        for (image in images) {
            frame(bitmap = Bitmap.makeFromImage(image))
        }
    }
}

/**
 * Shout Face
 * @see SHOUT_BACKGROUND
 */
public fun shout(face: Image, vararg lines: String): Surface {
    val background = try {
        Image.makeFromEncoded(File(System.getProperty(SHOUT_BACKGROUND, "shit.png")).readBytes())
    } catch (cause: Throwable) {
        throw IllegalStateException(
            "please download https://mirai.mamoe.net/assets/uploads/files/1644858542844-background.png , file path set property $SHOUT_BACKGROUND",
            cause
        )
    }
    val surface = Surface.makeRasterN32Premul(535, 500)
    val rect = Rect.makeXYWH(50F,70F,200F,200F)
    val black = Paint().setColor(0xFF000000.toInt())

    surface.canvas.drawImageRect(face, rect)
    surface.canvas.drawImage(background, 0F, 0F)

    when (lines.size) {
        1 -> {
            val size = 50F
            val font = Font(FontStyles.SimHei.matchStyle(FontStyle.BOLD)!!, size)
            val (line) = lines
            for((index, word) in line.withIndex()) {
                surface.canvas.drawString(word.toString(), 430F, (index + 1) * size, font, black)
            }
        }
        2 -> {
            val size = 40F
            val font = Font(FontStyles.SimHei.matchStyle(FontStyle.BOLD)!!, size)
            val (first, second) = lines
            for((index, word) in first.withIndex()) {
                surface.canvas.drawString(word.toString(), 400F, (index + 1) * size, font, black)
            }
            for((index, word) in second.withIndex()) {
                surface.canvas.drawString(word.toString(), 450F, (index + 1) * size, font, black)
            }
        }
    }

    return surface
}