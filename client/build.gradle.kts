plugins {
    kotlin("jvm") version "1.9.21"
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
    implementation("com.google.guava:guava:32.1.3-jre")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    implementation("com.google.code.gson:gson:2.10.1")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.10.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.10.1")
    implementation("org.bouncycastle:bcprov-jdk15on:1.70")
    // https://mvnrepository.com/artifact/cn.hutool/hutool-crypto
    implementation("cn.hutool:hutool-crypto:5.8.23")
    implementation("io.vertx:vertx-web:4.5.1")
    implementation("io.vertx:vertx-web-validation:4.5.1")
    // https://mvnrepository.com/artifact/io.vertx/vertx-auth-properties
    implementation("io.vertx:vertx-auth-properties:4.5.1")
    // https://mvnrepository.com/artifact/io.vertx/vertx-auth-jwt
    implementation("io.vertx:vertx-auth-jwt:4.5.1")
    // https://mvnrepository.com/artifact/io.vertx/vertx-config
    implementation("io.vertx:vertx-config:4.5.1")
    // https://mvnrepository.com/artifact/io.vertx/vertx-config-yaml
    implementation("io.vertx:vertx-config-yaml:4.5.1")
    // https://mvnrepository.com/artifact/io.vertx/vertx-service-discovery
    implementation("io.vertx:vertx-service-discovery:4.5.1")
    // https://mvnrepository.com/artifact/io.vertx/vertx-json-schema
    implementation("io.vertx:vertx-json-schema:4.5.1")
    implementation(project("application-gateway-java"))
// https://mvnrepository.com/artifact/io.grpc/grpc-api
    compileOnly("io.grpc:grpc-api:1.59.0")
    // https://mvnrepository.com/artifact/io.grpc/grpc-netty-shaded
    runtimeOnly("io.grpc:grpc-netty-shaded:1.59.0")
// https://mvnrepository.com/artifact/org.hyperledger.fabric/fabric-gateway
    implementation("org.hyperledger.fabric:fabric-gateway:1.4.0")
    implementation(project(":chaincode-java"))
    // https://mvnrepository.com/artifact/org.apache.velocity/velocity-engine-core
    implementation("org.apache.velocity:velocity-engine-core:2.3")
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