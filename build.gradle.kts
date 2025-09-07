plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
}

group = "io.github.brothanb"
version = "1.1"

var mcversion = "1.21.8"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.8-R0.1-SNAPSHOT")
}

tasks {
    processResources {
        val props = mapOf(
            "version" to version,
            "mcversion" to mcversion,
        )
        inputs.properties(props)
        //filteringCharset("UTF-8")
        filesMatching("paper-plugin.yml") {
            expand(props)
        }
    }

    jar {
        manifest {
            attributes["paperweight-mappings-namespace"] = "mojang"
        }
    }

    runServer {
        // Configure the Minecraft version for our task.
        // This is the only required configuration besides applying the plugin.
        // Your plugin's jar (or shadowJar if present) will be used automatically.
        minecraftVersion(mcversion)
    }
}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
