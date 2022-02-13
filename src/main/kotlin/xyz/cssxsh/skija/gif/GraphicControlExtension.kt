package xyz.cssxsh.skija.gif

import java.nio.*

public object GraphicControlExtension {
    private const val INTRODUCER = 0x21
    private const val LABEL = 0xF9
    private const val BLOCK_SIZE = 0x04
    private const val TERMINATOR = 0x00

    private fun block(
        buffer: ByteBuffer,
        flags: Byte,
        delay: Short,
        transparencyIndex: Byte,
    ) {
        buffer.put(INTRODUCER.asUnsignedByte())
        buffer.put(LABEL.asUnsignedByte())
        buffer.put(BLOCK_SIZE.asUnsignedByte())
        buffer.put(flags)
        buffer.putShort(delay)
        buffer.put(transparencyIndex)
        buffer.put(TERMINATOR.asUnsignedByte())
    }

    public fun write(
        buffer: ByteBuffer,
        disposalMethod: DisposalMethod,
        userInput: Boolean,
        transparencyIndex: Int?,
        delay: Double
    ) {
        // Not Interlaced Images
        var flags = 0x0000

        flags = flags or disposalMethod.flag
        if (userInput) flags = flags or 0x0002
        if (transparencyIndex in 0 until 256) flags = flags or 0x0001

        block(
            buffer = buffer,
            flags = flags.asUnsignedByte(),
            delay = (delay * 100).toInt().asUnsignedShort(),
            transparencyIndex = (transparencyIndex ?: 0).asUnsignedByte()
        )
    }
}