plugins {
    id("java")
    id("xyz.jpenilla.run-paper") version "2.3.1"
    id("com.gradleup.shadow") version "8.3.9"
}

group = "io.github.brothanb"
version = "1.3-snapshot"

var mcversion = "1.21.10"

repositories {
    mavenCentral()
    maven("https://repo.papermc.io/repository/maven-public/")
    maven("https://repo.bstats.org/content/repositories/releases/" )
}

dependencies {
    compileOnly("io.papermc.paper:paper-api:1.21.10-R0.1-SNAPSHOT")
    implementation("org.bstats:bstats-bukkit:3.1.0")
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

    shadowJar {
        // Relocate the bStats package to avoid conflicts with other plugins
        relocate( "org.bstats", "io.github.brothanb.tphome.bstats")
    }

    build {
        dependsOn(shadowJar)
    }

}

java {
    toolchain.languageVersion.set(JavaLanguageVersion.of(21))
}
