package quiz.entity;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CategoryEntityTest extends EntityTestBase {



    @Test
    public void testTooLongName(){
        Category category = new Category();
        String name = new String(new char[150]);
        category.setName(name);
        assertFalse(persistInATransaction(category));
        category.setId(null);
        category.setName("NormalName");
        assertTrue(persistInATransaction(category));
    }

    @Test
    public void testUniqueName(){
        Category category = new Category();
        category.setName("name");
        assertTrue(persistInATransaction(category));
        Category category2 = new Category();
        category2.setName("name");
        assertFalse(persistInATransaction(category2));
    }


}


