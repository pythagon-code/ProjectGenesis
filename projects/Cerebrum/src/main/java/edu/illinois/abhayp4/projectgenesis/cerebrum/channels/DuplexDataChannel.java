package edu.illinois.abhayp4.projectgenesis.cerebrum.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public record DuplexDataChannel(SimplexDataChannel channel1, SimplexDataChannel channel2) {
    public DuplexDataChannel() {
        this(
            new SimplexDataChannel(),
            new SimplexDataChannel()
        );
    }
}
