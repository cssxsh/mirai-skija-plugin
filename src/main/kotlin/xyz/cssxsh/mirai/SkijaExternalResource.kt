package xyz.cssxsh.mirai

import kotlinx.coroutines.*
import net.mamoe.mirai.utils.*
import org.jetbrains.skija.*
import java.io.*

public class SkijaExternalResource(override val origin: Data, override val formatName: String) : ExternalResource {
    public constructor(surface: Surface, format: EncodedImageFormat) : this(
        origin = requireNotNull(surface.makeImageSnapshot().encodeToData(format)) { "encode $format null." },
        formatName = format.name
    )

    override val closed: CompletableDeferred<Unit> = CompletableDeferred()
    override val md5: ByteArray by lazy { origin.bytes.md5() }
    override val sha1: ByteArray by lazy { origin.bytes.sha1() }
    override val size: Long get() = origin.size

    override fun close() {
        try {
            closed.complete(Unit)
        } catch (_: Throwable) {
            //
        }
    }

    override fun inputStream(): InputStream = origin.bytes.inputStream()
}
