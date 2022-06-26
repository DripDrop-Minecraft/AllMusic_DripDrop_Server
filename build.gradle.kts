import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    java
    id("maven-publish")
    id("com.github.johnrengelman.shadow") version "6.1.0"
}

version = "2.16.1"

repositories {
    mavenCentral()
    maven("https://hub.spigotmc.org/nexus/content/groups/public")
    maven("https://repo.extendedclip.com/content/repositories/placeholderapi/")
    maven("https://jitpack.io")
    maven("https://nexus.velocitypowered.com/repository/maven-public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.16.5-R0.1-SNAPSHOT")
    compileOnly("net.md-5:bungeecord-api:1.16-R0.4-SNAPSHOT")
    compileOnly("com.github.MilkBowl:VaultAPI:1.7")
    compileOnly("com.velocitypowered:velocity-api:3.0.0")
    compileOnly("me.clip:placeholderapi:2.10.10")

    implementation("org.xerial:sqlite-jdbc:3.36.0.3")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.9.0")

    annotationProcessor("com.velocitypowered:velocity-api:3.0.0")
}

tasks.withType<ShadowJar> {
    relocate("okhttp3", "coloryr.allmusic.lib.okhttp3")
    relocate("okio", "coloryr.allmusic.lib.okio")
    relocate("kotlin", "coloryr.allmusic.lib.kotlin")
    relocate("org.intellij", "coloryr.allmusic.lib.org.intellij")
    relocate("org.jetbrains", "coloryr.allmusic.lib.org.jetbrains")
    relocate("com.google.gson", "coloryr.allmusic.lib.com.google.gson")
}

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}