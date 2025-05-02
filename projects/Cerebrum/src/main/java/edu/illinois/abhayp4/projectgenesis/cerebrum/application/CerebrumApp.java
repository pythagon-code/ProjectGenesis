package edu.illinois.abhayp4.projectgenesis.cerebrum.application;

import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.BrainSimulator;
import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.SimulatorSettings;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;

public class CerebrumApp extends Application implements Closeable {
    private final CerebrumAppContext context = new CerebrumAppContext();
    private boolean done = false;

    public static String[] getResourceListing(Class<?> clazz, String path) throws IOException {
        URL dirURL = clazz.getClassLoader().getResource(path);
        if (dirURL != null && dirURL.getProtocol().equals("file")) {
            try {
                return new File(dirURL.toURI()).list();
            } catch (URISyntaxException e) {
                throw new IOError(e);
            }
        }
        return null;
    }

    @Override
    public void start(Stage stage) throws IOException {
        //System.out.println(getResourceListing(CerebrumApp.class, "configs/"));

        new ProcessBuilder("python3 src/main/python/worker.py").start();
        Properties properties = new Properties();
        SimulatorSettings settings;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("simulator.properties")) {
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
