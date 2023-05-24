package alessandrosalerno.framedtcp;

public interface FrameSizeValidator {
    boolean validFrameSize(int size);
    int getMaxSize();
}
