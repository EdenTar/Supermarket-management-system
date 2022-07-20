package Backend.DataAccess.DTOs.TransportDTOS;


import Backend.DataAccess.DTOs.DTO;

import java.lang.reflect.Field;

import Backend.DataAccess.DTOs.PrimaryKeys.PK;

public class TruckDTO extends DTO<PK> {

    private final long truckId;
    private final String model;
    private final long basicWeight;
    private final long maxWeight;
    private final String license;

    public TruckDTO(long truckId, String model, long basicWeight, long maxWeight, String license) {
        super(new PK(getFields(), truckId));
        this.truckId = truckId;
        this.model = model;
        this.basicWeight = basicWeight;
        this.maxWeight = maxWeight;
        this.license = license;
    }

    public static Field[] getFields() {
        return DTO.getFields(new String[]{"truckId"}, TruckDTO.class);
    }

    public static PK getPK(long truckId) {
        return new PK(getFields(), truckId);
    }

    public long getTruckId() {
        return truckId;
    }

    public String getModel() {
        return model;
    }

    public long getBasicWeight() {
        return basicWeight;
    }

    public long getMaxWeight() {
        return maxWeight;
    }

    public String getLicense() {
        return license;
    }

    @Override
    public Object[] getValues() {
        return new Object[]{truckId, model, basicWeight, maxWeight, license};
    }
}



