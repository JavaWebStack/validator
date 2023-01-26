package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.AbstractMapper;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.javawebstack.validator.rule.DoubleRule;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class DoubleRuleTest {
    @Test
    public void testSimple() {
        Validator validator = Validator.getValidator(SimpleTest.class);
        SimpleTest test = new SimpleTest();
        test.value = "hello world";
        assertFalse(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
        test.value = "13.37";
        assertTrue(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
    }

    @Test
    public void testEdgeCases() {
        Validator validator = Validator.getValidator(EdgeTest.class);
        EdgeTest test = new EdgeTest();
        test.value = "9.9";
        assertFalse(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
        test.value = "10.1";
        assertTrue(validator.validate(new ValidationContext(), new AbstractMapper().toAbstract(test)).isValid());
    }

    private class SimpleTest {
        @DoubleRule
        String value;
    }

    private class EdgeTest {
        @DoubleRule(min = 10)
        String value;
    }
}
