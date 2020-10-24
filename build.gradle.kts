plugins {
    `java-library`
}

repositories {
    jcenter()
}

val reactorVersion = "3.3.10.RELEASE"
val assertJVersion = "3.17.2"
val jupiterVersion = "5.6.2"

dependencies {
    api("org.assertj:assertj-core:$assertJVersion")
    api("io.projectreactor:reactor-core:$reactorVersion")
    testImplementation("io.projectreactor:reactor-test:$reactorVersion")
    testImplementation("org.junit.jupiter:junit-jupiter-api:$jupiterVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$jupiterVersion")
}

val test by tasks.getting(Test::class) {
    useJUnitPlatform()
}
