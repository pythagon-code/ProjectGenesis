@SuppressWarnings("module")
module edu.illinois.abhayp4.projectgenesis.cerebrum {
    requires com.fasterxml.jackson.databind;
    requires jakarta.annotation;
    requires java.logging;
    requires javafx.controls;
    requires org.yaml.snakeyaml;

    exports edu.illinois.abhayp4.projectgenesis.cerebrum.application;
    exports edu.illinois.abhayp4.projectgenesis.cerebrum.main;
}
