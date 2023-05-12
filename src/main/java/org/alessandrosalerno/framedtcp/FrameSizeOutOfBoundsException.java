package org.alessandrosalerno.framedtcp;

public class FrameSizeOutOfBoundsException extends RuntimeException {
    public FrameSizeOutOfBoundsException(int size, int maxSize) {
        super("Message of size " + size + " bytes exceeds the maximum limit of " + maxSize + " bytes");
    }
}
