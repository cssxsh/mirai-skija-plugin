package xyz.cssxsh.skija.gif

import io.github.humbleui.skija.*
import io.github.humbleui.types.*
import java.nio.*

public class GIFBuilder(public val width: Int, public val height: Int) {
    public companion object {
        internal const val GIF_HEADER = "GIF89a"
        internal const val GIF_TRAILER = ";"
    }

    internal fun header(buffer: ByteBuffer) = buffer.put(GIF_HEADER.toByteArray(Charsets.US_ASCII))

    internal fun trailer(buffer: ByteBuffer) = buffer.put(GIF_TRAILER.toByteArray(Charsets.US_ASCII))

    private var capacity = 1 shl 23

    public fun capacity(total: Int): GIFBuilder = apply { capacity = total }

    private var loop = 0

    public fun loop(count: Int): GIFBuilder = apply { loop = count }

    public var ratio: Int = 0

    public fun ratio(size: Int): GIFBuilder = apply { ratio = size }

    public var options: FrameOptions = FrameOptions(
        method = DisposalMethod.UNSPECIFIED,
        input = false,
        transparency = false,
        delay = 1.0,
        table = ColorTable.Empty,
        rect = IRect(0, 0, 0, 0)
    )

    public fun table(bitmap: Bitmap): GIFBuilder = apply {
        options.table = ColorTable(OctTreeQuantizer.quantize(bitmap, 256))
    }

    public fun table(value: ColorTable): GIFBuilder = apply {
        options.table = value
    }

    public fun options(block: FrameOptions.() -> Unit): GIFBuilder = apply { options.apply(block) }

    public var frames: MutableList<Pair<Bitmap, FrameOptions>> = ArrayList()

    public fun frame(bitmap: Bitmap, block: FrameOptions.() -> Unit = {}): GIFBuilder = apply {
        val rect = IRect(0, 0, bitmap.width, bitmap.height)
        frames.add(bitmap to options.copy(table = this.options.table, rect = rect).apply(block))
    }

    public fun build(buffer: ByteBuffer) {
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        header(buffer)
        LogicalScreenDescriptor.write(buffer, width, height, options.table, ratio)
        ApplicationExtension.loop(buffer, loop)
        for ((bitmap, options) in frames) {
            val table = when {
                options.table.exists() -> options.table
                this.options.table.exists() -> this.options.table
                else -> ColorTable(OctTreeQuantizer.quantize(bitmap, 256))
            }
            val index = if (options.transparency) table.background else -1

            GraphicControlExtension.write(buffer, options.method, options.input, index, options.delay)

            val result = AtkinsonDitherer.dither(bitmap, table.colors)

            ImageDescriptor.write(buffer, options.rect, table, table !== this.options.table, result)
        }
        trailer(buffer)
    }

    public fun data(): Data {
        val data = Data.makeFromBytes(ByteArray(capacity))
        val buffer = data.toByteBuffer()
        build(buffer = buffer)
        return data.makeSubset(0, buffer.remaining().toLong())
    }

    public fun build(): ByteArray {
        val buffer = ByteBuffer.allocate(capacity)
        build(buffer = buffer)

        return ByteArray(buffer.remaining()).also { buffer.get(it) }
    }

    public data class FrameOptions(
        var method: DisposalMethod,
        var input: Boolean,
        var transparency: Boolean,
        var delay: Double,
        var table: ColorTable,
        var rect: IRect
    )
}