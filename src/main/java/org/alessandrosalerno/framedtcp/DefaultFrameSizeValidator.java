package org.alessandrosalerno.framedtcp;

public final class DefaultFrameSizeValidator implements FrameSizeValidator {
    private final int maxSize;

    public DefaultFrameSizeValidator(int maxSize) {
        this.maxSize = maxSize;
    }

    public DefaultFrameSizeValidator() {
        this.maxSize = 120_000;
    }

    @Override
    public boolean validFrameSize(int size) {
        return size <= this.maxSize;
    }

    @Override
    public int getMaxSize() {
        return this.maxSize;
    }
}
