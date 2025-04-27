plugins {
    java
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

application {
    mainClass.set("edu.illinois.abhayp4.projectgenesis.cerebrum.main.Main")
}

javafx {
    version = "24.0.1"
    modules("javafx.controls", "javafx.fxml")
}