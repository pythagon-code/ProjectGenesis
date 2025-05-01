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
        try (BufferedReader reader = new BufferedReader(new FileReader(properties.getProperty("config.file.path")))) {
            return loadFromFiles(
                reader.readLine().strip(),
                properties.getProperty("system"),
                properties.getProperty("model.architecture"),
                properties.getProperty("transformers"),
                properties.getProperty("neuron.topology"),
                properties.getProperty("base.neuron"),
                properties.getProperty("graph.structures"),
                properties.getProperty("optimization")
            );
        }
    }

    private static SimulatorSettings loadFromFiles(
        String configFilePath,
        String systemFile,
        String modelArchitectureFile,
        String transformersFile,
        String neuronTopologyFile,
        String baseNeuronFile,
        String graphStructuresFile,
        String optimizationFile
    ) throws IOException {
        try (
            FileInputStream systemStream = new FileInputStream(Paths.get(configFilePath, systemFile).toString());
            FileInputStream modelArchitectureStream = new FileInputStream(Paths.get(configFilePath, modelArchitectureFile).toString());
            FileInputStream transformersStream = new FileInputStream(Paths.get(configFilePath, transformersFile).toString());
            FileInputStream neuronTopologyStream = new FileInputStream(Paths.get(configFilePath, neuronTopologyFile).toString());
            FileInputStream baseNeuronStream = new FileInputStream(Paths.get(configFilePath, baseNeuronFile).toString());
            FileInputStream graphStructuresStream = new FileInputStream(Paths.get(configFilePath, graphStructuresFile).toString());
            FileInputStream optimizationStream = new FileInputStream(Paths.get(configFilePath, optimizationFile).toString());
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