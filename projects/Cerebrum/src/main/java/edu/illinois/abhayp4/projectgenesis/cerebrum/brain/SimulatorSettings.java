package edu.illinois.abhayp4.projectgenesis.cerebrum.brain;

import java.io.*;
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
        try (InputStream stream = SimulatorSettings.class.getResourceAsStream("/configs/config.properties")) {
            Properties configProperties = new Properties();
            configProperties.load(stream);
            return loadFromFiles(
                configProperties.getProperty("config.path"),
                "system.yml",
                "model_architecture.yml",
                "transformers.yml",
                "neuron_topology.yml",
                "base_neuron.yml",
                "graph_structures.yml",
                "optimization.yml"
            );
        }
    }

    private static SimulatorSettings loadFromFiles(
        String configPath,
        String systemResource,
        String modelArchitectureResource,
        String transformersResource,
        String neuronTopologyResource,
        String baseNeuronResource,
        String graphStructuresResource,
        String optimizationResource
    ) throws IOException {
        try (
            InputStream systemStream = getConfigStream(configPath, systemResource);
            InputStream modelArchitectureStream = getConfigStream(configPath, modelArchitectureResource);
            InputStream transformersStream = getConfigStream(configPath, transformersResource);
            InputStream neuronTopologyStream = getConfigStream(configPath, neuronTopologyResource);
            InputStream baseNeuronStream = getConfigStream(configPath, baseNeuronResource);
            InputStream graphStructuresStream = getConfigStream(configPath, graphStructuresResource);
            InputStream optimizationStream = getConfigStream(configPath, optimizationResource);
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
        return SimulatorSettings.class.getResourceAsStream(configPath + resourceName);
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