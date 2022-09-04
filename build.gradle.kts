import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
    id("de.qualersoft.jmeter") version "2.3.0"
    application
}

group = "cn.edu.ruc"
version = "1.0-SNAPSHOT"


application {
    mainClass.set("cn.edu.ruc.hola.web.Entry")
}

tasks.jar {
    archiveFileName.set("core.jar")
    manifest.attributes["Main-Class"] = "cn.edu.ruc.hola.web.Entry"
}


repositories {
    mavenCentral()
}

dependencies {
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.0")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("net.bytebuddy:byte-buddy:1.12.14")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("io.vertx:vertx-web:4.3.3")
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.release.set(17)
    options.isIncremental = true
    options.isFork = true
    options.isFailOnError = true
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "17"
}