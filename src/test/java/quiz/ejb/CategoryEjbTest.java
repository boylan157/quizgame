package quiz.ejb;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import quiz.jpa.Category;
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

    }

    @Test
    public void testCreateSubCategory(){

    }

    @Test
    public void testGetAllCategories(){

    }

    @Test
    public void testCreateTwice(){

    }

}
