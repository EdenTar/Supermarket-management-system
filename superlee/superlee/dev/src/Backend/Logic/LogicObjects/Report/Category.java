package Backend.Logic.LogicObjects.Report;

import Backend.Logic.Controllers.Stock.DiscountHolder;
import Backend.Logic.LogicObjects.Product.Discount;
import Backend.DataAccess.DAOs.StockDAOs.CategoriesDAO;
import Backend.DataAccess.DAOs.StockDAOs.DiscountsDAO;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/* some definisions:
    route - the categories names from a category downstream to its sub-category and so on
    route examples - main-category => sub-category => sub-sub-category, sub-category=>sub-sub-category, main-category => sub-category.

    path - the categories themself from a category to its parent and so on, must end in main-category
    pate examples - sub-sub-category  => sub-category => main-category, sub-category => main-category.
 */


public class Category {


    private int id;
    private String name;
    private Category parent;
    private DiscountHolder discounts;
    private int discountID = 0;
    private CategoriesDAO categoriesDAO = new CategoriesDAO();

    public Category(int id, String name, Category parent){
        this.id = id;
        this.name = name;
        this.parent = parent;
        discounts = new DiscountHolder();
        new DiscountsDAO().getRowsFromDB("category_ID = " + id).forEach(d -> discountID = Math.max(Integer.parseInt(d.getId().split("c")[0]) + 1, discountID));
    }
    public int getId() {
        return id;
    }

    public void setParent(Category parent) {
        this.parent = parent;
    }

    public DiscountHolder getDiscounts() {
        return discounts;
    }

    public String getName(){
        return name;
    }
    public Category getParent(){
        return parent;
    }
    public List<Category> getSubCategories(){
        List<Category> lst =  categoriesDAO.getSubCategories(this);
        lst.forEach(c -> c.setParent(this));
        return lst;
    }
    public List<Category> getSubCategories(List<String> route) throws Exception {
        if(route.isEmpty()){
            return this.getSubCategories();
        }
        else if(!isExist(route)){
            throw new Exception("the route doesn't exist");
        }
        else{
            return getSubCategory(route.remove(0)).getSubCategories(route);
        }
    }
    public Category getSubCategory(String name) throws Exception {
        for(Category category: getSubCategories()){
            if(category.getName().equals(name)){
                return category;
            }
        }
        throw new Exception("the sub-category doesn't exist");
    }
    public Discount addDiscount(double discount, Date from, Date to){
        Discount toAdd = new Discount("" + (discountID++) + "c" + id, id, null, discount, from, to);
        this.discounts.add(toAdd);
        return toAdd;
    }
    public Boolean isExist(List<String> route) throws Exception {
        if(route.isEmpty()){
            return true;
        }
        try{
            List<String> route_c = new LinkedList<>(route);
            return getSubCategory(route_c.remove(0)).isExist(route_c);
        }catch (Exception e){
            throw new Exception("the route doesn't exist");
        }
    }

    public Category addCategory(int id, String categoryName) throws Exception {
        for(Category category: getSubCategories()) {
            if (category.getName().equals(categoryName)) {
                throw new Exception("category already exist");
            }
        }
        Category category = new Category(id, categoryName, this);
        categoriesDAO.insert(category);
        return category;
    }
    public void getCategoryPath(List<Category> path){
        path.add(this);
        if(parent != null) {
            this.parent.getCategoryPath(path);
        }
    }
    public List<Category> getCategoryPath(){
        List<Category> path = new LinkedList<>();
        path.add(this);
        if(parent != null) {
            parent.getCategoryPath(path);
        }
        return path;
    }
    public Discount getDiscount() {
        if (parent == null){
            return discounts.getMaxActiveDiscount();
        }
        else{
            Discount parentDiscount = parent.getDiscount();
            Discount maxActiveDiscount = discounts.getMaxActiveDiscount();
            if(maxActiveDiscount.getDiscount()<= parentDiscount.getDiscount()){
                return parentDiscount;
            }
            else{
                return maxActiveDiscount;
            }
        }
    }

    public Category getCategory(List<String> route) throws Exception {
        if(route.isEmpty()){
            return this;
        }
        else{
            return getSubCategory(route.remove(0)).getCategory(route);
        }
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Category category = (Category) o;
        if(category.getName().equals(getName()) && ((getParent() == null && category.getParent() == null) || category.getParent().equals(getParent()))){
            return true;
        }
        return false;

    }
}


