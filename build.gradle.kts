plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"

    id("net.mamoe.mirai-console") version "2.10.0-RC2"
    id("net.mamoe.maven-central-publish") version "0.7.0"
}

group = "xyz.cssxsh.mirai"
version = "1.0.0-RC2"

repositories {
    mavenLocal()
    mavenCentral()
}

mavenCentralPublish {
    useCentralS01()
    singleDevGithubProject("cssxsh", "mirai-skija-plugin")
    licenseFromGitHubProject("AGPL-3.0", "master")
    publication {
        artifact(tasks.getByName("buildPlugin"))
    }
}

dependencies {
    api("io.github.humbleui:skija-macos-x64:0.98.1")
    api("io.github.humbleui:skija-macos-arm64:0.98.1")
    api("io.github.humbleui:skija-linux:0.98.1")
    api("io.github.humbleui:skija-windows:0.98.1")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("net.mamoe:mirai-core-utils:2.10.0-RC2")
    //
    testImplementation(kotlin("test", "1.6.0"))
}

kotlin {
    explicitApi()
}

mirai {
    configureShadow {
        exclude("module-info.class")
    }
}

tasks {
    test {
        useJUnitPlatform()
    }
}
