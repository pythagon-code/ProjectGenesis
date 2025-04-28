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

    tasks.withType<ProcessResources> {
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
    }
}

dependencies {
    implementation(project(":cerebrum"))
    implementation(project(":new-eden"))
}

application {
    mainClass.set("edu.illinois.abhayp4.projectgenesis.main.Main")
}

tasks.withType<Jar> {
    dependsOn(tasks.compileJava)

    manifest {
        attributes["Main-Class"] = "edu.illinois.abhayp4.projectgenesis.main.Main"
    }

    from(sourceSets.main.get().output)

    from("src/main/resources/") {
        into("resources/")
    }

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}