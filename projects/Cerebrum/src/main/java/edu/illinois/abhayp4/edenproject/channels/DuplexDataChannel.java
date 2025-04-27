package edu.illinois.abhayp4.edenproject.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public record DuplexDataChannel(SimplexDataChannel channel1, SimplexDataChannel channel2) {
    public DuplexDataChannel(int capacity) {
        this(
            new SimplexDataChannel(capacity),
            new SimplexDataChannel(capacity)
        );
    }
}