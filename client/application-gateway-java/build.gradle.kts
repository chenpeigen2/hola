plugins {
    id("java")
}

group = "cn.ruc.edu.hola.client"
version = "1.0-SNAPSHOT"

repositories {
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/spring/")
    mavenLocal()
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    // https://mvnrepository.com/artifact/com.google.code.gson/gson
    implementation("com.google.code.gson:gson:2.10.1")
// https://mvnrepository.com/artifact/io.grpc/grpc-api
    compileOnly("io.grpc:grpc-api:1.56.0")
    // https://mvnrepository.com/artifact/io.grpc/grpc-netty-shaded
    runtimeOnly("io.grpc:grpc-netty-shaded:1.56.0")
// https://mvnrepository.com/artifact/org.hyperledger.fabric/fabric-gateway
    implementation("org.hyperledger.fabric:fabric-gateway:1.3.0")
    implementation(project(":chaincode-java"))
}

tasks.withType<JavaCompile> {
    options.release.set(17)
    options.isIncremental = true
    options.isFork = true
    options.isFailOnError = true
}

tasks.test {
    useJUnitPlatform()
}