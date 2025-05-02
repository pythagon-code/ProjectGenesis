package edu.illinois.abhayp4.projectgenesis.cerebrum.neurons;

import java.io.Closeable;
import java.util.ArrayList;
import java.util.List;
import java.util.PriorityQueue;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import edu.illinois.abhayp4.projectgenesis.cerebrum.brain.BrainSimulator;
import edu.illinois.abhayp4.projectgenesis.cerebrum.channels.SourceDataChannel;
import edu.illinois.abhayp4.projectgenesis.cerebrum.channels.TargetDataChannel;
import edu.illinois.abhayp4.projectgenesis.cerebrum.workers.ModelWorker;
import jakarta.annotation.Nonnull;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.MINIMAL_CLASS,
    include = JsonTypeInfo.As.PROPERTY
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PrimitiveNeuron.class),
    @JsonSubTypes.Type(value = MetaNeuron.class),
    @JsonSubTypes.Type(value = ResponseNeuron.class)
})
sealed abstract class RelayNeuron implements Runnable, Closeable permits MetaNeuron {
    private final List<SourceDataChannel> sources;
    private final List<TargetDataChannel> targets;
    private final PriorityQueue<TransmissionMessage> transmissionMessages;
    private BrainSimulator brain = null;
    private final Thread thread;
    private final ModelWorker modelWorker;
    private boolean awaken = false;
    private boolean done = false;

    private record TransmissionMessage(int channelIdx, String message, int targetStep)
        implements Comparable<TransmissionMessage>
    {
        @Override
        public int compareTo(@Nonnull TransmissionMessage other) {
            return Integer.compare(targetStep, other.targetStep);
        }
    }

    public RelayNeuron() {
        sources = new ArrayList<>();
        targets = new ArrayList<>();

        transmissionMessages = new PriorityQueue<>();

        thread = new Thread(this, "RelayNeuron-NeuronThread");
        thread.setDaemon(false);

        modelWorker = null;
    }

    public void attachBrain(@Nonnull BrainSimulator brain) {
        this.brain = brain;
        brain.heartbeat.registerHeartbeat();
    }

    public void addSource(@Nonnull SourceDataChannel source) {
        if (sources.contains(source)) {
            throw new IllegalArgumentException();
        }

        sources.add(source);
    }

    public void addTarget(TargetDataChannel target) {
        if (targets.contains(target)) {
            throw new IllegalArgumentException();
        }

        targets.add(target);
    }

    public void start() {
        thread.start();
    }

    public void awake() {
        awaken = true;
    }

    protected void sendMessage(int channelIdx, String message, int targetStep) {
        TargetDataChannel target = targets.get(channelIdx);
        transmissionMessages.add(new TransmissionMessage(channelIdx, message, targetStep));
    }

    @Override
    public void run() {
        do {
            brain.heartbeat.awaitProcessMessagePhase();

            for (int i = 0; i < sources.size(); i++) {
                SourceDataChannel source = sources.get(i);
                while (source.hasMessage()) {
                    String message = source.removeMessage();
                    onMessageReceived(i, message);
                    awaken = false;
                }
            }

            if (awaken) {
                onAwaken();
                awaken = false;
            }

            brain.heartbeat.awaitSendMessagePhase();

            while (
                !transmissionMessages.isEmpty()
                && transmissionMessages.peek().targetStep == brain.heartbeat.getCurrentStep()
            ) {
                TransmissionMessage transmission = transmissionMessages.remove();
                targets.get(transmission.channelIdx).addMessage(transmission.message);
            }
        }
        while (!done);
    }

    @Override
    public synchronized void close() {
        done = true;
        try {
            thread.join();
        }
        catch (InterruptedException e) {
            throw new IllegalThreadStateException(e.getMessage());
        }
    }

    protected abstract void onMessageNotSent();

    protected abstract void onMessageReceived(int channelIdx, @Nonnull String message);

    protected abstract void onAwaken();
}
