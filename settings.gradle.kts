plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.10.0"
}

rootProject.name = "project-genesis"

include("cerebrum", "new-eden")

//project(":cerebrum").projectDir = file("projects/Cerebrum")
//project(":new-eden").projectDir = file("projects/NewEden")