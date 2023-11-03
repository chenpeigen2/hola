import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.9.20"
    id("de.qualersoft.jmeter") version "2.4.0"
    application
    id("org.springframework.boot") version "3.1.2"
    id("io.spring.dependency-management") version "1.1.2"
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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.3")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.3")
    implementation("com.squareup.okhttp3:okhttp:4.11.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-codec:commons-codec:1.16.0")
    implementation("com.google.guava:guava:32.1.3-jre")
    implementation("net.bytebuddy:byte-buddy:1.14.5")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("io.vertx:vertx-web:4.4.6")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
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
    kotlinOptions.freeCompilerArgs = listOf("-Xjsr305=strict")
    kotlinOptions.jvmTarget = "17"
}

tasks.withType<Test> {
    useJUnitPlatform()
}