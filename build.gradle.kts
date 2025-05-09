plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "9.0.0-beta10"
}

group = "com.exorastudios"
version = "1.0.0"
java.sourceCompatibility = JavaVersion.VERSION_21

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://oss.sonatype.org/content/groups/public/")
    maven("https://repo.codemc.io/repository/maven-releases/")
    maven("https://repo.codemc.io/repository/maven-snapshots/")
    flatDir {
        dirs("libs")
    }
}

dependencies {
    implementation(files("libs/fastinv.jar"))

    compileOnly("io.papermc.paper:paper-api:1.21.1-R0.1-SNAPSHOT")

    compileOnly("org.projectlombok:lombok:1.18.38")
    annotationProcessor("org.projectlombok:lombok:1.18.38")
}

tasks {
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
    }

    processResources {
        val props = mapOf("version" to version)
        inputs.properties(props)
        filteringCharset = "UTF-8"
        filesMatching("plugin.yml") {
            expand(props)
        }
    }

    runServer {
        minecraftVersion("1.21")
        runDirectory = rootDir.resolve("run/")

        jvmArgs(listOf(
            "-Dcom.mojang.eula.agree=true"
        ))
    }

    shadowJar {
        archiveClassifier.set("")
        relocate("fr.mrmicky.fastinv", "com.exorastudios.library.menu")
    }

    build {
        dependsOn(shadowJar)
    }
}