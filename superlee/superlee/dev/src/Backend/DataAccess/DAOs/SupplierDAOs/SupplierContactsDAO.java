package Backend.DataAccess.DAOs.SupplierDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Supplier.Contact;
import Backend.DataAccess.DTOs.SupplierDTOS.SupplierContactsDTO;

import java.util.List;

public class SupplierContactsDAO extends DAO<PK, SupplierContactsDTO, Contact>{
    public SupplierContactsDAO() {
        super(SupplierContactsDTO.class,IM.getInstance().getIdentityMap(Contact.class));
    }
//tring cn, String contactPhoneNumber, String contactName, String contactEmail
    @Override
    protected Contact convertDtoToBusiness(SupplierContactsDTO dto) {
        return new Contact(dto.getContactEmail(), dto.getContactName(),dto.getContactPhoneNumber(),dto.getCn() );
    }

    @Override
    protected SupplierContactsDTO convertBusinessToDto(Contact business) {
        return new SupplierContactsDTO(business.getCn(),business.getPhoneNumber(),business.getName(),business.getEmail());
    }

    @Override
    protected SupplierContactsDTO createDTO(List<Object> listFields) {
        return new SupplierContactsDTO((String) listFields.get(0),(String) listFields.get(1),(String) listFields.get(2),(String) listFields.get(3));
    }
}



