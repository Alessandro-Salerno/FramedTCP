package org.alessandrosalerno.framedtcp;

import java.io.IOException;
import java.io.Writer;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FramedWriter {
    private final Writer writer;
    private final Charset charset;
    private final FrameSizeValidator frameSizeValidator;

    public FramedWriter(Writer writer,
                        Charset charset,
                        FrameSizeValidator frameSizeValidator) {

        this.writer = writer;
        this.charset = charset;
        this.frameSizeValidator = frameSizeValidator;
    }

    public FramedWriter(Writer writer,
                        Charset charset) {

        this.writer = writer;
        this.charset = charset;
        this.frameSizeValidator = new DefaultFrameSizeValidator();
    }

    public FramedWriter(Writer writer,
                        FrameSizeValidator frameSizeValidator) {

        this.writer = writer;
        this.charset = StandardCharsets.UTF_8;
        this.frameSizeValidator = frameSizeValidator;
    }

    public FramedWriter(Writer writer) {
        this.writer = writer;
        this.charset = StandardCharsets.UTF_8;
        this.frameSizeValidator = new DefaultFrameSizeValidator();
    }

    public void writeBytes(byte[] bytes) throws IOException {
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

    public void writeString(String string) throws IOException {
        this.writeBytes(string.getBytes(this.charset));
    }

    public Charset getCharset() {
        return this.charset;
    }

    private void writeByteBuffer(ByteBuffer byteBuffer, int size) throws IOException {
        for (int i = 0; i < size; i++)
            this.writer.write((char) byteBuffer.get(i));
    }
}
