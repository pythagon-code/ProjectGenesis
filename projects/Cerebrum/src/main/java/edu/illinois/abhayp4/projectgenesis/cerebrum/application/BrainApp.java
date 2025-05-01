package edu.illinois.abhayp4.projectgenesis.cerebrum.application;

import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.BrainSimulator;
import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.SimulatorSettings;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.Closeable;
import java.io.IOError;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class BrainApp extends Application implements Closeable {
    public static void main(String[] args) {
        launch(args);
    }

    private final BrainAppContext context = new BrainAppContext();
    private boolean done = false;

    @Override
    public void start(Stage stage) /* */ throws IOException {
        new ProcessBuilder("python3 src/main/python/worker.py").start();
        Properties properties = new Properties();
        SimulatorSettings settings;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("edu/illinois/abhayp4/project-genesis/celebrum/simulator.properties")) {
            properties.load(stream);
            settings = SimulatorSettings.loadFromProperties(properties);
        } catch (IOException e) {
            throw new IOError(e);
        }

        new Thread(() -> new BrainSimulator(settings).start(context), "BrainSimulator-Thread");

        stage.show();
    }

    @Override
    public void close() {
        done = true;
    }
}
