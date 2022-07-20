package Backend.DataAccess.Exceptions;

public class DbException extends RuntimeException {
    public DbException(String s) {
        super(s);
    }

    public DbException() {
        super();
    }
}
