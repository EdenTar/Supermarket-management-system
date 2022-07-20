package Backend.ServiceLayer.ServiceObjects.Report;

import Backend.Logic.LogicObjects.Report.Category;
import Backend.ServiceLayer.ServiceObjects.Product.SDiscount;

import java.util.List;
import java.util.stream.Collectors;

public class SCategory {

    private String name;
    private List<String> subCategories;
    private List<String> parent;
    private List<SDiscount> discounts;

    public SCategory(String name, List<String> subCategories, List<String> parent, List<SDiscount> discounts) {
        this.name = name;
        this.subCategories = subCategories;
        this.parent = parent;
        this.discounts = discounts;
    }

    public SCategory(Category category){
        this(category.getName(), category.getSubCategories().stream().map(sb -> sb.getName()).collect(Collectors.toList()),
                category.getCategoryPath().stream().map(c -> c.getName()).collect(Collectors.toList()),
                category.getDiscounts().getDiscounts().stream().map(d -> new SDiscount(d)).collect(Collectors.toList()));

    }

    public String getName() {
        return name;
    }

    public List<String> getSubCategories() {
        return subCategories;
    }

    public List<String> getParent() {
        return parent;
    }

    public List<SDiscount> getDiscounts() {
        return discounts;
    }

    @Override
    public String toString() {
        return String.format("[Name: %s\n\nParents: %s\n\nDiscounts: %s]", getName(), printList(getParent()), printList(getDiscounts()));
    }

    private  <T> String printList(List<T> lst){
        String res = "<";
        int i = 0;
        for(T t : lst) {
            res += t.toString() + ((i++ == lst.size()-1) ? "" : ",");
        }
        return res + ">";
    }
}
