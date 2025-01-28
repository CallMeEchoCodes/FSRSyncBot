plugins {
    application
}

group = "dev.callmeecho"
version = "1.0.0-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
    targetCompatibility = JavaVersion.VERSION_21
}

repositories {

    mavenCentral()
    maven("https://maven.lavalink.dev/releases")
    maven("https://libraries.minecraft.net")
    maven("https://maven.lukebemish.dev/releases")
	maven("https://maven.spiritstudios.dev/snapshots")
}

dependencies {
    compileOnly(libs.annotations)

    implementation(libs.javacord)

    implementation(libs.log4j.core)
    implementation(libs.log4j.slf4j)

    implementation(libs.datafixerupper)
    implementation(libs.fastutil)
}

application {
    mainClass.set("dev.callmeecho.fsrsbot.FSRSBot")
}

tasks.withType<Jar>() {
    manifest.attributes["Main-Class"] = "dev.callmeecho.fsrsbot.FSRSBot"
    val dependencies = configurations
        .runtimeClasspath
        .get()
        .map(::zipTree)
    from(dependencies)
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}

