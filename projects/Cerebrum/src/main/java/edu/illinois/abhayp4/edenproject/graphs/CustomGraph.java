package edu.illinois.abhayp4.edenproject.graphs;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public final class CustomGraph extends Graph {
    @JsonProperty("NVertices") public int nVertices;
    @JsonProperty("Edges") public List<List<Integer>> edges;

    public CustomGraph(
        @JsonProperty("Tag") String tag,
        @JsonProperty("NVertices") int nVertices,
        @JsonProperty("Edges") List<List<Integer>> edges
    ) {
        super(tag);
        this.nVertices = nVertices;
        this.edges = edges;
    }
}