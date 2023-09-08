plugins {
    kotlin("jvm") version "1.9.10"
    application
}

group = "cn.ruc.edu.hola.client"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/spring/")
    mavenLocal()
    mavenCentral()
}

application {
    mainClass.set("cn.ruc.edu.hola.client.Entry")
}

dependencies {
    // https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
// https://mvnrepository.com/artifact/org.jetbrains.kotlinx/kotlinx-coroutines-core-jvm
    runtimeOnly("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.7.3")
    implementation("com.google.guava:guava:32.1.2-jre")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.0")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.0")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    // https://mvnrepository.com/artifact/cn.hutool/hutool-crypto
    implementation("cn.hutool:hutool-crypto:5.8.21")
    implementation("io.vertx:vertx-web:4.4.5")
    implementation("io.vertx:vertx-web-validation:4.4.5")
    implementation(project("application-gateway-java"))
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
// https://mvnrepository.com/artifact/io.grpc/grpc-api
    compileOnly("io.grpc:grpc-api:1.57.2")
    // https://mvnrepository.com/artifact/io.grpc/grpc-netty-shaded
    runtimeOnly("io.grpc:grpc-netty-shaded:1.57.2")
// https://mvnrepository.com/artifact/org.hyperledger.fabric/fabric-gateway
    implementation("org.hyperledger.fabric:fabric-gateway:1.3.1")
    implementation(project(":chaincode-java"))
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