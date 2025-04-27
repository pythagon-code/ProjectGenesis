plugins {
    application
}

group = "edu.illinois.abhayp4.projectgenesis.neweden"
version = "1.0-SNAPSHOT"

application {
    mainClass.set("edu.illinois.abhayp4.projectgenesis.meweden.main.Main")
}

tasks.withType<Jar> {
    manifest {
        attributes["Main-Class"] = "edu.illinois.abhayp4.projectgenesis.neweden.Main"
    }

    from(sourceSets.main.get().output)

    from("src/main/resources")

    from(configurations.runtimeClasspath.get().map { if (it.isDirectory) it else zipTree(it) })
    duplicatesStrategy = DuplicatesStrategy.EXCLUDE
}