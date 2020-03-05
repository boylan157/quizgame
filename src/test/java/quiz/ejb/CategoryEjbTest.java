package quiz.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import quiz.entity.Category;
import quiz.entity.SubCategory;
;

import javax.ejb.EJB;

import java.util.List;

import static org.junit.Assert.*;

@RunWith(Arquillian.class)
public class CategoryEjbTest {

    @Deployment
    public static JavaArchive createDeployment(){
        return ShrinkWrap.create(JavaArchive.class)
                .addPackages(true, "quiz")
                .addAsResource("META-INF/persistence.xml");
    }

    @EJB
    private CategoryEjb categoryEjb;

    @EJB
    private ResetEjb resetEjb;

    @Before
    public void init(){
        resetEjb.resetDatabase();
    }

    @Test
    public void testNoCategory(){
        List<Category> list = categoryEjb.getAllCategories(false);
        assertEquals(0, list.size());
    }

    @Test
    public void testCreateCategory(){

        String category = "testCategory";
        Long id = categoryEjb.createCategory(category);
        assertNotNull(id);
    }

    @Test
    public void testGetCategory(){
        String category = "testCat";
        long createId = categoryEjb.createCategory(category);

        Category getResult = categoryEjb.getCategory(createId, false);

        assertEquals(category, getResult.getName());

    }

    @Test
    public void testCreateSubCategory(){
        String categoryName = "category";
        long categoryId =  categoryEjb.createCategory(categoryName);

        String subCategoryName = "subCategory";
        long subcategoryId = categoryEjb.createSubCategory(categoryId, subCategoryName);

        SubCategory savedSubcategory = categoryEjb.getSubCategory(subcategoryId);
        Category savedParentCategory = savedSubcategory.getParent();

        assertEquals(subCategoryName, savedSubcategory.getName());
        assertEquals(categoryName, savedParentCategory.getName());

    }

    @Test
    public void testGetAllCategories(){
        long a = categoryEjb.createCategory("a");
        long b = categoryEjb.createCategory("b");
        long c = categoryEjb.createCategory("c");

        categoryEjb.createSubCategory(a, "1");
        categoryEjb.createSubCategory(b, "2");
        categoryEjb.createSubCategory(c, "3");

        List<Category> categories = categoryEjb.getAllCategories(false);
        assertEquals(3, categories.size());

        Category first = categories.get(0);

        try {
            first.getSubCategories().size();
            fail();
        } catch (Exception e){

        }

        categories = categoryEjb.getAllCategories(true);

        first = categories.get(0);

        assertEquals(1, first.getSubCategories().size());
    }

    @Test
    public void testCreateTwice(){

        String ctg = "a";
        categoryEjb.createCategory(ctg);

        try {
            categoryEjb.createCategory(ctg);
            fail();
        } catch (Exception e){

        }
    }

}
