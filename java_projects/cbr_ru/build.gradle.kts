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

repositories {
	mavenCentral()
}


dependencies {
	testImplementation("junit:junit:4.12")
	testImplementation("org.mockito:mockito-core:4.11.0")
	testImplementation("org.testcontainers:junit-jupiter:1.18.3")
	implementation("org.springframework.boot:spring-boot-starter-web")
	compileOnly("org.projectlombok:lombok")
	developmentOnly("org.springframework.boot:spring-boot-devtools")
	annotationProcessor("org.projectlombok:lombok")
	testImplementation("org.springframework.boot:spring-boot-starter-test")
	testRuntimeOnly("org.junit.platform:junit-platform-launcher")
	implementation("org.slf4j:slf4j-api:2.0.9")
	implementation("org.springframework.boot:spring-boot-starter-validation")
	implementation("org.aspectj:aspectjweaver:1.9.19")
	implementation("org.aspectj:aspectjrt:1.9.19")
	implementation("com.google.guava:guava:31.1-jre")
	implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.2.0")
	implementation("io.github.resilience4j:resilience4j-spring-boot2:1.7.1")
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
			exclude("/tbank/mr_irmag/cbr_ru/DTO/**")
		}
	}))
}
