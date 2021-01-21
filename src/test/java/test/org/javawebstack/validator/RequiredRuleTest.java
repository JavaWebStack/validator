package test.org.javawebstack.validator;

import com.google.gson.annotations.Expose;
import org.javawebstack.abstractdata.AbstractMapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.Validator;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class RequiredRuleTest {

    @Test
    public void testSimpleRequiredRule(){
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        assertFalse(validator.validate(new AbstractMapper().toAbstract(test)).isValid());
        test.name = "Test";
        assertTrue(validator.validate(new AbstractMapper().toAbstract(test)).isValid());
    }

    @Test
    public void testInnerRequiredRule(){
        Validator validator = Validator.getValidator(TestObject2.class);
        TestObject2 test = new TestObject2();
        assertTrue(validator.validate(new AbstractMapper().toAbstract(test)).isValid());
        test.inners = new TestObject2.Inner[0];
        assertTrue(validator.validate(new AbstractMapper().toAbstract(test)).isValid());
        test.inners = new TestObject2.Inner[]{
                new TestObject2.Inner()
        };
        assertFalse(validator.validate(new AbstractMapper().toAbstract(test)).isValid());
        test.inners[0].name = "Test";
        assertTrue(validator.validate(new AbstractMapper().toAbstract(test)).isValid());
    }

    private static class TestObject1 {
        @Rule("required")
        String name;
    }

    private static class TestObject2 {
        private Inner[] inners;
        public static class Inner {
            @Rule("required")
            private String name;
        }
    }

}
