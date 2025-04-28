package edu.illinois.abhayp4.projectgenesis.cerebrum.brain;

import java.util.concurrent.Phaser;

public final class MessageHeartbeat {
    private Phaser processMessageBarrier, sendMessageBarrier;

    private boolean running = false;

    public MessageHeartbeat() {
        processMessageBarrier = new Phaser(1);
        sendMessageBarrier = new Phaser(0);
    }

    public synchronized void registerHeartbeat() {
        if (running) {
            throw new IllegalStateException("Cannot register heartbeat during heartbeat loop");
        }

        processMessageBarrier.register();
        sendMessageBarrier.register();
    }

    public void awaitProcessMessagePhase() {
        processMessageBarrier.arriveAndAwaitAdvance();
    }

    public void awaitSendMessagePhase() {
        sendMessageBarrier.arriveAndAwaitAdvance();
    }

    public int getCurrentStep() {
        return processMessageBarrier.getPhase();
    }

    public int getTargetStep(int deltaSteps) {
        int endStep = processMessageBarrier.getPhase() + deltaSteps;
        return (endStep < 0) ? (endStep - Integer.MIN_VALUE) : endStep;
    }

    synchronized void step() {
        running = true;
        processMessageBarrier.arrive();
        do {
            Thread.onSpinWait();
        } while (processMessageBarrier.getUnarrivedParties() != 1);
    }
}