import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("java")
    kotlin("jvm") version "1.6.21"
}

group = "cn.edu.ruc"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test"))
    testImplementation("org.junit.jupiter:junit-jupiter-params:5.8.2")
    implementation("com.squareup.okhttp3:okhttp:4.9.3")
    implementation("com.google.code.gson:gson:2.9.0")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.24")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("net.bytebuddy:byte-buddy:1.12.10")
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
    kotlinOptions.jvmTarget = "11"
}