package edu.illinois.abhayp4.projectgenesis.cerebrum.application;

import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.BrainSimulator;
import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.SimulatorSettings;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.util.Properties;

public class CerebrumApp extends Application implements Closeable {
    private final CerebrumAppContext context = new CerebrumAppContext();
    private boolean done = false;

    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println(getResourceListing(CerebrumApp.class, "configs/"));

        new ProcessBuilder("python3 src/main/python/worker.py").start();
        Properties properties = new Properties();
        SimulatorSettings settings;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("configs/config.properties")) {
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
