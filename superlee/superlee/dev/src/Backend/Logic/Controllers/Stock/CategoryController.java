package Backend.Logic.Controllers.Stock;


import Backend.Logic.LogicObjects.Product.Discount;
import Backend.Logic.LogicObjects.Report.Category;
import Backend.DataAccess.DAOs.StockDAOs.CategoriesDAO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class CategoryController {

    private CategoriesDAO categoriesDAO = new CategoriesDAO();

    private int category_id;
    public CategoryController(){
        category_id = 0;
        categoriesDAO.selectAll().forEach(c -> category_id = (int) Math.max(c.getId() + 1, category_id));
    }
    public Discount addDiscount(List<String> route, double discount, Date from, Date to) throws Exception {
        Category category = getCategory(route);
        return category.addDiscount(discount,from,to);
    }
    public Category addCategory(String categoryName, List<String> route) throws Exception {
        if (categoryName.equals("")) {
            throw new Exception("category name cannot be empty");
        }
        if (route.isEmpty()) {
            for (Category category : getMainCategories()) {
                if (category.getName().equals(categoryName)) {
                    throw new Exception("category already exist");
                }
            }
            Category category = new Category(category_id++, categoryName, null);
            categoriesDAO.insert(category);
            return category;
        }
        return getCategory(route).addCategory(category_id++,categoryName);
    }

    public List<Category> getMainCategories() {
        return categoriesDAO.getRowsFromDB("parent_ID = -1");
    }

    public Category getMainCategory(String name) throws Exception {
        for(Category category: getMainCategories()){
            if(category.getName().equals(name)){
                return category;
            }
        }
        throw new Exception("the main-category doesn't exist");
    }
    public Category getCategory(List<String> route) throws Exception {
        if(route.isEmpty()){
            throw new Exception("category doesn't exist(entered empty route)");
        }
        else{
            List<String> route_c = new LinkedList<>(route);
            return getMainCategory(route_c.remove(0)).getCategory(route_c);
        }
    }

    public List<Category> getSubCategories(List<String> route) throws Exception {
        if(route.size()<1){
            throw new Exception("route is not valid");
        }
        if(isCategoryExist(route.get(0))){
            for(Category category: getMainCategories()){
                if(category.getName().equals(route.get(0))){
                    List<String> route_c = new LinkedList<>(route);
                    route_c.remove(0);
                    return category.getSubCategories(route_c);
                }
            }
        }
        else{
            throw new Exception("category doesn't exist");
        }
        return null;
    }

    public boolean isCategoryExist(String name){
        for (Category category : getMainCategories()) {
            if (category.getName().equals(name)) {
                return true;
            }
        }
        return false;
    }
}
