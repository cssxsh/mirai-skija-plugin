package xyz.cssxsh.skija.gif

import io.github.humbleui.skija.*
import java.nio.*

public class GIFEncoder(public val buffer: ByteBuffer) {
    public constructor(data: Data) : this(data.toByteBuffer().order(ByteOrder.LITTLE_ENDIAN))

    internal companion object {
        const val GIF_HEADER = "GIF89a"
        const val GIF_TRAILER = 0x003B
    }

    public var table: ColorTable = ColorTable.Empty

    public var method: DisposalMethod = DisposalMethod.UNSPECIFIED

    public var delay: Double = 1.0

    public var transparency: Int? = null

    public fun header(): GIFEncoder = apply { buffer.put(GIF_HEADER.toByteArray(Charsets.US_ASCII)) }

    public fun table(bitmap: Bitmap): GIFEncoder = apply {
        table = ColorTable(OctTreeQuantizer.quantize(bitmap, 256))
    }

    public fun method(value: DisposalMethod): GIFEncoder = apply { method = value }

    public fun delay(second: Double): GIFEncoder = apply { delay = second }

    public fun transparency(index: Int): GIFEncoder = apply { transparency = index }

    public fun screen(width: Int, height: Int): GIFEncoder = apply {
        LogicalScreenDescriptor.write(buffer, width, height, table, 0)
    }

    public fun screen(width: Int, height: Int, table: ColorTable, ratio: Int): GIFEncoder = apply {
        this.table = table
        LogicalScreenDescriptor.write(buffer, width, height, table, ratio)
    }

    public fun loop(count: Int): GIFEncoder = apply {
        ApplicationExtension.loop(buffer, count)
    }

    public fun frame(bitmap: Bitmap): GIFEncoder = apply { frame(bitmap, delay, method, table) }

    public fun frame(bitmap: Bitmap, delay: Double, method: DisposalMethod, table: ColorTable): GIFEncoder = apply {

        GraphicControlExtension.write(buffer, method, false, transparency, delay)

        val result = AtkinsonDitherer.dither(bitmap, table.colors)

        ImageDescriptor.write(
            buffer,
            0,
            0,
            bitmap.width,
            bitmap.height,
            table,
            false,
            result
        )
    }

    public fun trailer(): GIFEncoder = apply { buffer.put(GIF_TRAILER.asUnsignedByte()) }
}