plugins {
    application
    id("org.openjfx.javafxplugin") version "0.1.0"
}

group = "edu.illinois.abhayp4.projectgenesis.cerebrum"
version = "1.0-SNAPSHOT"

dependencies {
    implementation("org.yaml:snakeyaml:2.3")
    implementation(platform("com.fasterxml.jackson:jackson-bom:2.18.0"))
    implementation("com.fasterxml.jackson.core:jackson-core")
    implementation("com.fasterxml.jackson.core:jackson-annotations")
    implementation("com.fasterxml.jackson.core:jackson-databind")
    implementation("jakarta.annotation:jakarta.annotation-api:2.1.1")
}

sourceSets {
    main {
        resources {
            srcDirs("src/main/resources", "src/main/python")
        }
    }
}

application {
    mainClass.set("edu.illinois.abhayp4.projectgenesis.cerebrum.main.Main")
}

javafx {
    version = "24.0.1"
    modules("javafx.controls", "javafx.fxml")
}

tasks.withType<Jar> {
    dependsOn(tasks.compileJava)

    manifest {
        attributes["Main-Class"] = "edu.illinois.abhayp4.projectgenesis.cerebrum.Main"
    }

    from(sourceSets.main.get().output)

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })

    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}