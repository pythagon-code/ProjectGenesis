package edu.illinois.abhayp4.projectgenesis.cerebrum.neurons;

import jakarta.annotation.Nonnull;

import java.util.ArrayList;
import java.util.List;

sealed class MetaNeuron extends RelayNeuron permits ResponseNeuron {
    private List<RelayNeuron> neurons;
    
    public MetaNeuron() {
        neurons = new ArrayList<>();
    }

    public void setAdjacentNeuron(RelayNeuron other) {
        
    }

    @Override
    protected void onMessageNotSent() {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onMessageReceived(int channelIdx, @Nonnull String message) {
        // TODO Auto-generated method stub
        
    }

    @Override
    protected void onAwaken() {
        // TODO Auto-generated method stub
        
    }
}
