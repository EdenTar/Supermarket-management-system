package Backend.DataAccess.DTOs.StockDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class CategoriesDTO extends DTO<PK> {

    private final long id;
    private final long parent_ID;
    private String name;

    public CategoriesDTO(long id, long parentId, String name) {
        super(new PK(getFields(), id));
        this.id = id;
        this.parent_ID = parentId;
        this.name = name;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"id"}, CategoriesDTO.class);
    }

    public static PK getPK(long id) {
        return new PK(getFields(), id);
    }

    public long getId() {
        return id;
    }


    public long getParentId() {
        return parent_ID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{id, parent_ID, name};
    }
}



