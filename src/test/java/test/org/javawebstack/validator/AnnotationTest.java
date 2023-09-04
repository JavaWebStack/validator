package test.org.javawebstack.validator;


import org.javawebstack.abstractdata.mapper.Mapper;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.javawebstack.validator.rule.IntegerRule;
import org.javawebstack.validator.rule.RequiredRule;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AnnotationTest {
    @Test
    public void testIntegerAnnotation () {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.x = 6;
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.x = 1338;
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }

    private static class TestObject1 {
        @IntegerRule(min = 5, max = 1337)
        @RequiredRule
        int x;
    }
}
