package cz.cvut.fel.bp.leisureportalbackend.exception;

public class AlreadyLoginException extends Exception {
    public AlreadyLoginException() {
        super("You are already login.");
    }
}
