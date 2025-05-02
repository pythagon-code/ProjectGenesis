package edu.illinois.abhayp4.projectgenesis.cerebrum.brain;

import java.io.*;
import java.nio.file.Paths;
import java.util.Map;
import java.util.Properties;

import jakarta.annotation.Nonnull;
import org.yaml.snakeyaml.Yaml;

public record SimulatorSettings(
    @Nonnull Map<String, Object> systemObject,
    @Nonnull Map<String, Object> modelArchitectureObject,
    @Nonnull Map<String, Object> transformersObject,
    @Nonnull Map<String, Object> neuronTopologyObject,
    @Nonnull Map<String, Object> baseNeuronObject,
    @Nonnull Map<String, Object> graphStructuresObject,
    @Nonnull Map<String, Object> optimizationObject
) {
    public static @Nonnull SimulatorSettings loadFromProperties(@Nonnull Properties properties) throws IOException {
        try (
            InputStream stream = SimulatorSettings.class.getClassLoader()
                .getResourceAsStream("configs/config.properties")
        ) {
            Properties configProperties = new Properties();
            configProperties.load(stream);
            return loadFromFiles(
                configProperties.getProperty("config.path"),
                "system.yml",
                "modelArchitecture.yml",
                "transformers.yml",
                "neuronTopology.yml",
                "baseNeuron.yml",
                "graphStructures.yml",
                "optimization.yml"
            );
        }
    }

    private static SimulatorSettings loadFromFiles(
        String configPath,
        String systemFile,
        String modelArchitectureFile,
        String transformersFile,
        String neuronTopologyFile,
        String baseNeuronFile,
        String graphStructuresFile,
        String optimizationFile
    ) throws IOException {
        try (
            InputStream systemStream = getConfigStream(configPath, systemFile);
            InputStream modelArchitectureStream = getConfigStream(configPath, modelArchitectureFile);
            InputStream transformersStream = getConfigStream(configPath, transformersFile);
            InputStream neuronTopologyStream = getConfigStream(configPath, neuronTopologyFile);
            InputStream baseNeuronStream = getConfigStream(configPath, baseNeuronFile);
            InputStream graphStructuresStream = getConfigStream(configPath, graphStructuresFile);
            InputStream optimizationStream = getConfigStream(configPath, optimizationFile);
        ) {
            return new SimulatorSettings(
                new Yaml(),
                systemStream,
                modelArchitectureStream,
                transformersStream,
                neuronTopologyStream,
                baseNeuronStream,
                graphStructuresStream,
                optimizationStream
            );
        }
    }

    private static InputStream getConfigStream(String configPath, String resourceName) {
        return SimulatorSettings.class.getClassLoader().getResourceAsStream(
            Paths.get(configPath, resourceName).toString());
    }

    private SimulatorSettings(
        Yaml yaml,
        InputStream systemStream,
        InputStream modelArchitectureStream,
        InputStream transformersStream,
        InputStream neuronTopologyStream,
        InputStream baseNeuronStream,
        InputStream graphStructuresStream,
        InputStream optimizationStream
    ) {
        this(
            yaml.load(systemStream),
            yaml.load(modelArchitectureStream),
            yaml.load(transformersStream),
            yaml.load(neuronTopologyStream),
            yaml.load(baseNeuronStream),
            yaml.load(graphStructuresStream),
            yaml.load(optimizationStream)
        );
    }
}