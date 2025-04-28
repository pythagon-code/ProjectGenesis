/**
 * SourceChannel.java
 * @author Abhay Pokhriyal
 */

package edu.illinois.abhayp4.projectgenesis.cerebrum.channels;

import com.fasterxml.jackson.annotation.JsonIgnoreType;

@JsonIgnoreType
public sealed interface SourceDataChannel permits SimplexDataChannel {
    String removeMessage();
    boolean hasMessage();
}