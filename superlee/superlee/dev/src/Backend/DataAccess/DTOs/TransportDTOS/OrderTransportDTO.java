package Backend.DataAccess.DTOs.TransportDTOS;

import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class OrderTransportDTO extends DTO<PK> {

    private final long orderTransportId;
    private final String origin;
    private final String destination;
    private final String basedOnCreationTime;
    private final String supplierID;

    public OrderTransportDTO(long orderTransportId, String origin, String destination, String basedOnCreationTime,String supplierId) {
        super(new PK(getFields(), orderTransportId));
        this.orderTransportId = orderTransportId;
        this.origin = origin;
        this.destination = destination;
        this.basedOnCreationTime = basedOnCreationTime;
        this.supplierID = supplierId;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"orderTransportId"}, OrderTransportDTO.class);
    }

    public static PK getPK(long orderTransportId) {
        return new PK(getFields(), orderTransportId);
    }

    public long getOrderTransportId() {
        return orderTransportId;
    }

    public String getOrigin() {
        return origin;
    }

    public String getDestination() {
        return destination;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{orderTransportId, origin, destination, basedOnCreationTime,supplierID};
    }

    public String getBasedOnCreationTime() {
        return basedOnCreationTime;
    }

    public String getSupplierID() {
        return supplierID;
    }
}



