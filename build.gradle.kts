plugins {
    kotlin("jvm") version "1.6.0"
    kotlin("plugin.serialization") version "1.6.0"

    id("net.mamoe.mirai-console") version "2.10.0"
    id("net.mamoe.maven-central-publish") version "0.7.1"
}

group = "xyz.cssxsh.mirai"
version = "1.0.0"

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
    api("io.github.humbleui:skija-macos-x64:0.100.0")
    api("io.github.humbleui:skija-macos-arm64:0.100.0")
    api("io.github.humbleui:skija-linux:0.100.0")
    api("io.github.humbleui:skija-windows:0.100.0")
    compileOnly("org.projectlombok:lombok:1.18.22")
    compileOnly("net.mamoe:mirai-core-utils:2.10.0")
    //
    testImplementation(kotlin("test", "1.6.0"))
}

kotlin {
    explicitApi()
    target.compilations {
        all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
    }
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
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
