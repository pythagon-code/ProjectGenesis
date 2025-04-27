plugins {
    application
}

group = "edu.illinois.abhayp4.projectgenesis"
version = "1.0-SNAPSHOT"

allprojects {
    apply(plugin = "java")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(23))
        }
    }

    repositories {
        mavenCentral()
    }

    dependencies {
        testImplementation("org.junit.jupiter:junit-jupiter:5.7.1")
        testRuntimeOnly("org.junit.platform:junit-platform-launcher")
    }

    tasks.withType<Test>().configureEach {
        useJUnitPlatform()
        maxHeapSize = "1G"

        testLogging {
            events("passed", "failed", "skipped")
            showStandardStreams = true
        }
    }
}

application {
    mainClass.set("edu.illinois.abhayp4.projectgenesis.Main")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "edu.illinois.abhayp4.projectgenesis.Main"
    }

    from(sourceSets.main.get().output)

    from("src/main/resources")

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}