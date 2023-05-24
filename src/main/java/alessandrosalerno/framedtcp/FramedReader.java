package alessandrosalerno.framedtcp;

import java.io.IOException;
import java.io.Reader;
import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class FramedReader {
    private final Reader reader;
    private final Charset charset;
    private final FrameSizeValidator frameSizeValidator;

    public FramedReader(Reader reader,
                        Charset charset,
                        FrameSizeValidator frameSizeValidator) {

        this.reader = reader;
        this.charset = charset;
        this.frameSizeValidator = frameSizeValidator;
    }

    public FramedReader(Reader reader,
                        Charset charset) {

        this.reader = reader;
        this.charset = charset;
        this.frameSizeValidator = new DefaultFrameSizeValidator();
    }

    public FramedReader(Reader reader,
                        FrameSizeValidator frameSizeValidator) {

        this.reader = reader;
        this.charset = StandardCharsets.UTF_8;
        this.frameSizeValidator = frameSizeValidator;
    }

    public FramedReader(Reader reader) {
        this.reader = reader;
        this.charset = StandardCharsets.UTF_8;
        this.frameSizeValidator = new DefaultFrameSizeValidator();
    }

    public byte[] readBytes() throws IOException {
        FrameHeader header = this.readHeader();

        if (!this.frameSizeValidator.validFrameSize(header.messageSize()))
            throw new FrameSizeOutOfBoundsException(header.messageSize(),
                                                        this.frameSizeValidator.getMaxSize());

        return this.readSegment(header.messageSize());
    }

    public String readString() throws IOException {
        return new String(this.readBytes(), this.charset);
    }

    public Charset getCharset() {
        return this.charset;
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
