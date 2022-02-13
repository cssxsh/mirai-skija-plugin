package xyz.cssxsh.skija

import io.github.humbleui.skija.*
import io.github.humbleui.types.*
import xyz.cssxsh.skija.gif.*
import java.io.*

internal const val PET_PET_SPRITE = "xyz.cssxsh.skija.petpet"

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
            "please download https://benisland.neocities.org/petpet/img/sprite.png, file path set property $PET_PET_SPRITE",
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

    return gif(112, 112) {
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