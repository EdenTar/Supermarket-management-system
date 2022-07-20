package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Report.Category;
import Backend.DataAccess.DTOs.StockDTOS.CategoriesDTO;

import java.util.List;

public class CategoriesDAO extends DAO<PK, CategoriesDTO, Category>{
    public CategoriesDAO() {
        super(CategoriesDTO.class,IM.getInstance().getIdentityMap(Category.class));
    }

    @Override
    protected Category convertDtoToBusiness(CategoriesDTO dto) {
        return new Category((int)dto.getId(), dto.getName(), null);
    }

    @Override
    protected CategoriesDTO convertBusinessToDto(Category business) {
        // to do - change parent id and id
        return new CategoriesDTO(business.getId(),( business.getParent() == null) ? -1 : business.getParent().getId(), business.getName());
    }

    @Override
    protected CategoriesDTO createDTO(List<Object> listFields) {
        return new CategoriesDTO((long)(int)listFields.get(0), (long)(int)listFields.get(1), (String) listFields.get(2));
    }


    public List<Category> getSubCategories(Category business){
        List<Category> lst = getRowsFromDB("parent_ID = " + business.getId());
        lst.forEach(c -> c.setParent(business));
        return lst;
    }
}



