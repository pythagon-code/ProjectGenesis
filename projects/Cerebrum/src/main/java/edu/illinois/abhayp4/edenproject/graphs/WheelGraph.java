package edu.illinois.abhayp4.edenproject.graphs;

import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.annotation.Nonnull;

final class WheelGraph extends CycleGraph {
    @JsonCreator
    public WheelGraph(
        @JsonProperty("Tag") String tag,
        @JsonProperty("N") int n
    ) {
        super(tag, n - 1);

        if (n <= 3) {
            throw new IllegalArgumentException();
        }

        GraphNode center = addNode();
        for (GraphNode node : nodes) {
            center.setAdjacentNode(node);
        }
    }
}