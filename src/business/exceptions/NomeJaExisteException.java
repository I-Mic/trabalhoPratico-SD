package business.exceptions;

public class NomeJaExisteException extends Exception {
    public NomeJaExisteException(String msg) {
        super(msg);
    }
}