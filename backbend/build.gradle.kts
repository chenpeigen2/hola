plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "cn.ruc.edu.hola.backbend"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-service-discovery
    implementation("io.vertx:vertx-service-discovery:4.5.0")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.vertx:vertx-web:4.5.0")
    implementation("io.vertx:vertx-web-validation:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-auth-properties
    implementation("io.vertx:vertx-auth-properties:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-auth-jwt
    implementation("io.vertx:vertx-auth-jwt:4.5.0")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    // https://mvnrepository.com/artifact/cn.hutool/hutool-crypto
    implementation("cn.hutool:hutool-crypto:5.8.23")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
}

application {
    mainClass.set("cn.ruc.edu.hola.backbend.Entry")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}

tasks.withType<JavaCompile> {
    options.release.set(17)
    options.isIncremental = true
    options.isFork = true
    options.isFailOnError = true
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    kotlinOptions.jvmTarget = "17"
}