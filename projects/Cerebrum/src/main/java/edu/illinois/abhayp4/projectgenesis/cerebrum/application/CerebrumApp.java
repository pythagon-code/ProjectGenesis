package edu.illinois.abhayp4.projectgenesis.cerebrum.application;

import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.BrainSimulator;
import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.SimulatorSettings;
import jakarta.annotation.Nonnull;
import javafx.application.Application;
import javafx.stage.Stage;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Properties;

public class CerebrumApp extends Application implements Closeable {
    private static final Path tempDir;

    static {
        try {
            tempDir = Files.createTempDirectory("python");
            new File(tempDir.toString()).deleteOnExit();

            String[] resources = {"worker.py", "requirements.txt"};

            for (String resource : resources) {
                try (PrintWriter writer = new PrintWriter(Paths.get(tempDir.toString(), resource).toString())) {
                    StringBuilder stringBuilder = new StringBuilder();
                    try (
                        BufferedReader reader = new BufferedReader(new InputStreamReader(
                            CerebrumApp.class.getClassLoader().getResourceAsStream(resource)))
                    ) {
                        while (reader.ready()) {
                            writer.println(reader.readLine());
                        }
                    }
                }
            }

            System.out.println(tempDir.toString());
        } catch (IOException e) {
            throw new IOError(e);
        }
    }

    private final CerebrumAppContext context = new CerebrumAppContext();
    private boolean done = false;

    @Override
    public void start(Stage primaryStage) throws IOException {
        String requirementsPath = Paths.get(tempDir.toString(), "requirements.txt").toString();
        String workerScriptPath = Paths.get(tempDir.toString(), "worker.py").toString();
        Process installRequirements = new ProcessBuilder(
            "python3", "-m", "pip", "install",  "-r", requirementsPath).inheritIO().start();
        try {
            int exitCode = installRequirements.waitFor();
            if (exitCode != 0) {
                throw new RuntimeException("Failed to install Python requirements. Please check your environment.");
            }
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        new ProcessBuilder("python3", workerScriptPath).start();

        Properties properties = new Properties();
        SimulatorSettings settings;
        try (InputStream stream = getClass().getClassLoader().getResourceAsStream("configs/config.properties")) {
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
