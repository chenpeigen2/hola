plugins {
    kotlin("jvm") version "1.8.0"
}

group = "cn.ruc.edu.hola.backbend"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.10.0")
    implementation("com.google.code.gson:gson:2.10")
    implementation("io.vertx:vertx-web:4.3.6")
    implementation("com.google.guava:guava:31.1-jre")
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.8.1")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.8.1")
}

tasks.getByName<Test>("test") {
    useJUnitPlatform()
}