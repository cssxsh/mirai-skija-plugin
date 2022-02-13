package xyz.cssxsh.skija.gif

import java.nio.*

internal object ApplicationExtension {
    private const val INTRODUCER = 0x0021
    private const val LABEL = 0x00FF
    private const val BLOCK_SIZE = 0x000B
    private const val TERMINATOR = 0x0000

    private fun block(
        buffer: ByteBuffer,
        identifier: ByteArray,
        authentication: ByteArray,
        data: ByteArray,
    ) {
        buffer.put(INTRODUCER.asUnsignedByte())
        buffer.put(LABEL.asUnsignedByte())
        buffer.put(BLOCK_SIZE.asUnsignedByte())
        buffer.put(identifier) // 8 byte
        buffer.put(authentication) // 3 byte
        buffer.put(data)
        buffer.put(TERMINATOR.asUnsignedByte())
    }

    fun write(buffer: ByteBuffer, identifier: String, authentication: String, data: ByteArray) {
        block(
            buffer = buffer,
            identifier = identifier.toByteArray(Charsets.US_ASCII),
            authentication = authentication.toByteArray(Charsets.US_ASCII),
            data = data
        )
    }

    fun loop(buffer: ByteBuffer, count: Int) {
        write(
            buffer = buffer,
            identifier = "NETSCAPE",
            authentication = "2.0",
            data = byteArrayOf(0x03, 0x01)
        )

        buffer.putShort(count.asUnsignedShort())
    }
}