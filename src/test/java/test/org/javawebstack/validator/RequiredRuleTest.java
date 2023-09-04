package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.mapper.Mapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.javawebstack.validator.rule.RequiredRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class RequiredRuleTest {

    @Test
    public void testSimpleRequiredRule() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.name = "Test";
        test.password = "123456";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }

    @Test
    public void testEmptyStringOption() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        test.name = "";
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.name = "Test";
        test.password = "";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }

    @Test
    public void testInnerRequiredRule() {
        Validator validator = Validator.getValidator(TestObject2.class);
        TestObject2 test = new TestObject2();
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.inners = new TestObject2.Inner[0];
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.inners = new TestObject2.Inner[]{
                new TestObject2.Inner()
        };
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.inners[0].name = "Test";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }


    private static class TestObject1 {
        @Rule("required")
        String name;
        @RequiredRule(allowEmptyStrings = true)
        String password;
    }

    private static class TestObject2 {
        private Inner[] inners;

        public static class Inner {
            @Rule("required")
            private String name;
        }
    }

}
