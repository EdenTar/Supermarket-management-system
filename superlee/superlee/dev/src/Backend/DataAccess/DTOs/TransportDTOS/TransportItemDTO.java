package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class TransportItemDTO extends DTO<PK> {


    private final String name;
    private final long quantity;
    private final long orderTransportId;
    private final String destinationFileId;

    public TransportItemDTO(String name, long quantity, long orderTransportId, String destinationFileId) {
        super(new PK(getFields(),name,orderTransportId, destinationFileId));

        this.name = name;
        this.quantity = quantity;
        this.orderTransportId = orderTransportId;
        this.destinationFileId = destinationFileId;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"name","orderTransportId", "destinationFileId"}, TransportItemDTO.class);
    }

    public static PK getPK(String name,long orderTransportId, String destinationFileId) {
        return new PK(getFields(),name, orderTransportId, destinationFileId);
    }

    public String getName() {
        return name;
    }

    public long getQuantity() {
        return quantity;
    }

    public String getDestinationFileId() {
        return destinationFileId;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{ name, quantity, orderTransportId, destinationFileId};
    }

    public long getOrderTransportId() {
        return orderTransportId;
    }
}



