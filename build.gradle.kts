import com.jfrog.bintray.gradle.BintrayExtension.*
import java.util.*

version = "1.0.5"
description = "AssertJ extensions for Mono and Flux."
group = "pl.rzrz"

plugins {
    `java-library`
    `maven-publish`
    id("com.jfrog.bintray") version "1.8.5"
}

java {
    sourceCompatibility = JavaVersion.VERSION_1_8
    targetCompatibility = JavaVersion.VERSION_1_8
}

repositories {
    jcenter()
}

java {
    withSourcesJar()
    withJavadocJar()
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

val githubPath = "jacek-rzrz/assertj-reactor"
val githubUrl = "https://github.com/$githubPath"

configure<PublishingExtension> {
    publications {
        create<MavenPublication>("mavenProject") {
            from(components["java"])
            groupId = project.group.toString()
            artifactId = project.name
            version = project.version.toString()

            pom {
                name.set(project.name)
                description.set(project.description)
                url.set(githubUrl)

                licenses {
                    license {
                        name.set("The Apache Software License, Version 2.0")
                        url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        distribution.set("repo")
                    }
                }

                developers {
                    developer {
                        name.set("Jacek Rzrz")
                        email.set("jacek@rzrz.pl")
                    }
                }

                scm {
                    url.set("$githubUrl/tree/master")
                    connection.set("scm:git:git://github.com/$githubPath.git")
                    developerConnection.set("scm:git:ssh://github.com:$githubPath.git")
                }
            }
        }
    }
}

bintray {
    user = System.getenv("BINTRAY_USER")
    key = System.getenv("BINTRAY_KEY")
    setPublications("mavenProject")
    dryRun = false
    publish = true
    pkg(closureOf<PackageConfig> {
        repo = project.name
        name = project.name
        setLicenses("Apache-2.0")
        vcsUrl = githubUrl
        issueTrackerUrl = "$githubUrl/issues"
        publicDownloadNumbers = true
        githubRepo = githubPath
        version(closureOf<VersionConfig> {
            name = project.version.toString()
            desc = project.description
            released = Date().toString()
            gpg(closureOf<GpgConfig> {
                sign = true
            })
            mavenCentralSync(closureOf<MavenCentralSyncConfig> {
                sync = true
                user = System.getenv("OSS_SONATYPE_USER")
                password = System.getenv("OSS_SONATYPE_PASSWORD")
                close = "1"
            })
        })
    })
}

tasks.create("printVersion") {
    doLast {
        println(project.version)
    }
}