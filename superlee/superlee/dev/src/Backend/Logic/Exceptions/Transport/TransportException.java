package Backend.Logic.Exceptions.Transport;

public class TransportException extends RuntimeException{

    public TransportException(String s)
    {
        super(s);
    }
    public TransportException()
    {
        super();
    }
}
