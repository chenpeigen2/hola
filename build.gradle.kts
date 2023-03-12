import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.8.10"
    id("de.qualersoft.jmeter") version "2.4.0"
    application
    id("org.springframework.boot") version "3.0.4"
    id("io.spring.dependency-management") version "1.1.0"
    kotlin("plugin.spring") version "1.8.10"
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
    testImplementation("org.junit.jupiter:junit-jupiter-api:5.9.2")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:5.9.2")
    implementation("com.squareup.okhttp3:okhttp:4.10.0")
    implementation("org.apache.httpcomponents.client5:httpclient5:5.2.1")
    implementation("com.google.code.gson:gson:2.10.1")
    implementation("commons-codec:commons-codec:1.15")
    implementation("com.google.guava:guava:31.1-jre")
    implementation("net.bytebuddy:byte-buddy:1.12.20")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
    implementation("org.apache.commons:commons-math3:3.6.1")
    implementation("io.vertx:vertx-web:4.4.0")
    implementation(project("sample")) // add sub-sample-project
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