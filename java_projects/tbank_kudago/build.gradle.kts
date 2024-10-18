plugins {
    java
    id("org.springframework.boot") version "3.3.4"
    id("io.spring.dependency-management") version "1.1.6"
    jacoco
}

group = "tbank.mr_irmag"
version = "0.0.1-SNAPSHOT"

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(17)
    }
}

configurations {
    compileOnly {
        extendsFrom(configurations.annotationProcessor.get())
    }
}

repositories {
    mavenCentral()
}

dependencies {
    testImplementation("junit:junit:4.12")
    testImplementation("org.mockito:mockito-core:4.11.0")
    testImplementation("org.testcontainers:junit-jupiter:1.18.3")
    testImplementation("com.github.tomakehurst:wiremock:3.0.1")
    testImplementation("io.projectreactor:reactor-test")
    compileOnly("org.projectlombok:lombok")
    developmentOnly("org.springframework.boot:spring-boot-devtools")
    annotationProcessor("org.projectlombok:lombok")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    implementation("org.slf4j:slf4j-api:2.0.9")
    implementation("com.fasterxml.jackson.core:jackson-core:2.16.1")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
    implementation("org.aspectj:aspectjweaver:1.9.19")
    implementation("org.aspectj:aspectjrt:1.9.19")
    implementation("org.springframework.boot:spring-boot-starter-webflux")
}

tasks.withType<Test> {
    useJUnitPlatform()
}

tasks.test {
    finalizedBy(tasks.jacocoTestReport)
}

tasks.jacocoTestReport {
    dependsOn(tasks.test)
}

jacoco {
    toolVersion = "0.8.12"
    reportsDirectory = layout.buildDirectory.dir("reports/jacoco")
}

tasks.jacocoTestReport {
    reports {
        xml.required = false
        csv.required = false
        html.outputLocation = layout.buildDirectory.dir("jacocoHtml")
    }

    classDirectories.setFrom(files(classDirectories.files.map {
        fileTree(it).apply {
            exclude("tbank/mr_irmag/tbank_kudago_task/domain/**")
            exclude("tbank/mr_irmag/tbank_kudago_task/config/**")
            exclude("tbank/mr_irmag/tbank_kudago_task/threadFactory/**")
        }
    }))
}