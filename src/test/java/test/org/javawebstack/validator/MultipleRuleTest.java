package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.mapper.Mapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class MultipleRuleTest {

    @Test
    public void testSimpleMultipleRules() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.name = "Test";
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.name = "123";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }


    private static class TestObject1 {
        @Rule({"required", "numeric"})
        String name;
    }

}
