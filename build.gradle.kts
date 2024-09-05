plugins {
    id("java")
}

group = "org.example"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    compileOnly("org.projectlombok:lombok:1.18.24")
    annotationProcessor("org.projectlombok:lombok:1.18.28")
    implementation("com.fasterxml.jackson.core:jackson-databind:2.16.1")
    implementation("ch.qos.logback:logback-classic:1.3.11")
    implementation("javax.xml.bind:jaxb-api:2.3.1")
    implementation("com.sun.xml.bind:jaxb-core:2.3.0")
    implementation("com.sun.xml.bind:jaxb-impl:2.3.1")
}

tasks.test {
    useJUnitPlatform()
}