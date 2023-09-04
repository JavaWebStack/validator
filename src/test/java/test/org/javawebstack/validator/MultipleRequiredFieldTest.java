package test.org.javawebstack.validator;

import org.javawebstack.abstractdata.mapper.Mapper;
import org.javawebstack.validator.Rule;
import org.javawebstack.validator.ValidationContext;
import org.javawebstack.validator.Validator;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertFalse;

public class MultipleRequiredFieldTest {

    @Test
    public void testSimpleMultipleRules() {
        Validator validator = Validator.getValidator(TestObject1.class);
        TestObject1 test = new TestObject1();
        test.name = "Test";
        assertFalse(validator.validate(new ValidationContext(), new Mapper().map(test)).isValid());
    }


    private static class TestObject1 {
        @Rule("required")
        String name;
        @Rule("required")
        String name2;
    }

}
