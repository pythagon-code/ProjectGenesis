/**
 * TargetChannel.java
 * @author Abhay Pokhriyal
 */

package edu.illinois.abhayp4.projectgenesis.cerebrum.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public sealed interface TargetDataChannel permits SimplexDataChannel {
    void addMessage(TransmissionMessage message);
}
