package org.alessandrosalerno.framedtcp;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;

public class FramedWriter {
    private final Writer writer;
    private final FrameSizeValidator frameSizeValidator;

    public FramedWriter(Writer writer, FrameSizeValidator frameSizeValidator) {
        this.writer = writer;
        this.frameSizeValidator = frameSizeValidator;
    }

    public FramedWriter(Writer writer) {
        this.writer = writer;
        this.frameSizeValidator = new DefaultFrameSizeValidator();
    }

    public void sendBytes(byte[] bytes) throws IOException {
        int messageLength = bytes.length;

        if (!this.frameSizeValidator.validFrameSize(messageLength))
            throw new FrameSizeOutOfBoundsException(messageLength,
                                                        this.frameSizeValidator.getMaxSize());

        ByteBuffer lengthBuffer = ByteBuffer.allocate(4).putInt(messageLength);
        this.writeByteBuffer(lengthBuffer, 4);

        ByteBuffer messageBuffer = ByteBuffer.wrap(bytes);
        this.writeByteBuffer(messageBuffer, messageLength);

        this.writer.flush();
    }

    public void sendString(String string) throws IOException {
        this.sendBytes(string.getBytes());
    }

    private void writeByteBuffer(ByteBuffer byteBuffer, int size) throws IOException {
        for (int i = 0; i < size; i++)
            this.writer.write((char) byteBuffer.get(i));
    }
}
