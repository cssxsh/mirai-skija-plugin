package xyz.cssxsh.mirai.plugin

import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.engine.okhttp.*
import io.ktor.client.features.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.*
import net.mamoe.mirai.utils.*
import java.io.File

internal val logger get() = MiraiSkijaPlugin.logger

private val http = HttpClient(OkHttp) {
    CurlUserAgent()
    install(HttpTimeout) {
        connectTimeoutMillis = 30_000
        socketTimeoutMillis = 30_000
    }
}

internal suspend fun download(urlString: String, folder: File): File = supervisorScope {
    http.get<HttpStatement>(urlString).execute { response ->
        val relative = response.headers[HttpHeaders.ContentDisposition]
            ?.let { ContentDisposition.parse(it).parameter(ContentDisposition.Parameters.FileName) }
            ?: response.request.url.encodedPath.substringAfterLast('/').decodeURLPart()

        val file = folder.resolve(relative)

        if (file.exists()) {
            logger.info { "文件 ${file.name} 已存在，跳过下载" }
            response.call.cancel("文件 ${file.name} 已存在，跳过下载")
        } else {
            logger.info { "文件 ${file.name} 开始下载" }
            file.outputStream().use { output ->
                val channel: ByteReadChannel = response.receive()

                while (!channel.isClosedForRead) channel.copyTo(output)
            }
        }

        file
    }
}

internal fun sevenZA(folder: File): File {
    val os = System.getProperty("os.name").lowercase()
    val arch = System.getProperty("os.arch")
    val relative = when {
        os.contains(other = "linux") && arch.contains(other = "aarch64") -> "7zz-linux-arm64"
        os.contains(other = "linux") && arch.contains(other = "aarch") -> "7zz-linux-arm"
        os.contains(other = "linux") && arch.contains(other = "64") -> "7zz-linux-x64"
        os.contains(other = "linux") && arch.contains(other = "86") -> "7zz-linux-x86"
        os.contains(other = "windows") && arch.contains(other = "64")-> "7za-x64.exe"
        os.contains(other = "windows") && arch.contains(other = "86")-> "7za-x86.exe"
        os.contains(other = "mac") -> "7zz-mac"
        os.contains(other = "darwin") -> "7zz-mac"
        else -> throw RuntimeException("Unsupported platform: $os $arch")
    }
    val binary = folder.resolve(relative).apply {
        if (exists().not()) {
            outputStream().use { output ->
                MiraiSkijaPlugin.getResourceAsStream("xyz/cssxsh/mirai/plugin/$relative")!!
                    .use { input -> input.transferTo(output) }
            }
        }
    }

    return binary
}