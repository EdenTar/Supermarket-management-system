package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.Order;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierOrdersDTO;
import Backend.Logic.LogicObjects.Supplier.ProductOrder;
import Obj.Parser;

import java.util.LinkedList;
import java.util.List;

public class SupplierOrdersDAO extends DAO<PK, SupplierOrdersDTO, Order>{

    public SupplierOrdersDAO() {
        super(SupplierOrdersDTO.class,IM.getInstance().getIdentityMap(Order.class));
    }

    @Override
    protected Order convertDtoToBusiness(SupplierOrdersDTO dto) {
        return new Order(
                dto,
                (LinkedList<ProductOrder>) new ProductOrderDAO().getRowsFromDB("orderId = " + dto.getOrderId()) /*TODO check condition*/);
    }

    @Override
    protected SupplierOrdersDTO convertBusinessToDto(Order business) {
        return new SupplierOrdersDTO(
                business.getId(),
                business.getSupplierCn(),
                business.getSupplierName(),
                business.getFromAddress(),
                business.getToAddress(),
                Parser.getStrDate(business.getDate()),
                business.getSupplierContactPhoneNum(),
                business.getTransportID());
    }

    @Override
    protected SupplierOrdersDTO createDTO(List<Object> listFields) {
        return new SupplierOrdersDTO((int) listFields.get(0),(String) listFields.get(1),(String) listFields.get(2),(String) listFields.get(3),
                (String) listFields.get(4),(String) listFields.get(5),(String) listFields.get(6),(int) listFields.get(7));
    }

    public int getNextOrderId(){
        long max = 0;
            for(SupplierOrdersDTO o : selectAll()){
                if(o.getOrderId() > max)
                    max = o.getOrderId();
            }
        return (int)max;
    }
}



