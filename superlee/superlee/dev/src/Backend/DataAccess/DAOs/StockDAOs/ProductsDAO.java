package Backend.DataAccess.DAOs.StockDAOs;

import Backend.DataAccess.DAOs.DAO;
import Backend.DataAccess.DTOs.PrimaryKeys.PK;
import Backend.DataAccess.DTOs.StockDTOS.CategoriesDTO;
import Backend.DataAccess.IdentityMap.IM;
import Backend.Logic.LogicObjects.Product.Product;
import Backend.DataAccess.DTOs.StockDTOS.ProductsDTO;

import java.util.List;

public class ProductsDAO extends DAO<PK, ProductsDTO, Product>{

    private CategoriesDAO categoriesDAO;

    public ProductsDAO() {
        super(ProductsDTO.class,IM.getInstance().getIdentityMap(Product.class));
        categoriesDAO = new CategoriesDAO();
    }

    @Override
    protected Product convertDtoToBusiness(ProductsDTO dto)  {
        Product p = null;
        try {
            p = new Product("" + dto.getId(), dto.getName(), dto.getManufacturer(), dto.getPrice(), categoriesDAO.getRow(CategoriesDTO.getPK(dto.getCategoryId())), (int) dto.getDemandPerDay(), (int)dto.getItemIdRunner());
        } catch (Exception e) {
            System.out.println("problem in dal " + e.getMessage());
        }
        return p;
    }


    @Override
    protected ProductsDTO convertBusinessToDto(Product business) {
        //to do - change category_id
        return new ProductsDTO(Long.parseLong(business.getProductNumber()), business.getName(), business.getManufacturer(), business.getPrice(), business.getCategory().getId(),business.getDemandPerDay(), business.getItemID());
    }

    @Override
    protected ProductsDTO createDTO(List<Object> listFields) {
        return new ProductsDTO((long)(int)listFields.get(0),
                (String) listFields.get(1),
                (String) listFields.get(2),
                (double)listFields.get(3),
                (long)(int)listFields.get(4),
                (long)(int)listFields.get(5),
                (long)(int)listFields.get(6));
    }
}



