plugins {
    id("java")
    id("me.champeau.jmh") version "0.7.2"
}

group = "t_bank.mr_irmag"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}


dependencies {
    implementation("org.apache.kafka:kafka-clients:3.6.0")
    implementation("com.rabbitmq:amqp-client:5.16.0")
    implementation("org.openjdk.jmh:jmh-core:1.33")
}

jmh {
    warmupIterations.set(1)
    iterations.set(3)
    fork.set(1)
}

tasks.test {
    useJUnitPlatform()
}