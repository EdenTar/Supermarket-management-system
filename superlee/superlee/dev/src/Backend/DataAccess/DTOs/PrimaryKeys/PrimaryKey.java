package Backend.DataAccess.DTOs.PrimaryKeys;


public interface PrimaryKey {

    Object[] getValue();

    boolean equals(Object other);

    int hashCode();

    String primaryKeyToString();
}
