plugins {
    kotlin("jvm") version "1.9.21"
    application
}

group = "cn.ruc.edu.hola.index"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/spring/")
    mavenLocal()
    mavenCentral()
}

application {
    mainClass.set("cn.ruc.edu.hola.index.Entry")
}

dependencies {
    // https://mvnrepository.com/artifact/com.squareup.okhttp3/okhttp
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
// https://mvnrepository.com/artifact/com.squareup.okhttp3/logging-interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:4.12.0")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("io.vertx:vertx-web:4.5.0")
    implementation("io.vertx:vertx-web-validation:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-service-discovery
    implementation("io.vertx:vertx-service-discovery:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-auth-properties
    implementation("io.vertx:vertx-auth-properties:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-auth-jwt
    implementation("io.vertx:vertx-auth-jwt:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-config
    implementation("io.vertx:vertx-config:4.5.0")
    // https://mvnrepository.com/artifact/io.vertx/vertx-config-yaml
    implementation("io.vertx:vertx-config-yaml:4.5.0")
    // https://mvnrepository.com/artifact/io.grpc/grpc-api
    compileOnly("io.grpc:grpc-api:1.59.0")
    // https://mvnrepository.com/artifact/io.grpc/grpc-netty-shaded
    runtimeOnly("io.grpc:grpc-netty-shaded:1.59.0")
// https://mvnrepository.com/artifact/org.hyperledger.fabric/fabric-gateway
    implementation("org.hyperledger.fabric:fabric-gateway:1.4.0")
    testImplementation(platform("org.junit:junit-bom:5.10.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
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

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    kotlinOptions.jvmTarget = "17"
}