package edu.illinois.abhayp4.projectgenesis.cerebrum.channels;

import java.util.ArrayDeque;
import java.util.Queue;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public final class SimplexDataChannel implements SourceDataChannel, TargetDataChannel {
    private final Queue<String> queue;
    public final int capacity;

    public SimplexDataChannel(int capacity) {
        queue = new ArrayDeque<>();
        this.capacity = capacity;
    }

    @Override
    public String removeMessage() {
        if (!hasMessage()) {
            throw new IllegalStateException("No message to remove");
        }

        return queue.remove();
    }

    @Override
    public boolean hasMessage() {
        return !queue.isEmpty();
    }

    @Override
    public void addMessage(String message) {
        if (!hasSpace()) {
            throw new IllegalStateException("No space to add message");
        }

        queue.add(message);
    }

    @Override
    public boolean hasSpace() {
        return queue.size() < capacity;
    }
}