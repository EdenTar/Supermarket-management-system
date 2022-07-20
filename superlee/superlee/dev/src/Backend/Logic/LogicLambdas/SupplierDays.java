package Backend.Logic.LogicLambdas;


import java.util.List;

public interface SupplierDays {

    // Easter egg: for a good time pls watch with sound on https://9gag.com/gag/adgWOoM
    boolean isConstSupplier(String supplierId);

    List<Integer> supplierDays(String supplierId);

    Integer preparationTime(String supplierId);
}
