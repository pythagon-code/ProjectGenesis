package edu.illinois.abhayp4.projectgenesis.cerebrum.application;

import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.BrainSimulator;
import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.SimulatorSettings;
import edu.illinois.abhayp4.projectgenesis.cerebrum.workers.PythonExecutor;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CerebrumApp extends Application implements Closeable {
    private final CerebrumAppContext context = new CerebrumAppContext();
    private boolean done = false;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Properties properties = new Properties();
        SimulatorSettings settings;

        new PythonExecutor();

        try (InputStream stream = getClass().getResourceAsStream("/configs/config.properties")) {
            if (stream == null)
                throw new IOException();
            properties.load(stream);
            settings = SimulatorSettings.loadFromProperties(properties);
        } catch (IOException e) {
            throw new IOError(e);
        }

        new Thread(() -> new BrainSimulator(settings).start(context), "BrainSimulator-Thread");

        primaryStage.show();
    }

    @Override
    public void close() {
        done = true;
    }
}
