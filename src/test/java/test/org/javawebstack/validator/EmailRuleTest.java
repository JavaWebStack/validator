package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.mapper.Mapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class EmailRuleTest {

    @Test
    public void testSimpleEMailRule() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        test.email = "Test";
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
        test.email = "info@javawebstack.org";
        assertTrue(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }

    private static class TestObject1 {
        @Rule("email")
        String email;
    }

}
