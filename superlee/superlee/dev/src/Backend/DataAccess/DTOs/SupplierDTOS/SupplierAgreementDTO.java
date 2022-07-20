package Backend.DataAccess.DTOs.SupplierDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;


public class SupplierAgreementDTO extends DTO<PK> {

    private final String cn;
    private final String isConsistent;

    public SupplierAgreementDTO(String cn, String isConsistent) {
        super(new PK(getFields(), cn));
        this.cn = cn;
        this.isConsistent = isConsistent;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"cn"}, SupplierAgreementDTO.class);
    }

    public static PK getPK(String cn) {
        return new PK(getFields(), cn);
    }

    public String getCn() {
        return cn;
    }

    public String getIsConsistent() {
        return isConsistent;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{cn, isConsistent};
    }
}



