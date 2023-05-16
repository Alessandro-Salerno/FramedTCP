package org.alessandrosalerno.framedtcp;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;

public class FramedReader {
    private final Reader reader;
    private final FrameSizeValidator frameSizeValidator;

    public FramedReader(Reader reader, FrameSizeValidator frameSizeValidator) {
        this.reader = reader;
        this.frameSizeValidator = frameSizeValidator;
    }

    public FramedReader(Reader reader) {
        this.reader = reader;
        this.frameSizeValidator = new DefaultFrameSizeValidator();
    }

    public byte[] readBytes() throws IOException {
        FrameHeader header = this.readHeader();

        if (!this.frameSizeValidator.validFrameSize(header.messageSize()))
            throw new FrameSizeOutOfBoundsException(header.messageSize(),
                                                        this.frameSizeValidator.getMaxSize());

        return this.readSegment(header.messageSize());
    }

    public char[] readChars() throws IOException {
        return ByteBuffer.wrap(this.readBytes()).asCharBuffer().array();
    }

    public String readString() throws IOException {
        return new String(this.readBytes());
    }

    private FrameHeader readHeader() throws IOException {
        byte[] lengthBuffer = this.readSegment(4);
        int messageLength = ByteBuffer.wrap(lengthBuffer).getInt();
        return new FrameHeader(messageLength);
    }

    private byte[] readSegment(int size) throws IOException {
        byte[] bytes = new byte[size];
        for (int i = 0; i < bytes.length; i++)
            bytes[i] = (byte) this.reader.read();

        return bytes;
    }
}
