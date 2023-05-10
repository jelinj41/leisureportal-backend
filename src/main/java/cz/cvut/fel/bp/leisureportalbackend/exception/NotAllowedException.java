package cz.cvut.fel.bp.leisureportalbackend.exception;

public class NotAllowedException extends Exception{
    public NotAllowedException() {
        super("Forbidden operation.");
    }
    public NotAllowedException(String message) {
        super(message);
    }
}
