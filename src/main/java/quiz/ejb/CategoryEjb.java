package quiz.ejb;


import quiz.jpa.Category;
import quiz.jpa.SubCategory;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.validation.constraints.NotNull;
import java.util.List;

@Stateless
public class CategoryEjb {

    @PersistenceContext
    private EntityManager em;


    protected Long createCategory(@NotNull String name){

        Category category = new Category();
        category.setName(name);

        em.persist(category);
        return category.getId();
    }

    protected Long createSubCategory(long parentId, String name){
        Category category = em.find(Category.class, parentId);
        if(category == null){
            throw new IllegalArgumentException("Category with id " + parentId + " does not exist");
        }

        SubCategory subCategory = new SubCategory();
        subCategory.setName(name);
        subCategory.setParent(category);
        em.persist(subCategory);

        category.getSubCategories().add(subCategory);
        return subCategory.getId();
    }

    protected List<Category> getAllCategories(boolean withSub){
        TypedQuery<Category> query = em.createQuery("select c from Category c", Category.class);
        List<Category> categories = query.getResultList();

        if(withSub){
            categories.forEach(c -> c.getSubCategories().size());
        }

        return categories;
    }

    private Category getCategory(long id, boolean withSub){
        Category category = em.find(Category.class, id);
        if(withSub && category != null){
            category.getSubCategories().size();
        }

        return category;
    }

    private SubCategory getSubCategory(long id){

        return em.find(SubCategory.class, id);
    }
}
