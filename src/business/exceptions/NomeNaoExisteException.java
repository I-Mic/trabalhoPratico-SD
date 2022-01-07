package business.exceptions;

public class NomeNaoExisteException extends Exception{
    public NomeNaoExisteException(String msg){
        super(msg);
    }
}
